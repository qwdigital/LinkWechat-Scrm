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
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.CategoryMediaType;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.enums.moments.task.WeMomentsTaskSendTypEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.moments.entity.*;
import com.linkwechat.domain.moments.query.WeMomentsJobIdToMomentsIdRequest;
import com.linkwechat.domain.moments.query.WeMomentsSyncGroupSendRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskAddRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskListRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsTaskMapper;
import com.linkwechat.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private IWeMomentsCustomerService weMomentsCustomerService;
    @Resource
    private IWeMomentsInteracteService weMomentsInteracteService;
    @Resource
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;


    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;


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
        //校验任务名称是否重复
        LambdaQueryWrapper<WeMomentsTask> queryWrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
        queryWrapper.eq(WeMomentsTask::getName, request.getName());
        queryWrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsTask> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new ServiceException("任务名称重复！", HttpStatus.BAD_REQUEST);
        }

        //新增
        WeMomentsTask task = BeanUtil.copyProperties(request, WeMomentsTask.class);
        task.setId(IdUtil.getSnowflake().nextId());
        task.setIsLwPush(1);
        task.setCreateById(SecurityUtils.getLoginUser().getSysUser().getUserId());
        task.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserName());
        task.setCreateTime(new Date());
        task.setDelFlag(Constants.COMMON_STATE);

        //朋友圈类型
        if (request.getSendType().equals(WeMomentsTaskSendTypEnum.ENTERPRISE_GROUP_SEND.getCode())) {
            //企业动态
            task.setType(0);
        } else {
            //个人动态
            task.setType(1);
        }

        //发送范围为按条件筛选
        if (request.getScopeType().equals(1)) {
            //TODO 需进一步完善
            //按条件筛选
            if (CollectionUtil.isNotEmpty(request.getDeptIds())) {
                task.setDeptIds(JSONObject.toJSONString(request.getDeptIds()));
            }
            if (CollectionUtil.isNotEmpty(request.getPosts())) {
                task.setPostIds(JSONObject.toJSONString(request.getPosts()));
            }
            if (CollectionUtil.isNotEmpty(request.getUserIds())) {
                task.setUserIds(JSONObject.toJSONString(request.getUserIds()));
            }
            //客户标签
            if (CollectionUtil.isNotEmpty(request.getCustomerTag())) {
                task.setCustomerTag(JSONObject.toJSONString(request.getCustomerTag()));
            }
        }

        if (BeanUtil.isNotEmpty(request.getExecuteTime())) {
            task.setStatus(1);
        } else {
            task.setStatus(2);
        }

        if (BeanUtil.isNotEmpty(request.getExecuteTime())) {
            assert request.getExecuteEndTime().isAfter(request.getExecuteTime());
        } else {
            assert request.getExecuteEndTime().isAfter(LocalDateTime.now());
        }

        //点赞标签
        if (CollectionUtil.isNotEmpty(request.getLikeTagIds())) {
            task.setLikeTagIds(JSONObject.toJSONString(request.getLikeTagIds()));
        }
        //评论标签
        if (CollectionUtil.isNotEmpty(request.getCommentTagIds())) {
            task.setCommentTagIds(JSONObject.toJSONString(request.getCommentTagIds()));
        }
        this.save(task);

        //添加朋友圈附件
        weMomentsAttachmentsService.addMomentsAttachments(task.getId(), request.getMaterialIds());

        //延迟执行
        if (BeanUtil.isNotEmpty(request.getExecuteTime())) {
            //延迟发送朋友圈
            delaySendMoments(task);
        } else {
            //马上执行
            sendWeMoments(task, request.getMaterialIds());
        }
        //取消发送朋友圈
        delayCancelMoments(task);
        return task.getId();
    }

    /**
     * 取消发送朋友圈
     *
     * @param task 朋友圈任务
     * @author WangYX
     * @date 2023/06/09 13:53
     */
    private void delayCancelMoments(WeMomentsTask task) {
        //执行结束时间，添加结束操作
        long intervalTime = DateUtil.betweenMs(DateUtil.date(), DateUtil.date(task.getExecuteEndTime()));
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayExecuteRk(), task.getId().toString(), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", intervalTime);
            return message;
        });
    }

    /**
     * 延迟发送朋友圈
     *
     * @param task 朋友圈任务
     * @author WangYX
     * @date 2023/06/09 13:51
     */
    private void delaySendMoments(WeMomentsTask task) {
        long intervalTime = DateUtil.betweenMs(DateUtil.date(), DateUtil.date(task.getExecuteTime()));
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
        //TODO 需完善
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getCustomerTag())) {
            weMomentsTaskVO.setCustomerTag(JSONObject.parseArray(weMomentsTask.getCustomerTag(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getDeptIds())) {
            weMomentsTaskVO.setDeptIds(JSONObject.parseArray(weMomentsTask.getDeptIds(), Long.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTask.getPostIds())) {
            weMomentsTaskVO.setPosts(JSONObject.parseArray(weMomentsTask.getPostIds(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getUserIds())) {
            weMomentsTaskVO.setUserIds(JSONObject.parseArray(weMomentsTask.getUserIds(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getLikeTagIds())) {
            weMomentsTaskVO.setLikeTagIds(JSONObject.parseArray(weMomentsTask.getLikeTagIds(), String.class));
        }
        if (BeanUtil.isNotEmpty(weMomentsTaskVO.getCommentTagIds())) {
            weMomentsTaskVO.setCommentTagIds(JSONObject.parseArray(weMomentsTask.getCommentTagIds(), String.class));
        }

        LambdaQueryWrapper<WeMomentsAttachments> wrapper = Wrappers.lambdaQuery(WeMomentsAttachments.class);
        wrapper.eq(WeMomentsAttachments::getMomentsTaskId, weMomentsTaskId);
        List<WeMomentsAttachments> list = weMomentsAttachmentsService.list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            //是否在lw平台发布的:1:是;0:否;
            if (weMomentsTaskVO.getIsLwPush().equals(1)) {
                List<Long> materialList = list.stream().map(WeMomentsAttachments::getMaterialId).collect(Collectors.toList());
                List<WeMaterial> weMaterials = weMaterialService.listByIds(materialList);
                weMomentsTaskVO.setMaterialList(weMaterials);
            }
            if (weMomentsTaskVO.getIsLwPush().equals(0)) {
                List<WeMaterial> weMaterials = new ArrayList<>();
                for (WeMomentsAttachments weMomentsAttachments : list) {
                    if (weMomentsAttachments.getMsgType().equals(0)) {
                        //图片
                        WeMaterial weMaterial = new WeMaterial();
                        weMaterial.setMaterialUrl(weMomentsAttachments.getMediaIdUrl());
                        weMaterial.setMediaType(CategoryMediaType.IMAGE.getType().toString());
                        weMaterials.add(weMaterial);
                    }
                    if (weMomentsAttachments.getMsgType().equals(1)) {
                        //视频
                        WeMaterial weMaterial = new WeMaterial();
                        weMaterial.setMaterialUrl(weMomentsAttachments.getMediaIdUrl());
                        weMaterial.setMediaType(CategoryMediaType.VIDEO.getType().toString());
                        weMaterial.setCoverUrl(weMomentsAttachments.getThumbMediaIdUrl());
                        weMaterials.add(weMaterial);
                    }
                    if (weMomentsAttachments.getMsgType().equals(2)) {
                        //链接
                        WeMaterial weMaterial = new WeMaterial();
                        weMaterial.setMaterialName(weMomentsAttachments.getLinkTitle());
                        weMaterial.setMaterialUrl(weMomentsAttachments.getLinkUrl());
                        weMaterial.setMediaType(CategoryMediaType.LINK.getType().toString());
                        weMaterials.add(weMaterial);
                    }
                }
                weMomentsTaskVO.setMaterialList(weMaterials);
            }
        }
        return weMomentsTaskVO;
    }


    @Override
    public void sendWeMoments(Long weMomentsTaskId) {
        //只有未开始的任务才可以执行
        LambdaQueryWrapper<WeMomentsTask> queryWrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
        queryWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeMomentsTask::getStatus, 1);
        WeMomentsTask weMomentsTask = this.getOne(queryWrapper);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            return;
        }

        //获取发送素材
        LambdaQueryWrapper<WeMomentsAttachments> wrapper = Wrappers.lambdaQuery(WeMomentsAttachments.class);
        wrapper.eq(WeMomentsAttachments::getMomentsTaskId, weMomentsTaskId);
        List<WeMomentsAttachments> list = weMomentsAttachmentsService.list(wrapper);
        List<Long> materialIds = list.stream().map(WeMomentsAttachments::getMaterialId).collect(Collectors.toList());

        //发送朋友圈
        sendWeMoments(weMomentsTask, materialIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSendMoments(Long weMomentsTaskId) {
        WeMomentsTask weMomentsTask = this.getById(weMomentsTaskId);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            //数据不存在，不处理
            return;
        }
        if (weMomentsTask.getStatus().equals(3)) {
            //任务已结束不处理
            return;
        }
        if (weMomentsTask.getStatus().equals(1)) {
            //任务未开始,直接设置为任务状态为已结束
            LambdaUpdateWrapper<WeMomentsTask> updateWrapper = Wrappers.lambdaUpdate(WeMomentsTask.class);
            updateWrapper.set(WeMomentsTask::getStatus, 3);
            updateWrapper.eq(WeMomentsTask::getId, weMomentsTaskId);
            this.update(updateWrapper);
            return;
        }

        //朋友圈任务状态为进行中
        if (weMomentsTask.getStatus().equals(2)) {
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
    private void sendWeMoments(WeMomentsTask weMomentsTask, List<Long> materialIds) {
        //1.客户标签
        List<String> customerTagIds = JSONObject.parseArray(weMomentsTask.getCustomerTag(), String.class);

        //2.员工
        List<String> weUserIds = new ArrayList<>();
        if (weMomentsTask.getScopeType().equals(1)) {
            if ((BeanUtil.isEmpty(weMomentsTask.getDeptIds()) && BeanUtil.isEmpty(weMomentsTask.getPostIds()) && BeanUtil.isEmpty(weMomentsTask.getUserIds()))
                    && CollectionUtil.isEmpty(customerTagIds)) {
                throw new ServiceException("成员来源和客户标签必须二选一！");
            }
            if (BeanUtil.isNotEmpty(weMomentsTask.getDeptIds()) || BeanUtil.isNotEmpty(weMomentsTask.getPostIds()) || BeanUtil.isNotEmpty(weMomentsTask.getUserIds())) {
                List<Long> deptIdList = JSONObject.parseArray(weMomentsTask.getDeptIds(), Long.class);
                List<String> postList = JSONObject.parseArray(weMomentsTask.getPostIds(), String.class);
                List<String> weUserIdList = JSONObject.parseArray(weMomentsTask.getUserIds(), String.class);
                List<SysUser> momentsTaskExecuteUser = weMomentsUserService.getMomentsTaskExecuteUser(weMomentsTask.getScopeType(), deptIdList, postList, weUserIdList);
                if (BeanUtil.isNotEmpty(momentsTaskExecuteUser)) {
                    weUserIds = momentsTaskExecuteUser.stream().map(SysUser::getWeUserId).collect(Collectors.toList());
                }
            }
            //标签不为空，通过标签在筛选一次
            if (CollectionUtil.isNotEmpty(customerTagIds)) {
                weUserIds = weFlowerCustomerTagRelService.getCountByTagIdAndUserId(weUserIds, customerTagIds);
                weUserIds = weUserIds.stream().distinct().collect(Collectors.toList());
            }
        }

        //3.企微群发
        if (weMomentsTask.getSendType().equals(0)) {

            MomentsParamDto dto = this.buildMomentsParam(weMomentsTask.getContent(), materialIds, weMomentsTask.getScopeType(), customerTagIds, weUserIds);
            AjaxResult<MomentsResultDto> result = qwMomentsClient.addMomentTask(dto);
            if (result.getCode() == HttpStatus.SUCCESS) {
                MomentsResultDto data = result.getData();
                String jobId = data.getJobid();

                //3.1 保存job和momentId的关联表
                DateTime jobIdExpireTime = DateUtil.offsetDay(DateUtil.date(), 1);
                WeMomentsTaskRelation build = WeMomentsTaskRelation.builder().momentTaskId(weMomentsTask.getId()).jobId(jobId).jobIdExpire(DateUtil.toLocalDateTime(jobIdExpireTime)).build();
                weMomentsTaskRelationService.save(build);

                //3.2 jobId换取momentsId，延迟2分钟执行
                Long intervalTime = 2 * 60 * 1000L;
                WeMomentsJobIdToMomentsIdRequest request = WeMomentsJobIdToMomentsIdRequest.builder().jobId(jobId).num(1).build();
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayJobIdToMomentsIdRK(), JSONObject.toJSONString(request), message -> {
                    //注意这里时间可使用long类型,毫秒单位，设置header
                    message.getMessageProperties().setHeader("x-delay", intervalTime);
                    return message;
                });
            }
        } else {
            //4.成员群发,需要员工在移动端，调用sdk来发送朋友圈
            //通过应用消息下发提醒
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            if (BeanUtil.isEmpty(weCorpAccount)) {
                throw new ServiceException("企微基础数据未配置！");
            }
            List<List<String>> split = CollectionUtil.split(weUserIds, 1000);
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

                String url = String.format(linkWeChatConfig.getMomentsUrl(), weMomentsTask.getId());
                String content = "【朋友圈营销】<br/> 管理员下发一条【" + weMomentsTask.getName() + "】的朋友圈营销任务，请及时执行 <br/><br/>" +
                        "<a href='" + url + "'>去处理</a>";

                template.setContent(content);
                body.setMessageTemplates(template);
                qwAppSendMsgService.appMsgSend(body);
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
     * @param customerTags 客户标签
     * @param weUserIds    发送员工
     * @return {@link MomentsParamDto}
     * @author WangYX
     * @date 2023/06/08 16:05
     */
    private MomentsParamDto buildMomentsParam(String content, List<Long> materialIds, Integer scopeType, List<String> customerTags, List<String> weUserIds) {
        MomentsParamDto.MomentsParamDtoBuilder builder = MomentsParamDto.builder();
        //文本内容
        if (StrUtil.isNotBlank(content)) {
            builder.text(MomentsParamDto.Text.builder().content(content).build());
        }
        //附件
        if (CollectionUtil.isNotEmpty(materialIds)) {
            List<Object> attachments = new ArrayList<>();
            List<WeMaterial> list = weMaterialService.listByIds(materialIds);
            for (WeMaterial weMaterial : list) {
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
                    String coverUrl = weMaterial.getCoverUrl();
                    if (BeanUtil.isEmpty(coverUrl)) {
                        coverUrl = "https://demo.linkwechat.net/static/TEXT_PIC.png";
                    }
                    String media_id = weMaterialService.uploadAttachmentMaterial(coverUrl, MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
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
                    if (BeanUtil.isEmpty(weMaterial.getCoverUrl())) {
                        weMaterial.setCoverUrl("https://demo.linkwechat.net/static/TEXT_PIC.png");
                    }
                    convertWeChatMaterial(attachments, weMaterial);
                }

                //文件
                if (weMaterial.getMediaType().equals(CategoryMediaType.FILE.getType().toString())) {
                    int indexOf = weMaterial.getMaterialUrl().lastIndexOf(".");
                    String substring = weMaterial.getMaterialUrl().substring(indexOf);
                    if (substring.equals("ppt")) {
                        //ppt
                        weMaterial.setCoverUrl("https://demo.linkwechat.net/static/PPT.png");
                    } else if (substring.equals("word")) {
                        //word
                        weMaterial.setCoverUrl("https://demo.linkwechat.net/static/WORD.png");
                    } else {
                        //pdf
                        weMaterial.setCoverUrl("https://demo.linkwechat.net/static/PDF.png");
                    }
                    convertWeChatMaterial(attachments, weMaterial);
                }
            }
            builder.attachments(attachments);
        }

        MomentsParamDto.VisibleRange visibleRange = MomentsParamDto.VisibleRange.builder().build();

        //设置可见范围
        //部分客户
        if (scopeType.equals(1)) {
            //部分客户
            if (CollectionUtil.isNotEmpty(customerTags)) {
                visibleRange.setExternal_contact_list(MomentsParamDto.ExternalContactList.builder().tag_list(customerTags.toArray(new String[0])).build());
            }
            //指定发送人
            if (CollectionUtil.isNotEmpty(weUserIds)) {
                visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weUserIds.toArray(new String[0])).build());
            }
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
     * @return
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
            String url = String.format(materialDetailUrl, weMaterial.getId(), 1);
            build.setUrl(url);
            build.setMedia_id(media_id);
            build.setTitle(truncationStr(weMaterial.getMaterialName(), 64));
            attachments.add(MomentsParamDto.LinkAttachments.builder().msgtype(MediaType.LINK.getMediaType()).link(build).build());
        }
    }


    private MomentsParamDto buildMomentsParam(WeMomentsTaskAddRequest request) {
        List<SysUser> sysUser = this.getSysUser(request);
        List<String> weUserIds = sysUser.stream().map(SysUser::getWeUserId).collect(Collectors.toList());
        return this.buildMomentsParam(request.getContent(), request.getMaterialIds(), request.getScopeType(), request.getCustomerTag(), weUserIds);
    }


    @Override
    public void syncMoments(Integer filterType) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setFilterType(filterType);
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeMomentsRk(), JSONObject.toJSONString(loginUser));
    }

    @Override
    public void syncWeMomentsHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
        SecurityContextHolder.setUserType(loginUser.getUserType());

        Integer filterType = loginUser.getFilterType();

        Long startTime = DateUtil.beginOfDay(new Date()).getTime() / 1000;
        Long endTime = DateUtil.date().getTime() / 1000;
        MomentsListDetailParamDto query = MomentsListDetailParamDto.builder().start_time(startTime).end_time(endTime).filter_type(filterType).build();

        List<MomentsListDetailResultDto.Moment> moments = new ArrayList<>();
        getByMoment(null, moments, query);

        if (CollectionUtil.isNotEmpty(moments)) {
            syncAndSave(moments);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void groupSendFinish(WeMomentsSyncGroupSendRequest request) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        //判断是否已经执行过
        LambdaQueryWrapper<WeMomentsUser> wrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        wrapper.eq(WeMomentsUser::getMomentsTaskId, request.getWeMomentsTaskId());
        wrapper.eq(WeMomentsUser::getUserId, sysUser.getWeUserId());
        WeMomentsUser one = weMomentsUserService.getOne(wrapper);
        if (BeanUtil.isNotEmpty(one)) {
            //执行过之后，无需在执行
            if (one.getExecuteStatus().equals(1)) {
                return;
            }
        }

        MomentsListDetailParamDto query = new MomentsListDetailParamDto();
        query.setCreator(sysUser.getWeUserId());
        //个人发表
        query.setFilter_type(1);
        //游标
        query.setCursor(null);
        //开始时间
        long startTime = Timestamp.valueOf(request.getSendTime()).getTime() / 1000;
        query.setStart_time(startTime);
        //结束时间往后偏移30秒
        DateTime offset = DateUtil.offset(DateUtil.date(request.getSendTime()), DateField.SECOND, 30);
        query.setEnd_time(offset.getTime() / 1000);
        query.setLimit(20);
        AjaxResult<MomentsListDetailResultDto> result = qwMomentsClient.momentList(query);

        if (result.getCode() == HttpStatus.SUCCESS) {
            MomentsListDetailResultDto data = result.getData();
            List<MomentsListDetailResultDto.Moment> moment_list = data.getMoment_list();
            if (CollectionUtil.isNotEmpty(moment_list)) {
                Optional<MomentsListDetailResultDto.Moment> first = moment_list.stream().findFirst();
                first.ifPresent(i -> {
                    //1.添加朋友圈任务和朋友圈绑定表
                    weMomentsTaskRelationService.syncAddRelation(request.getWeMomentsTaskId(), i.getMoment_id());
                    //2.同步朋友圈发送情况
                    weMomentsUserService.syncAddMomentsUser(request.getWeMomentsTaskId(), i.getMoment_id());
                    //3.同步朋友圈的客户情况
                    weMomentsCustomerService.syncAddMomentsCustomer(request.getWeMomentsTaskId(), i.getMoment_id());
                    //4.同步员工发送成功的数据
                    weMomentsCustomerService.syncMomentsCustomerSendSuccess(request.getWeMomentsTaskId(), i.getMoment_id());
                });
            }
        }
    }

    @Override
    public void jobIdToMomentsId(WeMomentsJobIdToMomentsIdRequest request) {
        LambdaQueryWrapper<WeMomentsTaskRelation> queryWrapper = Wrappers.lambdaQuery(WeMomentsTaskRelation.class);
        queryWrapper.eq(WeMomentsTaskRelation::getJobId, request.getJobId());
        WeMomentsTaskRelation one = weMomentsTaskRelationService.getOne(queryWrapper);
        if (BeanUtil.isEmpty(one)) {
            //数据不存在，不执行
            return;
        }
        if (StrUtil.isNotEmpty(one.getMomentId())) {
            //朋友圈id已经存在，说明已经同步过了，不继续执行。
            return;
        }

        AjaxResult<MomentsCreateResultDto> momentTaskResult = qwMomentsClient.getMomentTaskResult(request.getJobId());
        if (momentTaskResult.getCode() == HttpStatus.SUCCESS) {
            MomentsCreateResultDto data = momentTaskResult.getData();
            if (!data.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())) {
                //企微接口返回数据存在异常，不继续执行
                return;
            }
            //任务状态，整型，1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成
            if (data.getStatus().equals(3)) {
                MomentsCreateResultDto.Result result = data.getResult();
                if (result.getErrCode().equals(0)) {
                    String momentId = result.getMomentId();
                    //1.修改朋友圈任务和朋友圈绑定表
                    one.setMomentId(momentId);
                    weMomentsTaskRelationService.updateById(one);
                    //2.同步朋友圈发送情况
                    weMomentsUserService.syncAddMomentsUser(one.getMomentTaskId(), momentId);
                    //3.同步朋友圈的客户情况
                    weMomentsCustomerService.syncAddMomentsCustomer(one.getMomentTaskId(), momentId);
                    //4.同步员工发送成功的数据
                    weMomentsCustomerService.syncMomentsCustomerSendSuccess(one.getMomentTaskId(), momentId);
                }
            } else {
                if (request.getNum() <= 3) {
                    request.setNum(request.getNum() + 1);
                    Long intervalTime = request.getNum() * 5 * 60 * 1000L;
                    request.setNum(2);
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayJobIdToMomentsIdRK(), JSONObject.toJSONString(request), message -> {
                        //注意这里时间可使用long类型,毫秒单位，设置header
                        message.getMessageProperties().setHeader("x-delay", intervalTime);
                        return message;
                    });
                } else if (request.getNum() <= 12) {
                    //间隔1小时执行一次
                    Long intervalTime = 60 * 60 * 1000L;
                    request.setNum(request.getNum() + 1);
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentsDelayJobIdToMomentsIdRK(), JSONObject.toJSONString(request), message -> {
                        //注意这里时间可使用long类型,毫秒单位，设置header
                        message.getMessageProperties().setHeader("x-delay", intervalTime);
                        return message;
                    });
                } else {
                    //TODO 暂不处理
                    //jobId失效了，如何处理
                }
            }
        }
    }

    @Override
    public void getByMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, MomentsListDetailParamDto query) {
        MomentsListDetailResultDto result = qwMomentsClient.momentList(query).getData();
        if (null != result) {
            if (WeConstans.WE_SUCCESS_CODE.equals(result.getErrCode()) || WeConstans.NOT_EXIST_CONTACT.equals(result.getErrCode()) && CollectionUtil.isNotEmpty(result.getMoment_list())) {
                list.addAll(result.getMoment_list());
                if (StringUtils.isNotEmpty(result.getNext_cursor())) {
                    getByMoment(result.getNext_cursor(), list, query);
                }
            }
        }
    }

    /**
     * 企微同步，保存朋友圈
     *
     * @param list
     * @author WangYX
     * @date 2023/06/12 17:15
     */
    public void syncAndSave(List<MomentsListDetailResultDto.Moment> list) {
        //TODO 需要加锁处理
        for (MomentsListDetailResultDto.Moment moment : list) {
            this.syncAndSave(moment);
        }
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
        //1.判断朋友圈是否已经同步
        LambdaQueryWrapper<WeMomentsTaskRelation> queryWrapper = Wrappers.lambdaQuery(WeMomentsTaskRelation.class);
        queryWrapper.eq(WeMomentsTaskRelation::getMomentId, moment_id);
        WeMomentsTaskRelation one = weMomentsTaskRelationService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            //同步过，更新统计的情况
            //朋友圈Id存在
            Long momentTaskId = one.getMomentTaskId();
            //1.同步朋友圈发送情况
            weMomentsUserService.syncUpdateMomentsUser(momentTaskId, moment_id);
            //2.同步员工发送成功的数据
            weMomentsCustomerService.syncMomentsCustomerSendSuccess(momentTaskId, moment_id);
            //3.同步互动数据
            weMomentsInteracteService.syncAddWeMomentsInteracte(momentTaskId, moment_id);
        } else {
            //没有同步过
            Integer create_type = moment.getCreate_type();
            String creator = moment.getCreator();
            if (BeanUtil.isEmpty(creator)) {
                //通过API接口创建的企微群发朋友圈是没有creator
                //通过企微软件创建的朋友圈，无论是企业还是个人都存在creator
                //这里判断的目的是防止上个版本的数据，因为该版本通过api创建的朋友圈，都会通过JobId来换取数据,不需要从这里同步
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
                LambdaQueryWrapper<WeMomentsTask> wrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
                wrapper.eq(WeMomentsTask::getSendType, 0);
                wrapper.eq(WeMomentsTask::getScopeType, moment.getVisible_type().equals(1) ? 0 : 1);
                wrapper.eq(WeMomentsTask::getType, moment.getCreate_type());
                wrapper.eq(StrUtil.isNotBlank(sysUser.getUserName()), WeMomentsTask::getCreateBy, sysUser.getUserName());
                wrapper.eq(WeMomentsTask::getStatus, 2);
                wrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
                wrapper.lt(WeMomentsTask::getCreateTime, moment.getCreate_time() * 1000);
                wrapper.orderByDesc(WeMomentsTask::getCreateTime);
                List<WeMomentsTask> weMomentsTasks = weMomentsTaskMapper.selectList(wrapper);
                if (CollectionUtil.isNotEmpty(weMomentsTasks)) {
                    //存在，不处理，等待JobId去同步
                } else {
                    //不存在就新增
                    //1.新增朋友圈
                    Long weMomentsTaskId = this.syncAddMomentsTask(moment, sysUser);
                    //2.添加朋友圈摊任务和朋友圈绑定表
                    weMomentsTaskRelationService.syncAddRelation(weMomentsTaskId, moment_id);
                    //3.同步朋友圈附件
                    weMomentsAttachmentsService.syncAddMomentsAttachments(weMomentsTaskId, moment);
                    //4.同步朋友圈发送情况
                    weMomentsUserService.syncAddMomentsUser(weMomentsTaskId, moment_id);
                    //5.同步朋友圈的客户情况
                    weMomentsCustomerService.syncAddMomentsCustomer(weMomentsTaskId, moment_id);
                    //6.同步员工发送成功的数据
                    weMomentsCustomerService.syncMomentsCustomerSendSuccess(weMomentsTaskId, moment_id);
                    //6.同步互动情况
                    weMomentsInteracteService.syncAddWeMomentsInteracte(weMomentsTaskId, moment_id);
                }
            } else {
                //个人发表
                LambdaQueryWrapper<WeMomentsTask> wrapper = Wrappers.lambdaQuery(WeMomentsTask.class);
                wrapper.in(WeMomentsTask::getSendType, ListUtil.toList(1, 2));
                wrapper.eq(WeMomentsTask::getScopeType, moment.getVisible_type().equals(1) ? 0 : 1);
                wrapper.eq(WeMomentsTask::getType, moment.getCreate_type());
                wrapper.eq(WeMomentsTask::getStatus, 2);
                wrapper.eq(WeMomentsTask::getDelFlag, Constants.COMMON_STATE);
                wrapper.lt(WeMomentsTask::getCreateTime, moment.getCreate_time());
                wrapper.orderByDesc(WeMomentsTask::getCreateTime);
                List<WeMomentsTask> weMomentsTasks = weMomentsTaskMapper.selectList(wrapper);

                //TODO 待完善


            }
        }
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
        if (BeanUtil.isNotEmpty(sysUser)) {
            task.setCreateBy(sysUser.getUserName());
            task.setCreateById(sysUser.getUserId());
        }
        task.setDelFlag(Constants.COMMON_STATE);

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
        LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        queryWrapper.select(WeMomentsUser::getId, WeMomentsUser::getWeUserId, WeMomentsUser::getExecuteCount);
        queryWrapper.eq(WeMomentsUser::getMomentsTaskId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeMomentsUser::getExecuteStatus, 0);
        List<WeMomentsUser> list = weMomentsUserService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            //企微基础配置
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            if (BeanUtil.isEmpty(weCorpAccount)) {
                throw new ServiceException("企微基础数据未配置！");
            }
            List<List<WeMomentsUser>> split = CollectionUtil.split(list, 1000);
            for (List<WeMomentsUser> weMomentsUsers : split) {
                //未发送员工的企微id
                List<String> weUserIds = weMomentsUsers.stream().map(WeMomentsUser::getWeUserId).collect(Collectors.toList());
                QwAppMsgBody body = new QwAppMsgBody();
                body.setCorpId(weCorpAccount.getCorpId());
                body.setCorpUserIds(weUserIds);
                //设置消息模板
                WeMessageTemplate template = new WeMessageTemplate();
                //设置消息内型
                template.setMsgType("text");
                //设置应用id
                template.setAppId(weCorpAccount.getAgentId());

                String url = String.format(linkWeChatConfig.getMomentsUrl(), weMomentsTask.getId());

                //应用消息内容
                String content = null;
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
                weMomentsUsers.forEach(i -> i.setExecuteCount(i.getExecuteCount() + 1));
                weMomentsUserService.updateBatchById(weMomentsUsers);
            }
        }

    }

    //--------------------------------------------------------------------------------------------------------------//

    /**
     * 朋友圈列表
     *
     * @param weMoments
     * @return
     */
    @Override
    public List<WeMomentsTask> findMoments(WeMomentsTask weMoments) {
//        return this.baseMapper.findMoments(weMoments);
        return null;
    }

    /**
     * 发送更新朋友圈
     *
     * @param weMoments
     */
    @Override
    public void addOrUpdateMoments(WeMomentsTask weMoments) {
//        MomentsParamDto momentsParamDto = new MomentsParamDto();
//        weMoments.setPushTime(new Date());
//        weMoments.setIsLwPush(1);
//        if (StringUtils.isNotEmpty(weMoments.getContent())) {
//            weMoments.getOtherContent().add(WeMoments.OtherContent.builder().annexType(MediaType.TEXT.getMediaType()).other(weMoments.getContent()).build());
//            momentsParamDto.setText(MomentsParamDto.Text.builder().content(weMoments.getContent()).build());
//        }
//        //设置附件
//        List<WeMoments.OtherContent> otherContent = weMoments.getOtherContent();
//        if (CollectionUtil.isNotEmpty(otherContent)) {
//            List<WeMoments.OtherContent> otherContents = otherContent.stream().filter(s -> StringUtils.isNotEmpty(s.getAnnexType()) && StringUtils.isNotEmpty(s.getAnnexUrl())).collect(Collectors.toList());
//            if (CollectionUtil.isNotEmpty(otherContents)) {
//                List<Object> attachments = new ArrayList<>();
//                //图片
//                if (weMoments.getContentType().equals(MediaType.IMAGE.getMediaType())) {
//                    otherContents.forEach(image -> {
//                        String media_id = iWeMaterialService.uploadAttachmentMaterial(image.getAnnexUrl(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
//                        if (StringUtils.isNotEmpty(media_id)) {
//                            attachments.add(MomentsParamDto.ImageAttachments.builder().msgtype(MediaType.IMAGE.getMediaType()).image(MomentsParamDto.Image.builder().media_id(media_id).build()).build());
//                            weMoments.setContent(image.getAnnexUrl());
//                        }
//                    });
//                }
//                //视频
//                if (weMoments.getContentType().equals(MediaType.VIDEO.getMediaType())) {
//                    otherContents.forEach(video -> {
//                        String media_id = iWeMaterialService.uploadAttachmentMaterial(video.getAnnexUrl(), MediaType.VIDEO.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
//                        if (StringUtils.isNotEmpty(media_id)) {
//                            attachments.add(MomentsParamDto.VideoAttachments.builder().msgtype(MediaType.VIDEO.getMediaType()).video(MomentsParamDto.Video.builder().media_id(media_id).build()).build());
//                            weMoments.setContent(video.getAnnexUrl());
//                        }
//                    });
//                }
//                //网页链接
//                if (weMoments.getContentType().equals(MediaType.LINK.getMediaType())) {
//                    otherContents.forEach(link -> {
//                        String media_id = iWeMaterialService.uploadAttachmentMaterial(link.getOther(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
//                        if (StringUtils.isNotEmpty(media_id)) {
//                            MomentsParamDto.Link build = new MomentsParamDto.Link();
//                            build.setUrl(link.getAnnexUrl());
//                            build.setMedia_id(media_id);
//                            if (StringUtils.isNotEmpty(link.getTitle())) {
//                                build.setTitle(link.getTitle());
//                            }
//                            attachments.add(MomentsParamDto.LinkAttachments.builder().msgtype(MediaType.LINK.getMediaType()).link(build).build());
//                            weMoments.setContent(link.getAnnexUrl());
//                        }
//                    });
//                }
//                momentsParamDto.setAttachments(attachments);
//            }
//        }
//        MomentsParamDto.VisibleRange visibleRange = MomentsParamDto.VisibleRange.builder().build();
//
//        //设置可见范围
//        if (weMoments.getScopeType().equals(0)) { //部分
//            if (StringUtils.isNotEmpty(weMoments.getCustomerTag())) { //客户标签
//                visibleRange.setExternal_contact_list(MomentsParamDto.ExternalContactList.builder().tag_list(weMoments.getCustomerTag().split(",")).build());
//            }
//            if (StringUtils.isNotEmpty(weMoments.getNoAddUser())) {//指定发送人
//                visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weMoments.getNoAddUser().split(",")).build());
//            }
//        }
//        momentsParamDto.setVisible_range(visibleRange);
//        MomentsResultDto weResultDto = qwMomentsClient.addMomentTask(momentsParamDto).getData();
//        if (Objects.isNull(weResultDto)) {
//            throw new WeComException("调用企微接口失败");
//        }
//        if (ObjectUtil.notEqual(WeConstans.WE_SUCCESS_CODE, weResultDto.getErrCode())) {
//            throw new WeComException(weResultDto.getErrCode(), weResultDto.getErrMsg());
//        }
//        weMoments.setJobId(weResultDto.getJobid());
//        weMoments.setStatus(1);
//        this.save(weMoments);
    }


    /**
     * 朋友圈详情
     *
     * @param id
     * @return
     */
    @Override
    public WeMomentsTask findMomentsDetail(Long id) {
//        return this.baseMapper.findMomentsDetail(id);
        return null;
    }


    /**
     * 同步朋友圈个人互动情况逻辑
     *
     * @param msg
     */
    @Override
    @Async
    public void synchMomentsInteracteHandler(String msg) {

//        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
//        SecurityContextHolder.setCorpId(loginUser.getCorpId());
//        SecurityContextHolder.setUserName(loginUser.getUserName());
//        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
//        SecurityContextHolder.setUserType(loginUser.getUserType());
//        List<String> weUserIds = loginUser.getWeUserIds();
//
//        Map<String, SysUserVo> userIdMap = new HashMap<>();
//
//        if(CollectionUtil.isNotEmpty(weUserIds)){
//            SysUserQuery userQuery = new SysUserQuery();
//            userQuery.setWeUserIds(weUserIds);
//            List<SysUserVo> userList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
//            if (CollectionUtil.isNotEmpty(userList)) {
//                userIdMap = userList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, Function.identity(), (key1, key2) -> key2));
//            }
//        }
//
//        if (CollectionUtil.isNotEmpty(userIdMap)) {
//            userIdMap.forEach((weUserId, sysUserInfo) ->{
//                List<WeMoments> weMoments = list(new LambdaQueryWrapper<WeMoments>()
//                        .eq(WeMoments::getCreator,weUserId).eq(WeMoments::getType, 1)
//                        .eq(WeMoments::getDelFlag, WeConstans.WE_SUCCESS_CODE));
//                if (CollectionUtil.isNotEmpty(weMoments)) {
//                    List<WeMomentsInteracte> interactes = new ArrayList<>();
//                    weMoments.forEach(moment -> {
//                        weMomentsInteracteService.remove(new LambdaQueryWrapper<WeMomentsInteracte>()
//                                .eq(WeMomentsInteracte::getMomentId, moment.getMomentId()));
//                        interactes.addAll(getInteracte(moment.getMomentId(), moment.getCreator(), sysUserInfo));
//
//
//                    });
//                    if (CollectionUtil.isNotEmpty(interactes)) {
//                        weMomentsInteracteService.saveBatch(interactes);
//
//                        interactes.forEach(interacte -> {
//                            if (new Integer(1).equals(interacte.getInteracteUserType())) {//互动人员为客户
//                                iWeCustomerTrajectoryService.createInteractionTrajectory(
//                                        interacte.getInteracteUserId(), interacte.getMomentCreteOrId(), new Integer(1).equals(interacte.getInteracteType()) ? TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getType() :
//                                                TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getType(), null
//                                );
//                            }
//
//                        });
//
//                    }
//
//
//                }
//            });
//
//        }
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


    //获取互动数据
    private List<WeMomentsInteracte> getInteracte(String momentId, String creator, SysUserVo sysUser) {
        List<WeMomentsInteracte> interactes = new ArrayList<>();

        MomentsInteracteResultDto momentComments = qwMomentsClient.comments(
                MomentsInteracteParamDto.builder().moment_id(momentId).userid(creator).build()).getData();

        if (momentComments.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
            List<MomentsInteracteResultDto.Interacte> comment_list = momentComments.getComment_list();

            //评论
            if (CollectionUtil.isNotEmpty(comment_list)) {
                comment_list.forEach(k -> {
                    WeMomentsInteracte.WeMomentsInteracteBuilder builder = WeMomentsInteracte.builder();
                    builder.interacteUserType(StringUtils.isNotEmpty(k.getUserid()) ? new Integer(0) : new Integer(1));
                    builder.interacteType(0);
                    builder.interacteUserId(
                            StringUtils.isNotEmpty(k.getUserid()) ? k.getUserid() : k.getExternal_userid());
                    builder.interacteTime(new Date(k.getCreate_time() * 1000L));
                    builder.momentId(momentId);
                    WeMomentsInteracte weMomentsInteracte = builder.build();
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
                like_list.forEach(k -> {
                    WeMomentsInteracte weMomentsInteracte = WeMomentsInteracte.builder()
                            .interacteUserType(StringUtils.isNotEmpty(k.getUserid()) ? new Integer(0) : new Integer(1))
                            .interacteType(1).interacteUserId(
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
    private void getSendResult(WeMomentsTask weMoments) {
//        MomentsResultDto momentTask = qwMomentsClient.get_moment_task(
//                MomentsParamDto.builder().moment_id(weMoments.getMomentId()).build()).getData();
//
//        if (null != momentTask) {
//            if (momentTask.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
//                List<MomentsResultDto.TaskList> taskList = momentTask.getTask_list();
//                if (CollectionUtil.isNotEmpty(taskList)) {
//                    Map<Integer, List<MomentsResultDto.TaskList>> listMap = taskList.stream().collect(Collectors.groupingBy(MomentsResultDto.TaskList::getPublish_status));
//
//                    //已发表
//                    if(listMap.containsKey(1)){
//                        weMoments.setAddUser(listMap.get(1).stream().map(MomentsResultDto.TaskList::getUserid)
//                                .collect(Collectors.joining(",")));
//                    }
//
//                    if(listMap.containsKey(0) && ObjectUtil.equal(weMoments.getScopeType(),0)){
//                        weMoments.setNoAddUser(listMap.get(0).stream().map(MomentsResultDto.TaskList::getUserid)
//                                .collect(Collectors.joining(",")));
//                    }else {
//                        weMoments.setNoAddUser("");
//                    }
//                }
//            }
//        }
    }


    /**
     * 批量获取企业朋友圈(30天内数据)
     *
     * @param nextCursor
     * @param list
     */
    private void getByMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, Integer filterType) {
        MomentsListDetailParamDto query = MomentsListDetailParamDto.builder().start_time(DateUtils.getBeforeByDayLongTime(-30)).end_time(DateUtils.getBeforeByDayLongTime(0)).cursor(nextCursor).filter_type(filterType).build();
        getByMoment(nextCursor, list, query);
    }


    /**
     * 互动数据入库处理
     *
     * @param interactes
     */
    private void handleInteracte(List<WeMomentsInteracte> interactes, List<WeMomentsTask> weMoments) {
        if (CollectionUtil.isNotEmpty(interactes)) {
            weMomentsInteracteService.remove(new LambdaQueryWrapper<WeMomentsInteracte>()
                    .in(WeMomentsInteracte::getMomentId, weMoments.stream().map(WeMomentsTask::getId).collect(Collectors.toList())));
            weMomentsInteracteService.saveBatch(interactes);
        }
    }
}