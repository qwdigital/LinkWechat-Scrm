package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.enums.TrajectoryType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.annotation.SynchRecord;
import com.linkwechat.wecom.client.WeMomentsClient;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.customer.ExternalUserDetail;
import com.linkwechat.wecom.domain.dto.customer.FollowUserList;
import com.linkwechat.wecom.domain.dto.moments.*;
import com.linkwechat.wecom.mapper.WeMomentsMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WeMomentsServiceImpl extends ServiceImpl<WeMomentsMapper, WeMoments> implements IWeMomentsService {


    @Autowired
    WeMomentsClient weMomentsClient;


    @Autowired
    IWeUserService iWeUserService;


    @Autowired
    IWeMaterialService iWeMaterialService;


    @Autowired
    WeMomentsInteracteService weMomentsInteracteService;

    @Autowired
    IWeCustomerTrajectoryService iWeCustomerTrajectoryService;





    /**
     * 朋友圈列表
     * @param weMoments
     * @return
     */
    @Override
    public List<WeMoments> findMoments(WeMoments weMoments) {
        return this.baseMapper.findMoments(weMoments);
    }

    /**
     * 发送更新朋友圈
     * @param weMoments
     */
    @Override
    public void addOrUpdateMoments(WeMoments weMoments) throws InterruptedException {


        if(weMoments.getType().equals(new Integer(0))){//企业动态
            MomentsParamDto momentsParamDto=new MomentsParamDto();
            weMoments.setPushTime(new Date());
            weMoments.setIsLwPush(true);
            if(StringUtils.isNotEmpty(weMoments.getContent())){
                weMoments.getOtherContent().add(
                        WeMoments.OtherContent.builder()
                                .annexType(MediaType.TEXT.getMediaType())
                                .other(weMoments.getContent())
                                .build()
                );
                momentsParamDto.setText(
                        MomentsParamDto.Text.builder()
                                .content(weMoments.getContent())
                                .build()
                );
            }

            //设置附件
            List<WeMoments.OtherContent> otherContent = weMoments.getOtherContent();
            if(CollectionUtil.isNotEmpty(otherContent)){

                List<WeMoments.OtherContent> otherContents = otherContent.stream().filter(s -> StringUtils.isNotEmpty(s.getAnnexType()) && StringUtils.isNotEmpty(s.getAnnexUrl()))
                        .collect(Collectors.toList());


                if(CollectionUtil.isNotEmpty(otherContents)){

                    List<MomentsParamDto.BaseAttachments> attachments=new ArrayList<>();

                    //图片
                    if(weMoments.getContentType().equals(MediaType.IMAGE.getMediaType())){
                        otherContents.stream().forEach(image->{
                            String media_id = iWeMaterialService.uploadAttachmentMaterial(image.getAnnexUrl(),
                                    MediaType.IMAGE.getMediaType(),
                                    1
                                    , SnowFlakeUtil.nextId().toString()).getMedia_id();


                            if(StringUtils.isNotEmpty(media_id)){
                                attachments.add(
                                        MomentsParamDto.ImageAttachments.builder()
                                                .msgtype(MediaType.IMAGE.getMediaType())
                                                .image(
                                                        MomentsParamDto.Image.builder()
                                                                .media_id(
                                                                        media_id
                                                                )
                                                                .build()
                                                ).build()
                                );
                                weMoments.setContent(image.getAnnexUrl());

                            }



                        });
                    }

                    //视频
                    if(weMoments.getContentType().equals(MediaType.VIDEO.getMediaType())){
                        otherContents.stream().forEach(video->{

                            String media_id = iWeMaterialService.uploadAttachmentMaterial(video.getAnnexUrl(),
                                    MediaType.VIDEO.getMediaType(),
                                    1
                                    , SnowFlakeUtil.nextId().toString()).getMedia_id();


                            if(StringUtils.isNotEmpty(media_id)){
                                attachments.add(
                                        MomentsParamDto.VideoAttachments.builder()
                                                .msgtype(MediaType.VIDEO.getMediaType())
                                                .video(
                                                        MomentsParamDto.Video.builder()
                                                                .media_id(
                                                                        media_id
                                                                )
                                                                .build()
                                                ).build()
                                );
                                weMoments.setContent(video.getAnnexUrl());
                            }

                        });
                    }


                    //网页链接
                    if(weMoments.getContentType().equals(MediaType.LINK.getMediaType())){
                        otherContents.stream().forEach(link->{
                            attachments.add(
                                    MomentsParamDto.LinkAttachments.builder()
                                            .msgtype(MediaType.LINK.getMediaType())
                                            .link(
                                                    MomentsParamDto.Link.builder()
                                                            .url(link.getAnnexUrl())
//                                                            .media_id(
//                                                                    iWeMaterialService.uploadTemporaryMaterial(link.getAnnexUrl(),
//                                                                            MediaType.IMAGE.getMediaType()
//                                                                            ,SnowFlakeUtil.nextId().toString()).getMedia_id()
//                                                            )
                                                            .build()
                                            ).build()
                            );
                            weMoments.setContent(link.getAnnexUrl());
                        });



                    }

                    momentsParamDto.setAttachments(
                            attachments
                    );

                }



            }



            MomentsParamDto.VisibleRange visibleRange
                    = MomentsParamDto.VisibleRange.builder().build();

            //设置可见范围
            if(weMoments.getScopeType().equals(new Integer(0))){ //部分




                   if(StringUtils.isNotEmpty(weMoments.getCustomerTag())){ //客户标签
                       visibleRange.setExternal_contact_list(
                               MomentsParamDto.ExternalContactList.builder()
                                       .tag_list(weMoments.getCustomerTag().split(","))
                                       .build()
                       );
                   }

                   if(StringUtils.isNotEmpty(weMoments.getNoAddUser())){//指定发送人
                        visibleRange.setSender_list(
                                MomentsParamDto.SenderList.builder()
                                        .user_list(weMoments.getNoAddUser().split(","))
                                        .build()
                        );
                   }



               }else{ //全部

                List<WeUser> weUsers = iWeUserService.list();
                if(CollectionUtil.isNotEmpty(weUsers)){
                    visibleRange.setSender_list(
                            MomentsParamDto.SenderList.builder()
                                    .user_list(
                                            weUsers.stream().map(WeUser::getUserId).collect(Collectors.toList()).stream().toArray(String[]::new)
                                    )
                                    .build()
                    );
                    weMoments.setNoAddUser(
                            StringUtils.join(weUsers.stream().map(WeUser::getUserId).collect(Collectors.toList()).toArray(), ",")
                    );
                }


            }

            momentsParamDto.setVisible_range(
                    visibleRange
            );


            MomentsResultDto weResultDto = weMomentsClient.addMomentTask(
                       momentsParamDto
            );

            //入库
            if(weResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                   weMoments.setMomentId(weResultDto.getJobid());
                   this.saveOrUpdate(weMoments);
           }

        }


    }



    /**
     * 同步个人朋友圈
     * @param filterType
     */
    @Override
    @Transactional
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_PERSON_MOMENTS)
    public void synchPersonMoments(Integer filterType) {

        this.synchMoments(filterType);


    }


    /**
     * 同步企业朋友圈
     * @param filterType
     */
    @Override
    @Transactional
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_ENTERPRISE_MOMENTS)
    public void synchEnterpriseMoments(Integer filterType) {



        this.synchMoments(filterType);
    }


    /**
     * 朋友圈详情
     * @param momentId
     * @return
     */
    @Override
    public WeMoments findMomentsDetail(String momentId) {
        return this.baseMapper.findMomentsDetail(momentId);
    }


    /**
     * 同步指定员工个人朋友圈互动情况
     * @param userIds
     */
    @Override
    public void synchMomentsInteracte(List<String> userIds) {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        //异步同步一下标签库,解决标签不同步问题
        Threads.SINGLE_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                if(CollectionUtil.isNotEmpty(userIds)){
                    userIds.stream().forEach(userId->{


                        List<WeMoments> weMoments = list(new LambdaQueryWrapper<WeMoments>()
                                .apply(StringUtils.isNotEmpty(userId), "FIND_IN_SET('" + userId + "',add_user)")
                                .eq(WeMoments::getType,1)
                                .eq(WeMoments::getDelFlag, WeConstans.WE_SUCCESS_CODE));
                        if(CollectionUtil.isNotEmpty(weMoments)){
                            List<WeMomentsInteracte> interactes=new ArrayList<>();
                            //点赞集合
                            List<WeCustomerTrajectory.TrajectRel> dzTrajectrels=new ArrayList<>();


                            List<WeCustomerTrajectory.TrajectRel> plTrajectrels=new ArrayList<>();

                            weMoments.stream().forEach(moment->{
                                interactes.addAll(
                                        getInteracte(moment.getMomentId(), moment.getCreator())
                                );
                                interactes.stream().forEach(k->{

                                    WeCustomerTrajectory.TrajectRel trajectRel = WeCustomerTrajectory.TrajectRel.builder()
                                            .userId(moment.getCreator())
                                            .build();

                                    if(k.getInteracteUserType().equals(new Integer(1))){//客户
                                        trajectRel.setCustomerId(k.getInteracteUserId());
                                    }

                                    if(k.getInteracteType().equals(new Integer(0))){//评论
                                        plTrajectrels.add(trajectRel);
                                    }else{//点赞
                                        dzTrajectrels.add(trajectRel);
                                    }
                                });


                            });
                            if(CollectionUtil.isNotEmpty(interactes)){
                                weMomentsInteracteService.batchAddOrUpdate(interactes);
                            }

                            if(CollectionUtil.isNotEmpty(dzTrajectrels)){//点赞
                                iWeCustomerTrajectoryService.createTrajectory(dzTrajectrels, TrajectoryType.TRAJECTORY_TYPE_HDGZ.getType(), TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getType(),null,null);
                            }

                            if(CollectionUtil.isNotEmpty(plTrajectrels)){//评论
                                iWeCustomerTrajectoryService.createTrajectory(plTrajectrels, TrajectoryType.TRAJECTORY_TYPE_HDGZ.getType(), TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getType(),null,null);

                            }

                        }



                    });

                }

            }
        });



    }


    /**
     * 同步朋友圈
     */

    private void synchMoments(Integer filterType) {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        //异步同步一下标签库,解决标签不同步问题
        Threads.SINGLE_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                SecurityContextHolder.setContext(securityContext);


                List<MomentsListDetailResultDto.Moment> moments=new ArrayList<>();
                getByMoment(null,moments,filterType);

                if(CollectionUtil.isNotEmpty(moments)){

                    List<WeMoments> weMoments=new ArrayList<>();

                    List<WeMomentsInteracte> interactes=new ArrayList<>();

                    moments.stream().forEach(moment -> {

                    if(moment.getCreate_type().equals(new Integer(1))){//个人,获取互动数据

                        interactes.addAll(getInteracte(moment.getMoment_id(),moment.getCreator()));


//                            MomentsInteracteResultDto momentComments = weMomentsClient.get_moment_comments(MomentsInteracteParamDto.builder()
//                                    .moment_id(moment.getMoment_id())
//                                    .userid(moment.getCreator())
//                                    .build());
//                            if(momentComments.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
//                                List<MomentsInteracteResultDto.Interacte> comment_list
//                                        = momentComments.getComment_list();
//
//                                if(CollectionUtil.isNotEmpty(comment_list)){//评论
//                                    comment_list.stream().forEach(k->{
//                                        interactes.add(
//                                                WeMomentsInteracte.builder()
//                                                        .interacteUserType(StringUtils.isNotEmpty(k.getUserid())?new Integer(0):new Integer(1))
//                                                        .interacteType(new Integer(0))
//                                                        .interacteUserId(StringUtils.isNotEmpty(k.getUserid())?k.getUserid():k.getExternal_userid())
//                                                        .interacteTime(new Date(k.getCreate_time()* 1000L))
//                                                        .momentId(moment.getMoment_id())
//                                                        .build()
//                                        );
//                                    });
//
//                                }
//
//                                List<MomentsInteracteResultDto.Interacte> like_list
//                                        = momentComments.getLike_list();
//
//                                if(CollectionUtil.isNotEmpty(like_list)){ //点赞
//                                    like_list.stream().forEach(k->{
//                                        interactes.add(
//                                                WeMomentsInteracte.builder()
//                                                        .interacteUserType(StringUtils.isNotEmpty(k.getUserid())?new Integer(0):new Integer(1))
//                                                        .interacteType(new Integer(1))
//                                                        .interacteUserId(StringUtils.isNotEmpty(k.getUserid())?k.getUserid():k.getExternal_userid())
//                                                        .interacteTime(new Date(k.getCreate_time()* 1000L))
//                                                        .momentId(moment.getMoment_id())
//                                                        .build()
//                                        );
//                                    });
//                                }
//
//
//
//                            }
                        }








                        WeMoments weMoment=WeMoments.builder()
                                .type(moment.getCreate_type())
                                .scopeType(moment.getVisible_type())
                                .addUser(moment.getCreator())
                                .pushTime(new Date(moment.getCreate_time().getTime()* 1000L))
                                .momentId(moment.getMoment_id())
                                .creator(moment.getCreator())
                                .build();

                        //设置发表范围
                        if(moment.getCreate_type().equals(new Integer(0))){
                            getSendResult(weMoment);
                        }


                        List<WeMoments.OtherContent> otherContents=new ArrayList<>();

                        //文本
                        Optional.ofNullable(moment.getText()).ifPresent(k->{

                            if(StringUtils.isNotEmpty(k.getContent())){
                                otherContents.add(
                                        WeMoments.OtherContent.builder()
                                                .other(k.getContent())
                                                .annexType(MediaType.TEXT.getMediaType())
                                                .build()
                                );
                                weMoment.setContent(k.getContent());
                                weMoment.setContentType(MediaType.TEXT.getMediaType());
                            }

                        });



                        //图片
                        Optional.ofNullable(moment.getImage()).ifPresent(k->{
                            if(CollectionUtil.isNotEmpty(k)){
                                k.stream().forEach(image->{


                                String jpg = iWeMaterialService.mediaGet(image.getMedia_id(), MediaType.IMAGE.getType(), "jpg");
                                 weMoment.setContent(jpg);

                                    otherContents.add(
                                            WeMoments.OtherContent.builder()
                                                    .annexType(MediaType.IMAGE.getMediaType())
                                                    .annexUrl(jpg)
                                                    .build()
                                    );
                                });
                                weMoment.setContentType(MediaType.IMAGE.getMediaType());
                            }


                        });

                        //视频
                        Optional.ofNullable(moment.getVideo()).ifPresent(k->{

                            String video = iWeMaterialService.mediaGet(k.getMedia_id(), MediaType.VIDEO.getType(), "mp4");

                            weMoment.setContent(video);

                            otherContents.add(
                                    WeMoments.OtherContent.builder()
                                            .annexType(MediaType.VIDEO.getMediaType())
                                            .annexUrl(video)
//                                            .other(iWeMaterialService.mediaGet(k.getThumb_media_id(), MediaType.IMAGE.getType(),"jpg"))
                                            .build()
                            );
                            weMoment.setContentType(MediaType.VIDEO.getMediaType());
                        });


                        //链接
                        Optional.ofNullable(moment.getLink()).ifPresent(k->{
                            weMoment.setContent(k.getUrl());

                            otherContents.add(
                                    WeMoments.OtherContent.builder()
                                            .annexType(MediaType.LINK.getMediaType())
                                            .annexUrl(k.getUrl())
                                            .other(k.getTitle())
                                            .build()
                            );
                            weMoment.setContentType(MediaType.LINK.getMediaType());
                        });

                        if(CollectionUtil.isNotEmpty(otherContents)){
                            weMoment.setOtherContent(otherContents);
                        }

                        weMoments.add(weMoment);




                    });


                    if(filterType.equals(new Integer(0))){
                        baseMapper.removePushLwPush();
                    }
                     saveOrUpdateBatch(weMoments);
                    if(CollectionUtil.isNotEmpty(interactes)){
                        weMomentsInteracteService.batchAddOrUpdate(interactes);
                    }


                }

            }
        });




    }


    //获取互动数据
    private List<WeMomentsInteracte> getInteracte(String momentId,String creator){
        List<WeMomentsInteracte> interactes=new ArrayList<>();

        MomentsInteracteResultDto momentComments = weMomentsClient.get_moment_comments(MomentsInteracteParamDto.builder()
                .moment_id(momentId)
                .userid(creator)
                .build());

        if(momentComments.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
            List<MomentsInteracteResultDto.Interacte> comment_list
                    = momentComments.getComment_list();

            if(CollectionUtil.isNotEmpty(comment_list)){//评论
                comment_list.stream().forEach(k->{
                    interactes.add(
                            WeMomentsInteracte.builder()
                                    .interacteUserType(StringUtils.isNotEmpty(k.getUserid())?new Integer(0):new Integer(1))
                                    .interacteType(new Integer(0))
                                    .interacteUserId(StringUtils.isNotEmpty(k.getUserid())?k.getUserid():k.getExternal_userid())
                                    .interacteTime(new Date(k.getCreate_time()* 1000L))
                                    .momentId(momentId)
                                    .build()
                    );
                });

            }

            List<MomentsInteracteResultDto.Interacte> like_list
                    = momentComments.getLike_list();

            if(CollectionUtil.isNotEmpty(like_list)){ //点赞
                like_list.stream().forEach(k->{
                    interactes.add(
                            WeMomentsInteracte.builder()
                                    .interacteUserType(StringUtils.isNotEmpty(k.getUserid())?new Integer(0):new Integer(1))
                                    .interacteType(new Integer(1))
                                    .interacteUserId(StringUtils.isNotEmpty(k.getUserid())?k.getUserid():k.getExternal_userid())
                                    .interacteTime(new Date(k.getCreate_time()* 1000L))
                                    .momentId(momentId)
                                    .build()
                    );
                });
            }



        }

        return interactes;

    }


    /**
     * 设置员工发送结果
     * @param weMoments
     */
    private void getSendResult(WeMoments weMoments){

        MomentsResultDto moment_task = weMomentsClient.get_moment_task(MomentsParamDto.builder()
                .moment_id(weMoments.getMomentId())
                .build());

        if(moment_task.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
            List<MomentsResultDto.TaskList> task_list = moment_task.getTask_list();
            if(CollectionUtil.isNotEmpty(task_list)){
                task_list.stream().collect(Collectors.groupingBy(MomentsResultDto.TaskList::getPublish_status)).forEach((k,v)->{
                        if(k.equals(new Integer(0))){//未发表
                           weMoments.setNoAddUser(v.stream().map(MomentsResultDto.TaskList::getUserid).collect(Collectors.joining(",")));
                        }else if(k.equals(new Integer(1))){//已发表
                            weMoments.setAddUser(
                                    v.stream().map(MomentsResultDto.TaskList::getUserid).collect(Collectors.joining(","))
                            );
                        }
                });
            }else{
                List<WeUser> weUsers = iWeUserService.list();
                if(CollectionUtil.isNotEmpty(weUsers)){
                    weMoments.setNoAddUser(weUsers.stream().map(WeUser::getUserId).collect(Collectors.joining(",")));
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
    private void getByMoment(String nextCursor,List<MomentsListDetailResultDto.Moment> list,Integer filterType) {


        MomentsListDetailResultDto moment_list = weMomentsClient.get_moment_list(MomentsListDetailParamDto.builder()
                .start_time(DateUtils.getBeforeByDayLongTime(-30))
                .end_time(DateUtils.getBeforeByDayLongTime(0))
                .cursor(nextCursor)
                 .filter_type(filterType)
                .build());
        if (WeConstans.WE_SUCCESS_CODE.equals(moment_list.getErrcode())
                || WeConstans.NOT_EXIST_CONTACT.equals(moment_list.getErrcode())
                && CollectionUtil.isNotEmpty(moment_list.getMoment_list())) {
            list.addAll(moment_list.getMoment_list());
            if (StringUtils.isNotEmpty(moment_list.getNext_cursor())) {
                getByMoment(moment_list.getNext_cursor(), list,filterType);
            }
        }

    }






}
