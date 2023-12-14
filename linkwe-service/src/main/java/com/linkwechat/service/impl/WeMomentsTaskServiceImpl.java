package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.config.mybatis.LwBaseMapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.*;
import com.linkwechat.common.enums.moments.task.WeMomentsTaskSendTypEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.WeBacthMakeCustomerTag;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.moments.entity.*;
import com.linkwechat.domain.moments.query.*;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsCustomerMapper;
import com.linkwechat.mapper.WeMomentsTaskMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
public class WeMomentsTaskServiceImpl extends ServiceImpl<WeMomentsTaskMapper, WeMomentsTask> implements IWeMomentsTaskService {

    @Resource
    private WeMomentsTaskMapper weMomentsTaskMapper;
    @Resource
    private QwMomentsClient qwMomentsClient;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;
    @Resource
    private IWeMomentsUserService weMomentsUserService;

    @Autowired
    private IWeMomentsCustomerService weMomentsCustomerService;
    @Resource
    private IWeMomentsAttachmentsService weMomentsAttachmentsService;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private IWeMomentsTaskRelationService weMomentsTaskRelationService;
    @Resource
    private IWeMaterialService weMaterialService;
    @Resource
    private QwAppSendMsgService qwAppSendMsgService;
    @Resource
    private IWeCorpAccountService weCorpAccountService;

    @Resource
    private IWeMomentsInteracteService weMomentsInteracteService;

    @Resource
    private IWeMomentsEstimateUserService weMomentsEstimateUserService;
    @Resource
    private IWeMomentsEstimateCustomerService weMomentsEstimateCustomerService;

    @Resource
    private IWeMomentsUserService iWeMomentsUserService;


    @Autowired
     private IWeCustomerService iWeCustomerService;

    @Autowired
    private QwFileClient qwFileClient;

    @Autowired
    private IWeTagService iWeTagService;



