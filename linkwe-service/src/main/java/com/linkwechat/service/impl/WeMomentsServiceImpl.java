package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.moments.entity.WeMoments;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsMapper;
import com.linkwechat.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeMomentsServiceImpl extends ServiceImpl<WeMomentsMapper, WeMoments> implements IWeMomentsService {


    @Resource
    private QwMomentsClient qwMomentsClient;

    @Autowired
    private IWeMaterialService iWeMaterialService;


    @Autowired
    private WeMomentsInteracteService weMomentsInteracteService;

    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IWeCustomerService iWeCustomerService;


    /**
     * 朋友圈列表
     *
     * @param weMoments
     * @return
     */
    @Override
    public List<WeMoments> findMoments(WeMoments weMoments) {
        return this.baseMapper.findMoments(weMoments);
    }

    /**
     * 发送更新朋友圈
     *
     * @param weMoments
     */
    @Override
    public void addOrUpdateMoments(WeMoments weMoments) throws InterruptedException {
        if (weMoments.getType().equals(new Integer(0))) {//企业动态
            MomentsParamDto momentsParamDto = new MomentsParamDto();
            weMoments.setPushTime(new Date());
            weMoments.setIsLwPush(true);
            if (StringUtils.isNotEmpty(weMoments.getContent())) {
                weMoments.getOtherContent().add(WeMoments.OtherContent.builder().annexType(MediaType.TEXT.getMediaType()).other(weMoments.getContent()).build());
                momentsParamDto.setText(MomentsParamDto.Text.builder().content(weMoments.getContent()).build());
            }
            //设置附件
            List<WeMoments.OtherContent> otherContent = weMoments.getOtherContent();
            if (CollectionUtil.isNotEmpty(otherContent)) {
                List<WeMoments.OtherContent> otherContents = otherContent.stream().filter(s -> StringUtils.isNotEmpty(s.getAnnexType()) && StringUtils.isNotEmpty(s.getAnnexUrl())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(otherContents)) {
                    List<Object> attachments = new ArrayList<>();
                    //图片
                    if (weMoments.getContentType().equals(MediaType.IMAGE.getMediaType())) {
                        otherContents.stream().forEach(image -> {
                            String media_id = iWeMaterialService.uploadAttachmentMaterial(image.getAnnexUrl(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                            if (StringUtils.isNotEmpty(media_id)) {
                                attachments.add(MomentsParamDto.ImageAttachments.builder().msgtype(MediaType.IMAGE.getMediaType()).image(MomentsParamDto.Image.builder().media_id(media_id).build()).build());
                                weMoments.setContent(image.getAnnexUrl());
                            }
                        });
                    }
                    //视频
                    if (weMoments.getContentType().equals(MediaType.VIDEO.getMediaType())) {
                        otherContents.stream().forEach(video -> {
                            String media_id = iWeMaterialService.uploadAttachmentMaterial(video.getAnnexUrl(), MediaType.VIDEO.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                            if (StringUtils.isNotEmpty(media_id)) {
                                attachments.add(MomentsParamDto.VideoAttachments.builder().msgtype(MediaType.VIDEO.getMediaType()).video(MomentsParamDto.Video.builder().media_id(media_id).build()).build());
                                weMoments.setContent(video.getAnnexUrl());
                            }
                        });
                    }
                    //网页链接
                    if (weMoments.getContentType().equals(MediaType.LINK.getMediaType())) {
                        otherContents.stream().forEach(link -> {
                            String media_id = iWeMaterialService.uploadAttachmentMaterial(link.getOther(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                            if (StringUtils.isNotEmpty(media_id)) {
                                MomentsParamDto.Link build = new MomentsParamDto.Link();
                                build.setUrl(link.getAnnexUrl());
                                build.setMedia_id(media_id);
                                if (StringUtils.isNotEmpty(link.getTitle())) {
                                    build.setTitle(link.getTitle());
                                }
                                attachments.add(MomentsParamDto.LinkAttachments.builder().msgtype(MediaType.LINK.getMediaType()).link(build).build());
                                weMoments.setContent(link.getAnnexUrl());
                            }
                        });
                    }
                    momentsParamDto.setAttachments(attachments);
                }
            }
            MomentsParamDto.VisibleRange visibleRange = MomentsParamDto.VisibleRange.builder().build();

            //设置可见范围
            if (weMoments.getScopeType().equals(new Integer(0))) { //部分
                if (StringUtils.isNotEmpty(weMoments.getCustomerTag())) { //客户标签
                    visibleRange.setExternal_contact_list(MomentsParamDto.ExternalContactList.builder().tag_list(weMoments.getCustomerTag().split(",")).build());
                }
                if (StringUtils.isNotEmpty(weMoments.getNoAddUser())) {//指定发送人
                    visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weMoments.getNoAddUser().split(",")).build());
                }
            } else { //全部
                SysUser sysUser = new SysUser();
                List<SysUser> weUsers = qwSysUserClient.list(sysUser).getData();
                if (CollectionUtil.isNotEmpty(weUsers)) {
                    visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weUsers.stream().map(SysUser::getWeUserId).toArray(String[]::new)).build());
                    weMoments.setNoAddUser(StringUtils.join(weUsers.stream().map(SysUser::getWeUserId).toArray(), ","));
                }
            }
            momentsParamDto.setVisible_range(visibleRange);
            MomentsResultDto weResultDto = qwMomentsClient.addMomentTask(momentsParamDto).getData();

            //入库
            if (null != weResultDto) {
                if (weResultDto.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
                    weMoments.setMomentId(weResultDto.getJobid());
                    this.saveOrUpdate(weMoments);
                }
            }
        }
    }


    /**
     * 同步个人朋友圈
     *
     * @param filterType
     */
    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_PERSON_MOMENTS)
    public void synchPersonMoments(Integer filterType) {
        this.synchMoments(filterType);
    }


    /**
     * 同步企业朋友圈
     *
     * @param filterType
     */
    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_ENTERPRISE_MOMENTS)
    public void synchEnterpriseMoments(Integer filterType) {
        this.synchMoments(filterType);
    }


    /**
     * 朋友圈详情
     *
     * @param momentId
     * @return
     */
    @Override
    public WeMoments findMomentsDetail(String momentId) {
        return this.baseMapper.findMomentsDetail(momentId);
    }


    /**
     * 同步朋友圈个人互动情况逻辑
     *
     * @param msg
     */
    @Override
    @Async
    public void synchMomentsInteracteHandler(String msg) {

        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
        SecurityContextHolder.setUserType(loginUser.getUserType());
        List<String> weUserIds = loginUser.getWeUserIds();

        if (CollectionUtil.isNotEmpty(weUserIds)) {
            weUserIds.stream().forEach(userId -> {

                List<WeMoments> weMoments = list(
                        new LambdaQueryWrapper<WeMoments>().apply(StringUtils.isNotEmpty(userId),
                                        "FIND_IN_SET('" + userId + "',add_user)").eq(WeMoments::getType, 1)
                                .eq(WeMoments::getDelFlag, WeConstans.WE_SUCCESS_CODE));
                if (CollectionUtil.isNotEmpty(weMoments)) {
                    List<WeMomentsInteracte> interactes = new ArrayList<>();
                    Map<String, SysUser> currentTenantSysUser = iWeCustomerService.findCurrentTenantSysUser();

                    weMoments.stream().forEach(moment -> {
                        weMomentsInteracteService.remove(new LambdaQueryWrapper<WeMomentsInteracte>()
                                .eq(WeMomentsInteracte::getMomentId, moment.getMomentId()));
                        interactes.addAll(getInteracte(moment.getMomentId(), moment.getCreator(), currentTenantSysUser.get(moment.getCreator())));


                    });
                    if (CollectionUtil.isNotEmpty(interactes)) {
                        weMomentsInteracteService.saveBatch(interactes);

                        interactes.stream().forEach(interacte -> {
                            if (new Integer(1).equals(interacte.getInteracteUserType())) {//互动人员为客户
                                iWeCustomerTrajectoryService.createInteractionTrajectory(
                                        interacte.getInteracteUserId(), interacte.getMomentCreteOrId(), new Integer(1).equals(interacte.getInteracteType()) ? TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getType() :
                                                TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getType(), null
                                );
                            }

                        });

                    }


                }
            });
        }
    }


    /**
     * 同步指定员工个人朋友圈互动情况
     *
     * @param userIds
     */
    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_MOMENTS_INTERACTE)
    public void synchMomentsInteracte(List<String> userIds) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setWeUserIds(userIds);
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeHdMomentsRk(), JSONObject.toJSONString(loginUser));


    }


    /**
     * 同步朋友圈
     */
    private void synchMoments(Integer filterType) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setFilterType(filterType);
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeMomentsRk(), JSONObject.toJSONString(loginUser));

    }


    /**
     * 监听mq，同步数据
     *
     * @param msg
     */
    @Override
    @Async
    public void synchWeMomentsHandler(String msg) {

        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
        SecurityContextHolder.setUserType(loginUser.getUserType());

        Integer filterType = loginUser.getFilterType();

        if (null != filterType) {
            List<MomentsListDetailResultDto.Moment> moments = new ArrayList<>();

            getByMoment(null, moments, filterType);

            if (CollectionUtil.isNotEmpty(moments)) {
                Map<String, SysUser> currentTenantSysUser = iWeCustomerService.findCurrentTenantSysUser();

                List<WeMoments> weMoments = new ArrayList<>();

                List<WeMomentsInteracte> interactes = new ArrayList<>();

                moments.stream().forEach(moment -> {

                    SysUser sysUser = currentTenantSysUser.get(moment.getCreator());

                    if (moment.getCreate_type().equals(new Integer(1))) {//个人,获取互动数据

                        interactes.addAll(getInteracte(moment.getMoment_id(), moment.getCreator(), sysUser));

                    }


                    WeMoments weMoment = new WeMoments();
                    weMoment.setType(moment.getCreate_type());
                    weMoment.setScopeType(moment.getVisible_type());
                    weMoment.setAddUser(moment.getCreator());
                    weMoment.setPushTime(new Date(moment.getCreate_time().getTime() * 1000L));
                    weMoment.setMomentId(moment.getMoment_id());
                    weMoment.setCreator(moment.getCreator());


                    if (null != sysUser) {
                        weMoment.setCreateBy(sysUser.getUserName());
                        weMoment.setCreateById(sysUser.getUserId());
                        weMoment.setUpdateBy(sysUser.getUserName());
                        weMoment.setUpdateById(sysUser.getUserId());
                    }

                    //设置发表范围
                    if (moment.getCreate_type().equals(new Integer(0))) {
                        getSendResult(weMoment);
                    }


                    List<WeMoments.OtherContent> otherContents = new ArrayList<>();

                    //文本
                    Optional.ofNullable(moment.getText()).ifPresent(k -> {

                        if (StringUtils.isNotEmpty(k.getContent())) {
                            otherContents.add(WeMoments.OtherContent.builder().other(k.getContent())
                                    .annexType(MediaType.TEXT.getMediaType()).build());
                            weMoment.setContent(k.getContent());
                            weMoment.setContentType(MediaType.TEXT.getMediaType());
                        }

                    });


                    //图片
                    Optional.ofNullable(moment.getImage()).ifPresent(k -> {
                        if (CollectionUtil.isNotEmpty(k)) {
                            k.stream().forEach(image -> {


                                String jpg = iWeMaterialService.mediaGet(image.getMedia_id(),
                                        MediaType.IMAGE.getType(), "jpg");
                                weMoment.setContent(jpg);

                                otherContents.add(
                                        WeMoments.OtherContent.builder().annexType(MediaType.IMAGE.getMediaType())
                                                .annexUrl(jpg).build());
                            });
                            weMoment.setContentType(MediaType.IMAGE.getMediaType());
                        }


                    });

                    //视频
                    Optional.ofNullable(moment.getVideo()).ifPresent(k -> {

                        String video = iWeMaterialService.mediaGet(k.getMedia_id(), MediaType.VIDEO.getType(),
                                "mp4");

                        weMoment.setContent(video);

                        otherContents.add(WeMoments.OtherContent.builder().annexType(MediaType.VIDEO.getMediaType())
                                .annexUrl(video)
//                                            .other(iWeMaterialService.mediaGet(k.getThumb_media_id(), MediaType.IMAGE.getType(),"jpg"))
                                .build());
                        weMoment.setContentType(MediaType.VIDEO.getMediaType());
                    });


                    //链接
                    Optional.ofNullable(moment.getLink()).ifPresent(k -> {
                        weMoment.setContent(k.getUrl());

                        otherContents.add(WeMoments.OtherContent.builder().annexType(MediaType.LINK.getMediaType())
                                .annexUrl(k.getUrl()).other(k.getTitle()).build());
                        weMoment.setContentType(MediaType.LINK.getMediaType());
                    });

                    if (CollectionUtil.isNotEmpty(otherContents)) {
                        weMoment.setOtherContent(otherContents);
                    }
                    weMoment.setIsLwPush(false);
                    weMoments.add(weMoment);
                });

                if (filterType.equals(new Integer(0))) {
                    //朋友圈内容同步之后，这一步把所有的内容都删了,导致了素材数据的丢失，去掉了
                    //WangYX 2022-11-11
                    //baseMapper.removePushLwPush();
                }

                saveOrUpdateBatch(weMoments);

                if (CollectionUtil.isNotEmpty(interactes)) {
                    weMomentsInteracteService.remove(new LambdaQueryWrapper<WeMomentsInteracte>()
                            .in(WeMomentsInteracte::getMomentId, weMoments.stream().map(WeMoments::getMomentId).collect(Collectors.toList())));
                    weMomentsInteracteService.saveBatch(interactes);
                }
            }
        }


    }


    //获取互动数据
    private List<WeMomentsInteracte> getInteracte(String momentId, String creator, SysUser sysUser) {
        List<WeMomentsInteracte> interactes = new ArrayList<>();

        MomentsInteracteResultDto momentComments = qwMomentsClient.comments(
                MomentsInteracteParamDto.builder().moment_id(momentId).userid(creator).build()).getData();

        if (momentComments.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
            List<MomentsInteracteResultDto.Interacte> comment_list = momentComments.getComment_list();

            if (CollectionUtil.isNotEmpty(comment_list)) {//评论
                comment_list.stream().forEach(k -> {
                    WeMomentsInteracte weMomentsInteracte = WeMomentsInteracte.builder()
                            .interacteUserType(StringUtils.isNotEmpty(k.getUserid()) ? new Integer(0) : new Integer(1))
                            .interacteType(new Integer(0)).interacteUserId(
                                    StringUtils.isNotEmpty(k.getUserid()) ? k.getUserid() : k.getExternal_userid())
                            .interacteTime(new Date(k.getCreate_time() * 1000L)).momentId(momentId).build();
                    weMomentsInteracte.setCreateTime(new Date());
                    weMomentsInteracte.setUpdateTime(new Date());
                    weMomentsInteracte.setMomentCreteOrId(creator);
                    if (null != sysUser) {
                        weMomentsInteracte.setCreateBy(sysUser.getUserName());
                        weMomentsInteracte.setCreateById(sysUser.getUserId());
                        weMomentsInteracte.setUpdateBy(sysUser.getUserName());
                        weMomentsInteracte.setUpdateById(sysUser.getUserId());
                    }

                    interactes.add(weMomentsInteracte);
                });

            }

            List<MomentsInteracteResultDto.Interacte> like_list = momentComments.getLike_list();

            if (CollectionUtil.isNotEmpty(like_list)) { //点赞
                like_list.stream().forEach(k -> {
                    WeMomentsInteracte weMomentsInteracte = WeMomentsInteracte.builder()
                            .interacteUserType(StringUtils.isNotEmpty(k.getUserid()) ? new Integer(0) : new Integer(1))
                            .interacteType(new Integer(1)).interacteUserId(
                                    StringUtils.isNotEmpty(k.getUserid()) ? k.getUserid() : k.getExternal_userid())
                            .interacteTime(new Date(k.getCreate_time() * 1000L)).momentId(momentId).build();
                    weMomentsInteracte.setCreateTime(new Date());
                    weMomentsInteracte.setUpdateTime(new Date());

                    if (null != sysUser) {
                        weMomentsInteracte.setCreateBy(sysUser.getUserName());
                        weMomentsInteracte.setCreateById(sysUser.getUserId());
                        weMomentsInteracte.setUpdateBy(sysUser.getUserName());
                        weMomentsInteracte.setUpdateById(sysUser.getUserId());
                    }

                    interactes.add(weMomentsInteracte);
                });
            }
        }
        return interactes;
    }


    /**
     * 设置员工发送结果
     *
     * @param weMoments
     */
    private void getSendResult(WeMoments weMoments) {
        MomentsResultDto moment_task = qwMomentsClient.get_moment_task(
                MomentsParamDto.builder().moment_id(weMoments.getMomentId()).build()).getData();

        if (null != moment_task) {
            if (moment_task.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
                List<MomentsResultDto.TaskList> task_list = moment_task.getTask_list();
                if (CollectionUtil.isNotEmpty(task_list)) {
                    task_list.stream().collect(Collectors.groupingBy(MomentsResultDto.TaskList::getPublish_status))
                            .forEach((k, v) -> {
                                if (k.equals(new Integer(0))) {//未发表
                                    weMoments.setNoAddUser(v.stream().map(MomentsResultDto.TaskList::getUserid)
                                            .collect(Collectors.joining(",")));
                                } else if (k.equals(new Integer(1))) {//已发表
                                    weMoments.setAddUser(v.stream().map(MomentsResultDto.TaskList::getUserid)
                                            .collect(Collectors.joining(",")));
                                }
                            });
                } else {
                    List<SysUser> weUsers = qwSysUserClient.listAll().getData();
                    if (CollectionUtil.isNotEmpty(weUsers)) {
                        weMoments.setNoAddUser(weUsers.stream().map(SysUser::getWeUserId).collect(Collectors.joining(",")));
                    }
                }
            }
        }
    }


    /**
     * 批量获取企业朋友圈(30天内数据)
     *
     * @param nextCursor
     * @param list
     */
    private void getByMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, Integer filterType) {
        MomentsListDetailResultDto moment_list = qwMomentsClient.momentList(MomentsListDetailParamDto.builder().start_time(DateUtils.getBeforeByDayLongTime(-30)).end_time(DateUtils.getBeforeByDayLongTime(0)).cursor(nextCursor).filter_type(filterType).build()).getData();
        if (null != moment_list) {
            if (WeConstans.WE_SUCCESS_CODE.equals(moment_list.getErrCode()) || WeConstans.NOT_EXIST_CONTACT.equals(moment_list.getErrCode()) && CollectionUtil.isNotEmpty(moment_list.getMoment_list())) {
                list.addAll(moment_list.getMoment_list());
                if (StringUtils.isNotEmpty(moment_list.getNext_cursor())) {
                    getByMoment(moment_list.getNext_cursor(), list, filterType);
                }
            }
        }

    }
}