    @Override
    public List<WeMomentsTaskVO> selectList(WeMomentsTaskListRequest request) {
        return weMomentsTaskMapper.list(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WeMomentsTaskAddRequest request) {
        //校验朋友圈内容和附件是否同时为空
        if (StrUtil.isBlank(request.getContent()) && CollectionUtil.isEmpty(request.getMaterialIds())) {
            throw new ServiceException("朋友圈内容和附件两者不能同时为空！", HttpStatus.BAD_REQUEST);
        }

        if (BeanUtil.isNotEmpty(request.getExecuteTime())) {
            if (LocalDateTime.now().isAfter(request.getExecuteTime())) {
                throw new ServiceException("任务开始时间不能晚于当前时间！", HttpStatus.BAD_REQUEST);
            }
            if (request.getExecuteTime().isAfter(request.getExecuteEndTime())) {
                throw new ServiceException("任务开始时间不能晚于结束时间！", HttpStatus.BAD_REQUEST);
            }
        }
        if (LocalDateTime.now().isAfter(request.getExecuteEndTime())) {
            throw new ServiceException("任务结束时间不能晚于当前时间！", HttpStatus.BAD_REQUEST);
        }
        //新增
        WeMomentsTask task = BeanUtil.copyProperties(request, WeMomentsTask.class);
        task.setType(request.getSendType().equals(WeMomentsTaskSendTypEnum.ENTERPRISE_GROUP_SEND.getCode())?0:1);
        //点赞标签
        if (CollectionUtil.isNotEmpty(request.getLikeTagIds())) {
            task.setLikeTagIds(JSONObject.toJSONString(request.getLikeTagIds()));
        }
        //评论标签
        if (CollectionUtil.isNotEmpty(request.getCommentTagIds())) {
            task.setCommentTagIds(JSONObject.toJSONString(request.getCommentTagIds()));
        }
        task.setSenderList(task.getScopeType()==0?iWeCustomerService.findLimitSenderInfoWeCustomerList():task.getSenderList());

        //任务入库
        if(this.save(task)){
            //添加朋友圈附件
            weMomentsAttachmentsService.addMomentsAttachments(task.getId(), request.getMaterialIds());



            List<WeAddGroupMessageQuery.SenderInfo> senderList = task.getSenderList();

            if(CollectionUtil.isNotEmpty(senderList)){


                if (task.getSendType().equals(WeMomentsTaskSendTypEnum.USER_GROUP_SEND.getCode())) {

                    //3.新增预估朋友圈执行员工,以及客户（成员群发）
                    //预估执行员工
                    weMomentsEstimateUserService.batchInsert(task.getId(),
                            senderList.stream().map(WeAddGroupMessageQuery.SenderInfo::getUserId).collect(Collectors.toList()));
                    List<WeMomentsEstimateCustomer> customers = new ArrayList<>();
                    senderList.stream().forEach(k->{

                        List<WeCustomersVo> weCustomerList = iWeCustomerService.findWeCustomerList(WeCustomersQuery.builder()
                                .externalUserids(StringUtils.join(k.getCustomerList(), ","))
                                        .firstUserId(k.getUserId())
                                .build(), null);
                        //预估可查看客户
                        if(CollectionUtil.isNotEmpty(weCustomerList)){
                            for (WeCustomersVo weCustomer : weCustomerList) {
                                WeMomentsEstimateCustomer weMomentsEstimateCustomer = new WeMomentsEstimateCustomer();
                                weMomentsEstimateCustomer.setId(IdUtil.getSnowflake().nextId());
                                weMomentsEstimateCustomer.setMomentsTaskId(task.getId());
                                weMomentsEstimateCustomer.setWeUserId(weCustomer.getFirstUserId());
                                weMomentsEstimateCustomer.setExternalUserid(weCustomer.getExternalUserid());
                                weMomentsEstimateCustomer.setCustomerName(weCustomer.getCustomerName());
                                weMomentsEstimateCustomer.setDelFlag(Constants.COMMON_STATE);
                                customers.add(weMomentsEstimateCustomer);
                            }
                        }
                    });
                    if(CollectionUtil.isNotEmpty(customers)){
                        List<List<WeMomentsEstimateCustomer>> partitions = Lists.partition(customers, 1000);
                        for(List<WeMomentsEstimateCustomer> partition:partitions){
                            ((LwBaseMapper)weMomentsEstimateCustomerService.getBaseMapper()).insertBatchSomeColumn(partition);
                        }

                    }

                }else if(task.getSendType().equals(WeMomentsTaskSendTypEnum.ENTERPRISE_GROUP_SEND.getCode())){
                    //预估执行员工
                    AjaxResult<List<SysUser>> ajaxResult = qwSysUserClient.findSysUser(SysUserQuery.builder()
                            .weUserIds(senderList.stream().map(WeAddGroupMessageQuery.SenderInfo::getUserId).collect(Collectors.toList()))
                            .build());
                    if(null !=ajaxResult && CollectionUtil.isNotEmpty(ajaxResult.getData())){
                        weMomentsUserService.addMomentsUser(task.getId(),ajaxResult.getData());
                    }


                    List<WeMomentsCustomer> customers = new ArrayList<>();
                    senderList.stream().forEach(k->{
                        List<WeCustomersVo> weCustomerList = iWeCustomerService.findWeCustomerList(WeCustomersQuery.builder()
                                .externalUserids(StringUtils.join(k.getCustomerList(), ","))
                                .firstUserId(k.getUserId())
                                .build(), null);


                        //预估可查看客户
                        if(CollectionUtil.isNotEmpty(weCustomerList)){
                            for (WeCustomersVo weCustomer : weCustomerList) {
                                //构建数据
                                WeMomentsCustomer weMomentsCustomer = new WeMomentsCustomer();
                                weMomentsCustomer.setMomentsTaskId(task.getId());
                                weMomentsCustomer.setWeUserId(weCustomer.getFirstUserId());
                                weMomentsCustomer.setUserName(weCustomer.getUserName());
                                weMomentsCustomer.setExternalUserid(weCustomer.getExternalUserid());
                                weMomentsCustomer.setCustomerName(weCustomer.getCustomerName());
                                weMomentsCustomer.setDeliveryStatus(1);
                                weMomentsCustomer.setDelFlag(Constants.COMMON_STATE);
                                customers.add(weMomentsCustomer);
                            }

                        }

                    });

                    if(CollectionUtil.isNotEmpty(customers)){
                        List<List<WeMomentsCustomer>> partitions = Lists.partition(customers, 1000);
                        for(List<WeMomentsCustomer> partition:partitions){
                            ((WeMomentsCustomerMapper)weMomentsCustomerService.getBaseMapper()).insertBatchSomeColumn(partition);
                        }

                    }

                }

            }

        }

        return task.getId();
    }


    /**
     * 异步执行-立即执行发送朋友圈
     *
     * @param task 朋友圈任务
     * @return
     * @author WangYX
     * @date 2023/07/05 9:59
     */
    @Override
    public void immediatelySendMoments(WeMomentsTask task) {
        log.info("异步执行-立即执行发送朋友圈:Id为{}", task.getId());
        //延迟100毫秒
        long intervalTime = 100L;
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayExecuteRk(), task.getId().toString(), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", intervalTime);
            return message;
        });
    }

    @Override
    public WeMomentsTaskVO get(Long weMomentsTaskId) {
        LambdaQueryWrapper<WeMomentsTask> queryWrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
        queryWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
        WeMomentsTask weMomentsTask = weMomentsTaskMapper.selectOne(queryWrapper);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            return null;
        }
        WeMomentsTaskVO weMomentsTaskVO = BeanUtil.copyProperties(weMomentsTask, WeMomentsTaskVO.class);
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getCustomerTag())) {
            weMomentsTaskVO.setCustomerTag(JSONObject.parseArray(weMomentsTask.getCustomerTag(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getDeptIds())) {
            weMomentsTaskVO.setDeptIds(JSONObject.parseArray(weMomentsTask.getDeptIds(), Long.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTask.getPostIds())) {
            weMomentsTaskVO.setPosts(JSONObject.parseArray(weMomentsTask.getPostIds(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getLikeTagIds())) {
            weMomentsTaskVO.setLikeTagIds(JSONObject.parseArray(weMomentsTask.getLikeTagIds(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getCommentTagIds())) {
            weMomentsTaskVO.setCommentTagIds(JSONObject.parseArray(weMomentsTask.getCommentTagIds(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getUserIds())) {
            weMomentsTaskVO.setUserIds(JSONObject.parseArray(weMomentsTask.getUserIds(), String.class));
        }

        //不是在linkwechat平台发布的
        if (weMomentsTask.getIsLwPush().equals(0)) {
            //发送者
            LambdaQueryWrapper<WeMomentsUser> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeMomentsUser::getMomentsTaskId, weMomentsTask.getId());
            wrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
            List<WeMomentsUser> list = weMomentsUserService.list(wrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                List<String> weUserIds = list.stream().map(WeMomentsUser::getWeUserId).collect(Collectors.toList());
                weMomentsTaskVO.setUserIds(weUserIds);
            }
            //客户数
            LambdaQueryWrapper<WeMomentsCustomer> customerWrapper = Wrappers.lambdaQuery();
            customerWrapper.eq(WeMomentsCustomer::getMomentsTaskId, weMomentsTask.getId());
            customerWrapper.eq(WeMomentsCustomer::getDelFlag, Constants.COMMON_STATE);
            int count = weMomentsCustomerService.count(customerWrapper);
            weMomentsTaskVO.setCustomerNum(count);
        }

        LambdaQueryWrapper<WeMomentsAttachments> wrapper = Wrappers.lambdaQuery(WeMomentsAttachments.class);
        wrapper.eq(WeMomentsAttachments::getMomentsTaskId, weMomentsTaskId);
        List<WeMomentsAttachments> list = weMomentsAttachmentsService.list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            //是否在lw平台发布的:1:是;0:否;
            if (weMomentsTaskVO.getIsLwPush().equals(1)) {
                List<Long> materialList = new ArrayList<>();
                list.stream().forEach(i->materialList.add(i.getMaterialId()));
                List<WeMaterial> weMaterials = weMaterialService.listByIds(materialList);
                //防止重复的素材被过滤掉
                if (BeanUtil.isNotEmpty(weMaterials)) {
                    Map<Long, WeMaterial> collect = weMaterials.stream().collect(Collectors.toMap(WeMaterial::getId, Function.identity()));
                    List<WeMaterial> attachments = new ArrayList<>();
                    materialList.forEach(i -> {
                        WeMaterial weMaterial = collect.get(i);
                        if (BeanUtil.isNotEmpty(weMaterial)) {
                            attachments.add(weMaterial);
                        }
                    });
                    weMomentsTaskVO.setMaterialList(attachments);
                }
            }
            if (weMomentsTaskVO.getIsLwPush().equals(0)) {
                List<WeMaterial> weMaterials = new ArrayList<>();

                Optional<WeMomentsAttachments> videos = list.stream().filter(i -> i.getMsgType().equals(1)).findFirst();
                Optional<WeMomentsAttachments> link = list.stream().filter(i -> i.getMsgType().equals(2)).findFirst();

                if (videos.isPresent()) {
                    Optional<WeMomentsAttachments> cover = list.stream().filter(i -> i.getMsgType().equals(0)).findFirst();
                    WeMomentsAttachments weMomentsAttachments = videos.get();
                    //视频
                    WeMaterial weMaterial = new WeMaterial();
                    weMaterial.setMaterialUrl(weMomentsAttachments.getMediaIdUrl());
                    weMaterial.setMediaType(CategoryMediaType.VIDEO.getType().toString());
                    weMaterial.setCoverUrl(cover.isPresent() ? cover.get().getMediaIdUrl() : null);
                    weMaterials.add(weMaterial);
                } else if (link.isPresent()) {
                    Optional<WeMomentsAttachments> cover = list.stream().filter(i -> i.getMsgType().equals(0)).findFirst();
                    WeMomentsAttachments weMomentsAttachments = link.get();
                    //链接
                    WeMaterial weMaterial = new WeMaterial();
                    weMaterial.setMaterialName(weMomentsAttachments.getLinkTitle());
                    weMaterial.setMaterialUrl(weMomentsAttachments.getLinkUrl());
                    weMaterial.setMediaType(CategoryMediaType.IMAGE_TEXT.getType().toString());
                    weMaterial.setCoverUrl(cover.isPresent() ? cover.get().getMediaIdUrl() : null);
                    weMaterials.add(weMaterial);
                } else {
                    for (WeMomentsAttachments weMomentsAttachments : list) {
                        if (weMomentsAttachments.getMsgType().equals(0)) {
                            //图片
                            WeMaterial weMaterial = new WeMaterial();
                            weMaterial.setMaterialUrl(weMomentsAttachments.getMediaIdUrl());
                            weMaterial.setMediaType(CategoryMediaType.IMAGE.getType().toString());
                            weMaterials.add(weMaterial);
                        }
                    }
                }
                weMomentsTaskVO.setMaterialList(weMaterials);
            }
        }
        return weMomentsTaskVO;
    }


    @Override
    public void sendWeMoments(Long weMomentsTaskId) throws IOException {
        //任务不存在，不执行
        LambdaQueryWrapper<WeMomentsTask> queryWrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
        queryWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
        WeMomentsTask weMomentsTask = this.getOne(queryWrapper);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            return;
        }

        //获取发送素材
        LambdaQueryWrapper<WeMomentsAttachments> wrapper = Wrappers.lambdaQuery(WeMomentsAttachments.class);
        wrapper.eq(WeMomentsAttachments::getMomentsTaskId, weMomentsTaskId);
        List<WeMomentsAttachments> list = weMomentsAttachmentsService.list(wrapper);
        List<Long> materialIds = new ArrayList<>();
        list.forEach(i->materialIds.add(i.getMaterialId()));

        //发送朋友圈
        sendWeMoments(weMomentsTask, materialIds);

        //发送完成之后，修改朋友圈状态为已开始
        LambdaUpdateWrapper<WeMomentsTask> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
        updateWrapper.set(WeMomentsTask::getStatus, 2);
        this.update(updateWrapper);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSendMoments(Long weMomentsTaskId) {
        WeMomentsTask weMomentsTask = this.getById(weMomentsTaskId);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            //数据不存在，不处理
            return;
        }
//        if (weMomentsTask.getStatus().equals(3)) {
//            //任务已结束不处理
//            return;
//        }
        if (weMomentsTask.getStatus().equals(1)) {
            //任务未开始,直接设置为任务状态为已结束
            LambdaUpdateWrapper<WeMomentsTask> updateWrapper = Wrappers.lambdaUpdate(WeMomentsTask.class);
            updateWrapper.set(WeMomentsTask::getStatus, 3);
            updateWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
            this.update(updateWrapper);
            return;
        }else{ //进行中获取已经结束的，取消发送

            //1.修改朋友圈任务状态为已结束
            LambdaUpdateWrapper<WeMomentsTask> updateWrapper = Wrappers.lambdaUpdate(WeMomentsTask.class);
            updateWrapper.set(WeMomentsTask::getStatus, 3);
            updateWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
            this.update(updateWrapper);

            //2.停止企微朋友圈
            LambdaQueryWrapper<WeMomentsTaskRelation> queryWrapper = Wrappers.lambdaQuery(WeMomentsTaskRelation.class);
            queryWrapper.eq(WeMomentsTaskRelation::getMomentTaskId, weMomentsTaskId);
            List<WeMomentsTaskRelation> list = weMomentsTaskRelationService.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                for (WeMomentsTaskRelation relation : list) {
                    //停止朋友圈
                    qwMomentsClient.cancel_moment_task(MomentsCancelDTO.builder().moment_id(relation.getMomentId()).build());
                }
            }


        }

    }

    /**
     * 创建朋友圈
     *
     * @param weMomentsTask 朋友圈任务
     * @param materialIds   素材id集合
     * @author WangYX
     * @date 2023/06/09 18:00
     */
    private void sendWeMoments(WeMomentsTask weMomentsTask, List<Long> materialIds) throws IOException {


        //企微发送
        if (weMomentsTask.getSendType().equals(WeMomentsTaskSendTypEnum.ENTERPRISE_GROUP_SEND.getCode())){

            List<WeMomentsUser> weMomentsUsers = iWeMomentsUserService.list(new LambdaQueryWrapper<WeMomentsUser>()
                    .eq(WeMomentsUser::getMomentsTaskId, weMomentsTask.getId()));
            if(CollectionUtil.isNotEmpty(weMomentsUsers)){
                List<String> customerTagIds = new ArrayList<>();
                if(StringUtils.isNotEmpty(weMomentsTask.getCustomerTag())){
                    customerTagIds = JSONObject.parseArray(weMomentsTask.getCustomerTag(), String.class);
                }

                MomentsParamDto dto = this.buildMomentsParam(weMomentsTask.getContent(), materialIds, weMomentsTask.getScopeType(), customerTagIds,
                        weMomentsUsers.stream().map(WeMomentsUser::getWeUserId).collect(Collectors.toList()));
                AjaxResult<MomentsResultDto> result = qwMomentsClient.addMomentTask(dto);
                if (result.getCode() == HttpStatus.SUCCESS) {
                    MomentsResultDto data = result.getData();
                    String jobId = data.getJobid();

                    //3.1 保存job和momentId的关联表
                    DateTime jobIdExpireTime = DateUtil.offsetDay(DateUtil.date(), 1);
                    WeMomentsTaskRelation build = WeMomentsTaskRelation.builder().id(SnowFlakeUtil.nextId()).momentTaskId(weMomentsTask.getId()).jobId(jobId).jobIdExpire(DateUtil.toLocalDateTime(jobIdExpireTime)).build();
                    weMomentsTaskRelationService.save(build);

                }

            }




            //成员发送
        }else if(weMomentsTask.getSendType().equals(WeMomentsTaskSendTypEnum.USER_GROUP_SEND.getCode())){
            //朋友圈执行成员
            List<WeMomentsEstimateUser> estimateUsers = weMomentsEstimateUserService.list(new LambdaQueryWrapper<WeMomentsEstimateUser>()
                    .eq(WeMomentsEstimateUser::getMomentsTaskId, weMomentsTask.getId()));

            if(CollectionUtil.isNotEmpty(estimateUsers)){

                //4.成员群发,需要员工在移动端，调用sdk来发送朋友圈
                //通过应用消息下发提醒
                WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
                if (BeanUtil.isEmpty(weCorpAccount)) {
                    throw new ServiceException("企微基础数据未配置！");
                }
                List<List<String>> split = CollectionUtil.split( estimateUsers.stream().map(WeMomentsEstimateUser::getWeUserId).collect(Collectors.toList()), 1000);
                for (List<String> list : split) {
                    QwAppMsgBody body = new QwAppMsgBody();
                    body.setCorpId(weCorpAccount.getCorpId());
                    body.setCorpUserIds(list);

                    //设置消息模板
                    WeMessageTemplate template = new WeMessageTemplate();
                    //设置消息内型
                    template.setMsgType("text");
                    //设置应用id
                    template.setAppId(weCorpAccount.getAgentId());

                    String url = StringUtils.format(linkWeChatConfig.getMomentsUrl(), weMomentsTask.getId());
                    String content = "【朋友圈营销】<br/> 管理员下发一条【" + weMomentsTask.getName() + "】的朋友圈营销任务，请及时执行 <br/><br/>" +
                            "<a href='" + url + "'>去处理</a>";

                    template.setContent(content);
                    body.setMessageTemplates(template);
                    qwAppSendMsgService.appMsgSend(body);
                }


            }

        }
    }


    /**
     * 获取员工
     *
     * @param request 获取员工参数
     * @return {@link List<SysUser>}
     * @author WangYX
     * @date 2023/06/07 15:44
     */
    public List<SysUser> getSysUser(WeMomentsTaskAddRequest request) {
        return weMomentsUserService.getMomentsTaskExecuteUser(request.getScopeType(), request.getDeptIds(), request.getPosts(), request.getUserIds());
    }

    /**
     * 构建创建企微圈数据
     *
     * @param content      文本内容
     * @param materialIds  附件内容
     * @param scopeType    发送类型
     * @param exIds 客户id
     * @param weUserIds    发送员工
     * @return {@link MomentsParamDto}
     * @author WangYX
     * @date 2023/06/08 16:05
     */
    private MomentsParamDto buildMomentsParam(String content, List<Long> materialIds, Integer scopeType, List<String> exIds, List<String> weUserIds) throws IOException {
        MomentsParamDto.MomentsParamDtoBuilder builder = MomentsParamDto.builder();
        //文本内容
        if (StrUtil.isNotBlank(content)) {
            builder.text(MomentsParamDto.Text.builder().content(content).build());
        }
        //附件
        if (CollectionUtil.isNotEmpty(materialIds)) {
            List<Object> attachments = new ArrayList<>();
            List<WeMaterial> list = weMaterialService.listByIds(materialIds);
            List<WeMaterial> materialList = new ArrayList<>();
            //防止重复的素材被过滤掉
            if (BeanUtil.isNotEmpty(list)) {
                Map<Long, WeMaterial> collect = list.stream().collect(Collectors.toMap(WeMaterial::getId, Function.identity()));
                materialIds.forEach(i -> {
                    WeMaterial weMaterial = collect.get(i);
                    if (BeanUtil.isNotEmpty(weMaterial)) {
                        materialList.add(weMaterial);
                    }
                });
            }
            for (WeMaterial weMaterial : materialList) {
                //图片和海报
                if (weMaterial.getMediaType().equals(CategoryMediaType.IMAGE.getType().toString()) || weMaterial.getMediaType().equals(CategoryMediaType.POSTER.getType().toString())) {
                    String media_id = weMaterialService.uploadAttachmentMaterial(weMaterial.getMaterialUrl(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                    if (StringUtils.isNotEmpty(media_id)) {
                        attachments.add(MomentsParamDto.ImageAttachments.builder().msgtype(MediaType.IMAGE.getMediaType()).image(MomentsParamDto.Image.builder().media_id(media_id).build()).build());
                    }
                }

                //视频
                if (weMaterial.getMediaType().equals(CategoryMediaType.VIDEO.getType().toString())) {
                    convertWeChatMaterial(attachments, weMaterial);
                }

                //图文
                if (weMaterial.getMediaType().equals(CategoryMediaType.IMAGE_TEXT.getType().toString())) {
                    String media_id = weMaterialService.uploadAttachmentMaterial(weMaterial.getCoverUrl(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                    if (StringUtils.isNotEmpty(media_id)) {
                        MomentsParamDto.Link build = new MomentsParamDto.Link();
                        //素材详情地址
                        build.setUrl(weMaterial.getMaterialUrl());
                        build.setMedia_id(media_id);
                        build.setTitle(truncationStr(weMaterial.getMaterialName(), 64));
                        attachments.add(MomentsParamDto.LinkAttachments.builder().msgtype(MediaType.LINK.getMediaType()).link(build).build());
                    }
                }

                //文章
                if (weMaterial.getMediaType().equals(CategoryMediaType.ARTICLE.getType().toString())) {
                    convertWeChatMaterial(attachments, weMaterial);
                }

                //文件
                if (weMaterial.getMediaType().equals(CategoryMediaType.FILE.getType().toString())) {
                    int indexOf = weMaterial.getMaterialUrl().lastIndexOf(".");
                    String substring = weMaterial.getMaterialUrl().substring(indexOf);

                    FileEntity fileEntity = qwFileClient.upload(
                            new MockMultipartFile(".png", SnowFlakeUtil.nextId()+".png",null,
                                    new ClassPathResource("/static/image/"+substring+".png").getInputStream())
                    ).getData();

                    weMaterial.setCoverUrl(
                            fileEntity.getUrl()
                    );
                    convertWeChatMaterial(attachments, weMaterial);
                }
            }
            builder.attachments(attachments);
        }

        MomentsParamDto.VisibleRange visibleRange = MomentsParamDto.VisibleRange.builder().build();

        //设置可见范围
            //指定发送人
            if (CollectionUtil.isNotEmpty(weUserIds)) {
                visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weUserIds.toArray(new String[0])).build());
            }

        builder.visible_range(visibleRange);
        return builder.build();
    }

    /**
     * 截取字符串
     *
     * @param str    字符串
     * @param length 截取的字节长度
     * @return {@link String}
     * @author WangYX
     * @date 2023/06/13 16:57
     */
    private String truncationStr(String str, int length) {
        assert length > 16;
        assert StrUtil.isNotEmpty(str);
        int strLength = str.getBytes(StandardCharsets.UTF_8).length;
        if (strLength >= length) {
            for (int i = 16; i < length; i++) {
                if (str.substring(0, i).getBytes(StandardCharsets.UTF_8).length >= length) {
                    str = str.substring(0, i - 1);
                    break;
                }
            }
        }
        return str;
    }

    /**
     * 内容中心素材转换为企微素材
     *
     * @param attachments 附件
     * @param weMaterial  内容中心素材
     * @author WangYX
     * @date 2023/06/13 16:44
     */
    private void convertWeChatMaterial(List<Object> attachments, WeMaterial weMaterial) {
        //封面
        String media_id = weMaterialService.uploadAttachmentMaterial(weMaterial.getCoverUrl(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
        if (StringUtils.isNotEmpty(media_id)) {
            MomentsParamDto.Link build = new MomentsParamDto.Link();
            //移动端素材详情地址
            String materialDetailUrl = linkWeChatConfig.getMaterialDetailUrl();
            String url = StringUtils.format(materialDetailUrl, weMaterial.getId());
            build.setUrl(url);
            build.setMedia_id(media_id);
            build.setTitle(truncationStr(weMaterial.getMaterialName(), 64));
            attachments.add(MomentsParamDto.LinkAttachments.builder().msgtype(MediaType.LINK.getMediaType()).link(build).build());
        }
    }


    private MomentsParamDto buildMomentsParam(WeMomentsTaskAddRequest request) throws IOException {
        List<SysUser> sysUser = this.getSysUser(request);
        List<String> weUserIds = sysUser.stream().map(SysUser::getWeUserId).collect(Collectors.toList());
        return this.buildMomentsParam(request.getContent(), request.getMaterialIds(), request.getScopeType(), request.getCustomerTag(), weUserIds);
    }


    @Override
    public void syncMoments(List<String> taskIds) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setBusinessIds( Joiner.on(",").join(taskIds));
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeMomentsRk(), JSONObject.toJSONString(loginUser));

    }

    @Override
    public void syncWeMomentsHandler(String msg) {

        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());

        List<WeMomentsTaskRelation> weMomentsTaskRelations = weMomentsTaskRelationService.list(new LambdaQueryWrapper<WeMomentsTaskRelation>()
                        .in(StringUtils.isNotEmpty(loginUser.getBusinessIds()),WeMomentsTaskRelation::getMomentTaskId,ListUtil.toList(loginUser.getBusinessIds().split(",")))
                .isNotNull(WeMomentsTaskRelation::getMomentTaskId));

        if(CollectionUtil.isNotEmpty(weMomentsTaskRelations)){
            weMomentsTaskRelations.stream().forEach(k->{
                //同步互动数据
                weMomentsInteracteService.syncAddWeMomentsInteracte(k.getMomentTaskId(),k.getMomentId());
                //更新员工执行情况
                weMomentsUserService.syncUpdateMomentsUser(k.getMomentTaskId(),k.getMomentId());
                //更新客户送达情况
                weMomentsCustomerService.syncMomentsCustomerSendSuccess(k.getMomentTaskId(),k.getMomentId());
            });
        }


        List<WeMomentsTask> weMomentsTasks = this.listByIds(ListUtil.toList(loginUser.getBusinessIds().split(",")));
        if(CollectionUtil.isNotEmpty(weMomentsTasks)){
            weMomentsTasks.stream().forEach(kk->{

                //点赞标签
                if(StringUtils.isNotEmpty(kk.getLikeTagIds())){
                    List<WeMomentsInteracte> weMomentsInteractes = weMomentsInteracteService.list(new LambdaQueryWrapper<WeMomentsInteracte>()
                            .eq(WeMomentsInteracte::getInteracteUserType, 1)
                            .eq(WeMomentsInteracte::getMomentsTaskId, kk.getId()));

                    List<WeMakeCustomerTag>  weMakeCustomerTags=new ArrayList<>();

                    if(CollectionUtil.isNotEmpty(weMomentsInteractes)){
                        weMomentsInteractes.stream().forEach(interactes->{


                            if(StringUtils.isNotEmpty(kk.getLikeTagIds())&&interactes.getInteracteType()==1){
                                weMakeCustomerTags.add(
                                        WeMakeCustomerTag.builder().addTag(
                                                        iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                                                                .in(WeTag::getTagId,JSONObject.parseArray(kk.getLikeTagIds(),String.class)))
                                                )
                                                .isCompanyTag(true)
                                                .extIdAndWeUserId(interactes.getInteracteUserId()+":"+interactes.getWeUserId())
                                                .userId(interactes.getWeUserId())
                                                .externalUserid(interactes.getInteracteUserId()).build()

                                );
                            }


                            if(StringUtils.isNotEmpty(kk.getCommentTagIds())&&interactes.getInteracteType()==0){
                                weMakeCustomerTags.add(
                                        WeMakeCustomerTag.builder().addTag(
                                                        iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                                                                .in(WeTag::getTagId,JSONObject.parseArray(kk.getCommentTagIds(),String.class)))
                                                )
                                                .isCompanyTag(true)
                                                .extIdAndWeUserId(interactes.getInteracteUserId()+":"+interactes.getWeUserId())
                                                .userId(interactes.getWeUserId())
                                                .externalUserid(interactes.getInteracteUserId()).build()
                                );
                            }
                        });

                    }

                    if(CollectionUtil.isNotEmpty(weMomentsInteractes)){
                        //针对同一个客户既点赞又评论的场景，打标签
                        List<WeMakeCustomerTag>  newWeMakeCustomerTags=new ArrayList<>();
                        weMakeCustomerTags.stream().collect(Collectors
                                .groupingBy(WeMakeCustomerTag::getExtIdAndWeUserId)).forEach((k,v)->{

                            WeMakeCustomerTag customerTag=new WeMakeCustomerTag();
                            List<WeTag> weTags=new ArrayList<>();
                            v.stream().forEach(ks->{
                                weTags.addAll(ks.getAddTag());
                                customerTag.setIsCompanyTag(true);
                                customerTag.setUserId(ks.getUserId());
                                customerTag.setExternalUserid(ks.getExternalUserid());

                            });
                            customerTag.setAddTag(weTags);

                            newWeMakeCustomerTags.add(customerTag);
                        });


                        iWeCustomerService.batchMakeLabel(WeBacthMakeCustomerTag.builder().addOrRemove(true)
                                .weMakeCustomerTagList(newWeMakeCustomerTags)
                                .build());
                    }


                }

            });


        }


    }

    //朋友圈同步分页入库
    public void getMomentPage(MomentsListDetailParamDto query){
        MomentsListDetailResultDto result = qwMomentsClient.momentList(query).getData();
        if (null != result) {
            if (WeConstans.WE_SUCCESS_CODE.equals(result.getErrCode()) || WeConstans.NOT_EXIST_CONTACT.equals(result.getErrCode()) && CollectionUtil.isNotEmpty(result.getMoment_list())) {
                syncAndSave(result.getMoment_list());
                if (StringUtils.isNotEmpty(result.getNext_cursor())) {
                    query.setCursor(result.getNext_cursor());
                    getMomentPage(query);
                }
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void groupSendFinish(WeMomentsSyncGroupSendRequest request) {




        log.info("成员朋友发送结束:{}", JSONObject.toJSONString(request));

        if (BeanUtil.isEmpty(SecurityUtils.getLoginUser())) {
            throw new ServiceException("用户未登录", HttpStatus.UNAUTHORIZED);
        }

//        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
//        log.info("成员朋友发送结束！，任务Id：{}，发送成员：{}", request.getWeMomentsTaskId(), sysUser.getWeUserId());

//        WeMomentsTask weMomentsTask = this.getById(request.getWeMomentsTaskId());
//        if (BeanUtil.isEmpty(weMomentsTask)) {
//            return;
//        }

        //判断是否已经执行过
        LambdaQueryWrapper<WeMomentsEstimateUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WeMomentsEstimateUser::getMomentsTaskId, request.getWeMomentsTaskId());
        wrapper.eq(WeMomentsEstimateUser::getWeUserId, SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        WeMomentsEstimateUser one = weMomentsEstimateUserService.getOne(wrapper);
        if (BeanUtil.isEmpty(one)) {
            return;
        }
        //执行过之后，无需在执行
        if (one.getExecuteStatus().equals(1)) {
            return;
        }

        //修改执行状态为已执行
        one.setExecuteStatus(1);
        weMomentsEstimateUserService.updateById(one);

        //更新执行状态
//        weMomentsEstimateCustomerService.update(WeMomentsEstimateCustomer.builder()
//                        .momentsTaskId(weMomentsTask.getId())
//                        .deliveryStatus(0)
//                .build(), new LambdaQueryWrapper<WeMomentsEstimateCustomer>()
//                        .eq(WeMomentsEstimateCustomer::getMomentsTaskId,weMomentsTask.getId())
//                .in(WeMomentsEstimateCustomer::getWeUserId,sysUser.getWeUserId()));

        //获取成员群发执行结果
//        WeMomentsSyncGroupSendMqRequest mqRequest = new WeMomentsSyncGroupSendMqRequest();
//        mqRequest.setWeMomentsTaskId(request.getWeMomentsTaskId());
//        mqRequest.setSendTime(request.getSendTime());
//        mqRequest.setUser(sysUser);
//        mqRequest.setNum(1);
        //延迟1分钟执行
//        long intervalTime = 60 * 1000;
//        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayGetGroupSendResultRK(), JSONObject.toJSONString(mqRequest), message -> {
//            //注意这里时间可使用long类型,毫秒单位，设置header
//            message.getMessageProperties().setHeader("x-delay", intervalTime);
//            return message;
//        });
    }

    @Override
    public void getGroupSendExecuteResult(WeMomentsSyncGroupSendMqRequest request) {
        MomentsListDetailParamDto query = new MomentsListDetailParamDto();
        query.setCreator(request.getUser().getWeUserId());
        //个人发表
        query.setFilter_type(1);
        //游标
        query.setCursor(null);
        //开始时间
        DateTime date = DateUtil.date(request.getSendTime());
        //提前3秒
        DateTime advanceTime = DateUtil.offset(date, DateField.SECOND, -3);
        query.setStart_time(advanceTime.getTime() / 1000);
        //结束时间往后偏移30秒
        DateTime putOffTime = DateUtil.offset(date, DateField.SECOND, 30);
        query.setEnd_time(putOffTime.getTime() / 1000);
        query.setLimit(1);
        AjaxResult<MomentsListDetailResultDto> result = qwMomentsClient.momentList(query);

        if (result.getCode() == HttpStatus.SUCCESS) {
            MomentsListDetailResultDto data = result.getData();
            List<MomentsListDetailResultDto.Moment> moment_list = data.getMoment_list();
            if (CollectionUtil.isEmpty(moment_list)) {
                if (request.getNum() <= 3) {
                    //最多尝试三次，获取不到数据就放弃
                    request.setNum(request.getNum() + 1);
                    //延迟2分钟执行
                    long intervalTime = 2 * 60 * 1000;
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayGetGroupSendResultRK(), JSONObject.toJSONString(request), message -> {
                        //注意这里时间可使用long类型,毫秒单位，设置header
                        message.getMessageProperties().setHeader("x-delay", intervalTime);
                        return message;
                    });
                }
                return;
            }

            //获取结果
            Optional<MomentsListDetailResultDto.Moment> first = moment_list.stream().findFirst();
            first.ifPresent(i -> {
                //1.添加朋友圈任务和朋友圈绑定表
                weMomentsTaskRelationService.syncAddRelation(request.getWeMomentsTaskId(), i.getMoment_id());
                //2.同步朋友圈发送情况
                weMomentsUserService.syncAddMomentsUser(request.getWeMomentsTaskId(), i, request.getUser());
                //3.同步朋友圈的客户情况
                weMomentsCustomerService.syncAddMomentsCustomer(request.getWeMomentsTaskId(), i.getMoment_id());
                //4.同步员工发送成功的数据
                weMomentsCustomerService.syncMomentsCustomerSendSuccess(request.getWeMomentsTaskId(), i.getMoment_id());
                //5.同步互动数据
                weMomentsInteracteService.syncAddWeMomentsInteracte(request.getWeMomentsTaskId(), i.getMoment_id());
            });
        }
    }

    @Override
    public void jobIdToMomentsId(List<WeMomentsTaskRelation> relations) {


        if(CollectionUtil.isNotEmpty(relations)){
            relations.stream().forEach(k->{
                AjaxResult<MomentsCreateResultDto> momentTaskResult = qwMomentsClient.getMomentTaskResult(k.getJobId());
                        if (momentTaskResult.getCode() == HttpStatus.SUCCESS) {
                            MomentsCreateResultDto data = momentTaskResult.getData();
                            if (data.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
                                MomentsCreateResultDto.Result result = data.getResult();
                                if(result != null){
                                   k.setMomentId(result.getMomentId());
                                }
                            }
                        }

            });

            weMomentsTaskRelationService.updateBatchById(relations);
        }




    }

    @Override
    public void getMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, MomentsListDetailParamDto query) {
        query.setCursor(nextCursor);
        MomentsListDetailResultDto result = qwMomentsClient.momentList(query).getData();
        if (null != result) {
            if (WeConstans.WE_SUCCESS_CODE.equals(result.getErrCode()) || WeConstans.NOT_EXIST_CONTACT.equals(result.getErrCode()) && CollectionUtil.isNotEmpty(result.getMoment_list())) {
                list.addAll(result.getMoment_list());

                if (StringUtils.isNotEmpty(result.getNext_cursor())) {
                    getMoment(result.getNext_cursor(), list, query);
                }
            }
        }
    }

    /**
     * 企微同步，保存朋友圈
     *
     * @param list 企微朋友圈
     * @author WangYX
     * @date 2023/06/12 17:15
     */
    public void syncAndSave(List<MomentsListDetailResultDto.Moment> list) {
        list.forEach(this::syncAndSave);
    }

    /**
     * 企微同步，保存朋友圈
     *
     * @param moment 企微朋友圈
     * @author WangYX
     * @date 2023/06/13 14:49
     */
    private void syncAndSave(MomentsListDetailResultDto.Moment moment) {
        //朋友圈Id
        String moment_id = moment.getMoment_id();
        LambdaQueryWrapper<WeMomentsTaskRelation> queryWrapper = Wrappers.lambdaQuery(WeMomentsTaskRelation.class);
        queryWrapper.eq(WeMomentsTaskRelation::getMomentId, moment_id);

        List<WeMomentsTaskRelation> weMomentsTaskRelations = weMomentsTaskRelationService.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(weMomentsTaskRelations)){
            WeMomentsTaskRelation one=weMomentsTaskRelations.stream().findFirst().get();
            if (BeanUtil.isNotEmpty(one)) {
                //同步过，更新统计的情况
                //朋友圈Id存在
                Long momentTaskId = one.getMomentTaskId();


                //1.同步朋友圈发送情况
                weMomentsUserService.syncUpdateMomentsUser(momentTaskId, moment_id);
                //3.同步朋友圈附件
                weMomentsAttachmentsService.syncAddMomentsAttachments(momentTaskId, moment);
                //2.同步员工发送成功的数据
                weMomentsCustomerService.syncMomentsCustomerSendSuccess(momentTaskId, moment_id);
                //3.同步互动数据
                weMomentsInteracteService.syncUpdateWeMomentsInteract(momentTaskId, moment_id);
            } else {
                //没有同步过
                Integer create_type = moment.getCreate_type();
                String creator = moment.getCreator();
                if (StrUtil.isBlank(creator)) {
                    //通过API接口创建的企微群发朋友圈是没有creator
                    //通过企微软件创建的朋友圈，无论是企业还是个人都存在creator
                    //这里判断的目的是防止上个版本的数据，以及从系统创建的数据，因为该版本通过api创建的朋友圈，都会通过JobId来换取数据,不需要从这里同步
                    return;
                }
                SysUser sysUser = null;
                AjaxResult<SysUser> info = qwSysUserClient.getInfo(creator);
                if (info.getCode() == HttpStatus.SUCCESS) {
                    sysUser = info.getData();
                }
                if (BeanUtil.isEmpty(sysUser)) {
                    //创建者不存在，跳过
                    return;
                }
                //朋友圈创建来源 0：企业 1：个人
                if (create_type.equals(0)) {
                    //企微群发

                    //创建者存在，说明朋友圈从企微创建，又因为系统中不存在该数据，则直接新增
                    syncAddMomentsRelation(moment, sysUser);
                } else {
                    //个人发表
                    LambdaQueryWrapper<WeMomentsTask> wrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
                    wrapper.in(WeMomentsTask::getSendType, ListUtil.toList(1, 2));
                    wrapper.eq(WeMomentsTask::getScopeType, moment.getVisible_type().equals(1) ? 0 : 1);
                    wrapper.eq(WeMomentsTask::getType, moment.getCreate_type());
                    wrapper.eq(WeMomentsTask::getStatus, 3);
                    wrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
                    //通过企微API发送的数据都没有创建者,通过企微软件创建的数据都存在创建者
                    wrapper.eq(StrUtil.isNotBlank(sysUser.getUserName()), WeMomentsTask::getCreateBy, sysUser.getUserName());

                    //保持误差在一分钟以内
                    Long create_time = moment.getCreate_time();
                    DateTime date = DateUtil.date(create_time * 1000);
                    DateTime offset = DateUtil.offset(date, DateField.SECOND, -30);
                    DateTime offset1 = DateUtil.offset(date, DateField.SECOND, 30);
                    wrapper.between(WeMomentsTask::getCreateTime, offset, offset1);
                    wrapper.orderByDesc(WeMomentsTask::getCreateTime);
                    List<WeMomentsTask> weMomentsTasks = weMomentsTaskMapper.selectList(wrapper);
                    if (CollectionUtil.isNotEmpty(weMomentsTasks)) {
                        //存在，不处理
                    } else {
                        //不存在，新增
                        //1.新增朋友圈
                        syncAddMomentsRelation(moment, sysUser);
                    }
                }
            }
        }

    }

    /**
     * 同步新增朋友圈相关数据
     *
     * @param moment  企微朋友圈
     * @param sysUser 员工数据
     * @author WangYX
     * @date 2023/06/21 15:26
     */
    private void syncAddMomentsRelation(MomentsListDetailResultDto.Moment moment, SysUser sysUser) {
        //1.新增朋友圈
        Long weMomentsTaskId = this.syncAddMomentsTask(moment, sysUser);
        //2.添加朋友圈摊任务和朋友圈绑定表
        weMomentsTaskRelationService.syncAddRelation(weMomentsTaskId, moment.getMoment_id());
        //3.同步朋友圈附件
        weMomentsAttachmentsService.syncAddMomentsAttachments(weMomentsTaskId, moment);
        //4.同步朋友圈发送情况
        weMomentsUserService.syncAddMomentsUser(weMomentsTaskId, moment, sysUser);
        //5.同步朋友圈的客户情况
        weMomentsCustomerService.syncAddMomentsCustomer(weMomentsTaskId, moment.getMoment_id());
        //6.同步员工发送成功的数据
        weMomentsCustomerService.syncMomentsCustomerSendSuccess(weMomentsTaskId, moment.getMoment_id());
        //6.同步互动情况
        weMomentsInteracteService.syncAddWeMomentsInteracte(weMomentsTaskId, moment.getMoment_id());
    }


    /**
     * 企微同步时，保存朋友圈数据
     *
     * @param moment  企微朋友圈
     * @param sysUser 创建者
     * @return {@link Long}
     * @author WangYX
     * @date 2023/06/12 13:56
     */
    private Long syncAddMomentsTask(MomentsListDetailResultDto.Moment moment, SysUser sysUser) {
        WeMomentsTask task = new WeMomentsTask();
        task.setId(IdUtil.getSnowflake().nextId());
        task.setEstablishTime(new Date(moment.getCreate_time() * 1000));
        task.setExecuteTime(DateUtil.date(moment.getCreate_time() * 1000).toLocalDateTime());
        if (BeanUtil.isNotEmpty(sysUser)) {
            task.setCreateBy(sysUser.getUserName());
            task.setCreateById(sysUser.getUserId());
        }
        task.setDelFlag(Constants.COMMON_STATE);
        task.setType(moment.getCreate_type());
        task.setSendType(moment.getCreate_type());
        task.setIsLwPush(0);
        Integer visible_type = moment.getVisible_type();
        task.setScopeType(visible_type.equals(0) ? 1 : 0);
        task.setContent(moment.getText().getContent());
        if (task.getSendType().equals(0)) {
            task.setStatus(2);
        } else {
            task.setStatus(3);
        }
        this.save(task);
        return task.getId();
    }


    @Override
    public void syncMomentsDataHandle(List<MomentsListDetailResultDto.Moment> moments) {
        this.syncAndSave(moments);
    }

    @Override
    public void reminderExecution(Long weMomentsTaskId) {
        WeMomentsTask weMomentsTask = this.getById(weMomentsTaskId);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            //为null不处理
            return;
        }
        //企微群发，没有提醒执行操作
        if (weMomentsTask.getSendType().equals(0)) {
            return;
        }

        LambdaUpdateWrapper<WeMomentsEstimateUser> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(WeMomentsEstimateUser::getMomentsTaskId, weMomentsTaskId);
        wrapper.eq(WeMomentsEstimateUser::getExecuteStatus, 0);
        List<WeMomentsEstimateUser> list = weMomentsEstimateUserService.list(wrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            //企微基础配置
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            if (BeanUtil.isEmpty(weCorpAccount)) {
                throw new ServiceException("企微基础数据未配置！");
            }
            List<List<WeMomentsEstimateUser>> split = CollectionUtil.split(list, 1000);
            for (List<WeMomentsEstimateUser> weMomentsEstimateUsers : split) {
                //未发送员工的企微id
                List<String> weUserIds = weMomentsEstimateUsers.stream().map(WeMomentsEstimateUser::getWeUserId).collect(Collectors.toList());
                QwAppMsgBody body = new QwAppMsgBody();
                body.setCorpId(weCorpAccount.getCorpId());
                body.setCorpUserIds(weUserIds);
                //设置消息模板
                WeMessageTemplate template = new WeMessageTemplate();
                //设置消息内型
                template.setMsgType("text");
                //设置应用id
                template.setAppId(weCorpAccount.getAgentId());

                String url = StringUtils.format(linkWeChatConfig.getMomentsUrl(), weMomentsTask.getId());

                //应用消息内容
                String content;
                if (StrUtil.isNotBlank(weMomentsTask.getName())) {
                    content = "【朋友圈营销】<br/> 你有一条【" + weMomentsTask.getName() + "】的朋友圈营销任务还未执行，请尽快执行 <br/><br/>" +
                            "<a href='" + url + "'>去处理</a>";
                } else {
                    content = "【朋友圈营销】<br/> 你有一条朋友圈营销任务还未执行，请尽快执行 <br/><br/>" +
                            "<a href='" + url + "'>去处理</a>";
                }
                template.setContent(content);
                body.setMessageTemplates(template);
                //发送
                qwAppSendMsgService.appMsgSend(body);

                //修改提醒执行次数
                weMomentsEstimateUsers.forEach(i -> i.setExecuteCount(i.getExecuteCount() + 1));
                weMomentsEstimateUserService.updateBatchById(weMomentsEstimateUsers);
            }
        }
    }


    /**
     * 同步指定员工个人朋友圈互动情况
     *
     * @param userIds
     */
    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_MOMENTS_INTERACTE)
    public void syncMomentsInteract(List<String> userIds) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setWeUserIds(userIds);
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeHdMomentsRk(), JSONObject.toJSONString(loginUser));
    }


    /**
     * 同步朋友圈个人互动数据
     *
     * @param msg
     * @author WangYX
     * @date 2023/06/21 16:26
     */
    @Override
    @Async
    public void syncMomentsInteractHandler(String msg) {

        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        List<String> weUserIds = loginUser.getWeUserIds();

        if (CollectionUtil.isNotEmpty(weUserIds)) {
            LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
            queryWrapper.in(WeMomentsUser::getWeUserId, weUserIds);
            queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
            List<WeMomentsUser> list = weMomentsUserService.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                for (WeMomentsUser weMomentsUser : list) {
                    weMomentsInteracteService.syncAddWeMomentsInteracte(weMomentsUser.getMomentsTaskId(), weMomentsUser.getMomentsId());
                }
            }
        }
    }
}