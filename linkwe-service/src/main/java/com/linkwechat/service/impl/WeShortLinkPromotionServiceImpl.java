package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.*;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.moments.dto.CancelMomentTaskDto;
import com.linkwechat.domain.moments.dto.MomentsCreateResultDto;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.*;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.customer.msg.WeCancelGroupMsgSendQuery;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeShortLinkPromotionMapper;
import com.linkwechat.service.*;
import com.linkwechat.service.impl.strategic.shortlink.DefaultPromotion;
import com.linkwechat.service.impl.strategic.shortlink.PromotionType;
import com.linkwechat.service.impl.strategic.shortlink.ShortLinkPromotionStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 短链推广 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@Service
public class WeShortLinkPromotionServiceImpl extends ServiceImpl<WeShortLinkPromotionMapper, WeShortLinkPromotion> implements IWeShortLinkPromotionService {

    @Resource
    private WeShortLinkPromotionMapper weShortLinkPromotionMapper;
    @Resource
    private WeMaterialMapper weMaterialMapper;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;
    @Resource
    private IWeShortLinkService weShortLinkService;
    @Resource
    private IWeShortLinkPromotionTemplateClientService weShortLinkPromotionTemplateClientService;
    @Resource
    private IWeShortLinkPromotionTemplateGroupService weShortLinkPromotionTemplateGroupService;
    @Resource
    private IWeShortLinkPromotionTemplateMomentsService weShortLinkPromotionTemplateMomentsService;
    @Resource
    private IWeShortLinkPromotionTemplateAppMsgService weShortLinkPromotionTemplateAppMsgService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private IWeTagService weTagService;
    @Resource
    private QwCustomerClient qwCustomerClient;
    @Resource
    private QwMomentsClient qwMomentsClient;
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;


    @Override
    public List<WeShortLinkPromotionVo> selectList(WeShortLinkPromotionQuery query) {
        return weShortLinkPromotionMapper.selectList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = new WeShortLinkPromotion();
        weShortLinkPromotion.setTaskName(query.getTaskName());
        weShortLinkPromotion.setShortLinkId(query.getShortLinkId());
        weShortLinkPromotion.setStyle(query.getStyle());
        weShortLinkPromotion.setType(query.getType());
        weShortLinkPromotion.setDelFlag(0);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));

        //保存和发送
        PromotionType promotionType = ShortLinkPromotionStrategyFactory.getPromotionType(query.getType());
        Long id = promotionType.saveAndSend(query, weShortLinkPromotion);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long ts(WeShortLinkPromotionAddQuery query) throws IOException {
        Long id = query.getId();
        WeShortLinkPromotion weShortLinkPromotion = null;
        if (id == null) {
            //id不存在时，属于新增流程暂存
            weShortLinkPromotion = add_ts(query);
        } else {
            //id存在时，属于修改流程暂存，需取消定时任务
            weShortLinkPromotion = edit_ts(query);
        }
        return weShortLinkPromotion.getId();
    }

    /**
     * 新增流程暂存
     *
     * @param query
     * @return
     * @throws IOException
     */
    private WeShortLinkPromotion add_ts(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));
        weShortLinkPromotion.setDelFlag(0);
        weShortLinkPromotion.setTaskStatus(0);
        weShortLinkPromotionMapper.insert(weShortLinkPromotion);

        PromotionType promotionType = SpringUtil.getBean(DefaultPromotion.class);
        WeMessageTemplate weMessageTemplate = promotionType.getPromotionUrl(weShortLinkPromotion.getId(), query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        weShortLinkPromotionMapper.updateById(weShortLinkPromotion);
        return weShortLinkPromotion;
    }

    /**
     * 修改流程暂存，需取消定时任务
     *
     * @param query
     * @return
     */
    private WeShortLinkPromotion edit_ts(WeShortLinkPromotionAddQuery query) throws IOException {

        WeShortLinkPromotion preShortLinkPromotion = weShortLinkPromotionMapper.selectById(query.getId());
        //删除短链推广模板
        Integer type = preShortLinkPromotion.getType();
        switch (type) {
            case 0:
                cancelClient(query.getId());
                break;
            case 1:
                cancelGroup(query.getId());
                break;
            case 2:
                cancelMoments(query.getId());
                break;
            case 3:
                cancelAppMsg(query.getId());
                break;
        }

        WeShortLinkPromotion weShortLinkPromotion = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
        PromotionType promotionType = SpringUtil.getBean(DefaultPromotion.class);
        WeMessageTemplate weMessageTemplate = promotionType.getPromotionUrl(weShortLinkPromotion.getId(), query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        weShortLinkPromotionMapper.updateById(weShortLinkPromotion);
        return weShortLinkPromotion;
    }

    /**
     * 取消短链推广-客户
     *
     * @param promotionId
     */
    private void cancelClient(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateClient> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getDelFlag, 0);
        WeShortLinkPromotionTemplateClient one = weShortLinkPromotionTemplateClientService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            LambdaUpdateWrapper<WeShortLinkPromotionTemplateClient> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.set(WeShortLinkPromotionTemplateClient::getDelFlag, 1);
            updateWrapper.eq(WeShortLinkPromotionTemplateClient::getId, one.getId());
            weShortLinkPromotionTemplateClientService.update(updateWrapper);
        }
    }

    /**
     * 取消短链推广-客群
     *
     * @param promotionId
     */
    private void cancelGroup(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateGroup> queryWrapper = Wrappers.lambdaQuery(WeShortLinkPromotionTemplateGroup.class);
        queryWrapper.eq(WeShortLinkPromotionTemplateGroup::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateGroup::getDelFlag, 0);
        WeShortLinkPromotionTemplateGroup one = weShortLinkPromotionTemplateGroupService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            LambdaUpdateWrapper<WeShortLinkPromotionTemplateGroup> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.set(WeShortLinkPromotionTemplateGroup::getDelFlag, 1);
            updateWrapper.eq(WeShortLinkPromotionTemplateGroup::getId, one.getId());
            weShortLinkPromotionTemplateGroupService.update(updateWrapper);
        }
    }

    /**
     * 取消短链推广-朋友圈
     *
     * @param promotionId
     */
    private void cancelMoments(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateMoments> queryWrapper = Wrappers.lambdaQuery(WeShortLinkPromotionTemplateMoments.class);
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getDelFlag, 0);
        WeShortLinkPromotionTemplateMoments one = weShortLinkPromotionTemplateMomentsService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            LambdaUpdateWrapper<WeShortLinkPromotionTemplateMoments> updateWrapper = Wrappers.lambdaUpdate(WeShortLinkPromotionTemplateMoments.class);
            updateWrapper.set(WeShortLinkPromotionTemplateMoments::getDelFlag, 1);
            updateWrapper.eq(WeShortLinkPromotionTemplateMoments::getId, one.getId());
            weShortLinkPromotionTemplateMomentsService.update(updateWrapper);
        }
    }

    /**
     * 取消短链推广-应用消息
     *
     * @param promotionId
     */
    private void cancelAppMsg(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateAppMsg> queryWrapper = Wrappers.lambdaQuery(WeShortLinkPromotionTemplateAppMsg.class);
        queryWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getDelFlag, 0);
        WeShortLinkPromotionTemplateAppMsg one = weShortLinkPromotionTemplateAppMsgService.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            LambdaUpdateWrapper<WeShortLinkPromotionTemplateAppMsg> updateWrapper = Wrappers.lambdaUpdate(WeShortLinkPromotionTemplateAppMsg.class);
            updateWrapper.set(WeShortLinkPromotionTemplateAppMsg::getDelFlag, 1);
            updateWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getId, one.getId());
            weShortLinkPromotionTemplateAppMsgService.update(updateWrapper);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(WeShortLinkPromotionUpdateQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = weShortLinkPromotionMapper.selectById(query.getId());
        if (BeanUtil.isNotEmpty(weShortLinkPromotion)) {
            //任务状态: 0带推广 1推广中 2已结束 3暂存中
            if (weShortLinkPromotion.getTaskStatus().equals(1) || weShortLinkPromotion.getTaskStatus().equals(2)) {
                throw new ServiceException("当前状态无法修改！");
            }
            //短链推广
            WeShortLinkPromotion weShortLinkPromotionTemp = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
            //更新和发送
            PromotionType promotionType = ShortLinkPromotionStrategyFactory.getPromotionType(query.getType());
            promotionType.updateAndSend(query, weShortLinkPromotionTemp);
        }
    }

    @Override
    public WeShortLinkPromotionGetVo get(Long id) {
        LambdaQueryWrapper<WeShortLinkPromotion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeShortLinkPromotion::getId, id);
        queryWrapper.eq(WeShortLinkPromotion::getDelFlag, 0);
        WeShortLinkPromotion weShortLinkPromotion = getOne(queryWrapper);
        WeShortLinkPromotionGetVo result = Optional.ofNullable(weShortLinkPromotion).map(i -> {
            WeShortLinkPromotionGetVo vo = BeanUtil.copyProperties(weShortLinkPromotion, WeShortLinkPromotionGetVo.class);
            WeShortLink weShortLink = weShortLinkService.getById(weShortLinkPromotion.getShortLinkId());
            //短链详情
            WeShortLinkListVo weShortLinkListVo = BeanUtil.copyProperties(weShortLink, WeShortLinkListVo.class);
            String encode = Base62NumUtil.encode(weShortLinkListVo.getId());
            String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;
            weShortLinkListVo.setShortLink(shortLinkUrl);
            vo.setShortLink(weShortLinkListVo);

            Optional.ofNullable(vo.getMaterialId()).ifPresent(m -> {
                WeMaterial weMaterial = weMaterialMapper.selectById(m);
                Optional.ofNullable(weMaterial).ifPresent(item -> {
                    WeMaterialVo weMaterialVo = BeanUtil.copyProperties(weMaterial, WeMaterialVo.class);
                    vo.setWeMaterial(weMaterialVo);
                });
            });

            //任务状态: 0待推广 1推广中 2已结束
            //推广信息
            Integer type = i.getType();
            switch (type) {
                case 0:
                    getWeShortLinkPromotionTemplateClientVo(i.getId(), vo);
                    break;
                case 1:
                    getShortLinkPromotionTemplateGroupVo(i.getId(), vo);
                    break;
                case 2:
                    getWeShortLinkPromotionTemplateMomentsVo(i.getId(), vo);
                    break;
                case 3:
                    getWeShortLinkPromotionTemplateAppMsgVo(i.getId(), vo);
                    break;
                default:
                    break;
            }
            return vo;
        }).orElse(null);
        return result;
    }

    @Override
    public void delete(Long id) {
        LambdaUpdateWrapper<WeShortLinkPromotion> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(WeShortLinkPromotion::getId, id);
        updateWrapper.set(WeShortLinkPromotion::getDelFlag, 1);
        boolean update = this.update(updateWrapper);
        if (update) {
            //短链推广删除之后，短链推广模板不用处理，待推广状态的任务，也不会得到执行（执行时，有做判断）
            //已结束的任务也不用考虑

            //短链推广删除之后，对于已经处于推广中的任务，需要取消企微中的任务。
            cancelSend(id);
        }
    }

    /**
     * 取消处于推广中的企微任务
     *
     * @param id
     */
    private void cancelSend(Long id) {
        WeShortLinkPromotion weShortLinkPromotion = getById(id);
        if (weShortLinkPromotion.getTaskStatus().equals(1)) {
            switch (weShortLinkPromotion.getType()) {
                case 0:
                    //客户
                    cancelClientSend(id);
                    break;
                case 1:
                    //客群
                    cancelGroupSend(id);
                    break;
                case 2:
                    //朋友圈
                    cancelMomentsSend(id);
                    break;
                case 3:
                    //应用消息
                    cancelAppMsgSend(id);
                    break;
            }
        }
    }


    /**
     * 取消企微群发客户
     *
     * @param promotionId
     */
    private void cancelClientSend(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateClient> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getDelFlag, 0);
        WeShortLinkPromotionTemplateClient one = weShortLinkPromotionTemplateClientService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(client -> {
            LambdaQueryWrapper<WeShortLinkUserPromotionTask> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 1);
            wrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, client.getId());
            wrapper.eq(WeShortLinkUserPromotionTask::getDelFlag, 0);
            List<WeShortLinkUserPromotionTask> list = weShortLinkUserPromotionTaskService.list(wrapper);
            if (list != null && list.size() > 0) {
                list.stream().forEach(i -> Optional.ofNullable(i.getMsgId()).ifPresent(o -> {
                    WeCancelGroupMsgSendQuery query = new WeCancelGroupMsgSendQuery();
                    query.setMsgid(o);
                    qwCustomerClient.cancelGroupMsgSend(query);
                }));
            }
        });
    }

    /**
     * 取消企微群发客群
     *
     * @param promotionId
     */
    private void cancelGroupSend(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateGroup> queryWrapper = Wrappers.lambdaQuery(WeShortLinkPromotionTemplateGroup.class);
        queryWrapper.eq(WeShortLinkPromotionTemplateGroup::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateGroup::getDelFlag, 0);
        WeShortLinkPromotionTemplateGroup one = weShortLinkPromotionTemplateGroupService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(group -> {
            LambdaQueryWrapper<WeShortLinkUserPromotionTask> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 1);
            wrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, group.getId());
            wrapper.eq(WeShortLinkUserPromotionTask::getDelFlag, 0);
            List<WeShortLinkUserPromotionTask> list = weShortLinkUserPromotionTaskService.list(wrapper);
            if (list != null && list.size() > 0) {
                list.stream().forEach(i -> Optional.ofNullable(i.getMsgId()).ifPresent(o -> {
                    WeCancelGroupMsgSendQuery query = new WeCancelGroupMsgSendQuery();
                    query.setMsgid(o);
                    qwCustomerClient.cancelGroupMsgSend(query);
                }));
            }
        });
    }

    /**
     * 取消企微群发朋友圈
     *
     * @param promotionId
     */
    private void cancelMomentsSend(Long promotionId) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateMoments> queryWrapper = Wrappers.lambdaQuery(WeShortLinkPromotionTemplateMoments.class);
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getDelFlag, 0);
        WeShortLinkPromotionTemplateMoments one = weShortLinkPromotionTemplateMomentsService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(moments -> {
            LambdaQueryWrapper<WeShortLinkUserPromotionTask> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 2);
            wrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, moments.getId());
            wrapper.eq(WeShortLinkUserPromotionTask::getDelFlag, 0);
            WeShortLinkUserPromotionTask task = weShortLinkUserPromotionTaskService.getOne(wrapper);
            Optional.ofNullable(task).ifPresent(i -> {
                AjaxResult<MomentsCreateResultDto> momentTaskResult = qwMomentsClient.getMomentTaskResult(i.getMsgId());
                Optional.ofNullable(momentTaskResult).filter(o -> o.getCode() == 200).ifPresent(m -> {
                    MomentsCreateResultDto data = m.getData();
                    MomentsCreateResultDto.Result result = data.getResult();
                    //停止发送朋友圈
                    CancelMomentTaskDto cancelMomentTaskDto = new CancelMomentTaskDto();
                    cancelMomentTaskDto.setMoment_id(result.getMoment_id());
                    qwMomentsClient.cancel_moment_task(cancelMomentTaskDto);
                });
            });
        });
    }

    /**
     * 取消应用消息发送
     *
     * @param promotionId
     */
    private void cancelAppMsgSend(Long promotionId) {
        //无需处理
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<WeShortLinkPromotion> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.in(WeShortLinkPromotion::getId, ids);
        updateWrapper.set(WeShortLinkPromotion::getDelFlag, 1);
        boolean update = this.update(updateWrapper);
        if (update) {
            for (Long id : ids) {
                cancelSend(id);
            }
        }
    }

    /**
     * 客户
     *
     * @param promotionId 短链推广Id
     * @param vo
     * @return
     */
    private void getWeShortLinkPromotionTemplateClientVo(Long promotionId, WeShortLinkPromotionGetVo vo) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateClient> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateClient::getDelFlag, 0);
        WeShortLinkPromotionTemplateClient one = weShortLinkPromotionTemplateClientService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            WeShortLinkPromotionTemplateClientVo clientVo = BeanUtil.copyProperties(one, WeShortLinkPromotionTemplateClientVo.class);
            //群发客户分类：0全部客户 1部分客户
            if (one.getType() == 1) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                List<String> list = Arrays.asList(one.getUserIds().split(","));
                sysUserQuery.setWeUserIds(list);
                AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                if (weUsersResult.getCode() == 200) {
                    List<SysUserVo> data = weUsersResult.getData();
                    Map<String, String> map = new HashMap<>(list.size());
                    data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                    clientVo.setUsers(map);
                }
            }
            //标签
            if (StrUtil.isNotBlank(one.getLabelIds())) {
                LambdaQueryWrapper<WeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                tagQueryWrapper.eq(WeTag::getDelFlag, 0);
                tagQueryWrapper.in(WeTag::getTagId, one.getLabelIds().split(","));
                List<WeTag> list = weTagService.list(tagQueryWrapper);
                Map<String, String> map = new HashMap<>(list.size());
                list.stream().forEach(o -> map.put(o.getTagId(), o.getName()));
                clientVo.setLabels(map);
            }
            //附件
            LambdaQueryWrapper<WeShortLinkPromotionAttachment> attachmentQueryWrapper = Wrappers.lambdaQuery();
            attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getTemplateType, 0);
            attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getTemplateId, one.getId());
            attachmentQueryWrapper.eq(WeShortLinkPromotionAttachment::getDelFlag, 0);
            List<WeShortLinkPromotionAttachment> list = weShortLinkPromotionAttachmentService.list(attachmentQueryWrapper);
            if (list != null && list.size() > 0) {
                List<WeMessageTemplate> collect = list.stream().map(o -> BeanUtil.copyProperties(o, WeMessageTemplate.class)).collect(Collectors.toList());
                vo.setAttachments(collect);
            }
            vo.setClient(clientVo);
        });
    }

    /**
     * 客群
     *
     * @param promotionId 短链推广Id
     * @param vo
     * @return
     */
    private void getShortLinkPromotionTemplateGroupVo(Long promotionId, WeShortLinkPromotionGetVo vo) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateGroup> groupQueryWrapper = Wrappers.lambdaQuery();
        groupQueryWrapper.eq(WeShortLinkPromotionTemplateGroup::getPromotionId, promotionId);
        groupQueryWrapper.eq(WeShortLinkPromotionTemplateGroup::getDelFlag, 0);
        WeShortLinkPromotionTemplateGroup group = weShortLinkPromotionTemplateGroupService.getOne(groupQueryWrapper);
        Optional.ofNullable(group).ifPresent(i -> {
            WeShortLinkPromotionTemplateGroupVo groupVo = BeanUtil.copyProperties(group, WeShortLinkPromotionTemplateGroupVo.class);
            //客群分类 0全部群 1部分群
            if (groupVo.getType() == 1) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                List<String> userIds = Arrays.asList(groupVo.getUserIds().split(","));
                sysUserQuery.setWeUserIds(userIds);
                AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                if (weUsersResult.getCode() == 200) {
                    List<SysUserVo> data = weUsersResult.getData();
                    Map<String, String> map = new HashMap<>(userIds.size());
                    data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                    groupVo.setUsers(map);
                }
            }
            //附件
            LambdaQueryWrapper<WeShortLinkPromotionAttachment> attachmentQueryWrapper1 = Wrappers.lambdaQuery();
            attachmentQueryWrapper1.eq(WeShortLinkPromotionAttachment::getTemplateType, 1);
            attachmentQueryWrapper1.eq(WeShortLinkPromotionAttachment::getTemplateId, group.getId());
            attachmentQueryWrapper1.eq(WeShortLinkPromotionAttachment::getDelFlag, 0);
            List<WeShortLinkPromotionAttachment> list1 = weShortLinkPromotionAttachmentService.list(attachmentQueryWrapper1);
            if (list1 != null && list1.size() > 0) {
                List<WeMessageTemplate> collect = list1.stream().map(o -> BeanUtil.copyProperties(o, WeMessageTemplate.class)).collect(Collectors.toList());
                vo.setAttachments(collect);
            }
            vo.setGroup(groupVo);
        });
    }

    /**
     * 朋友圈
     *
     * @param promotionId 短链推广Id
     * @return
     */
    private void getWeShortLinkPromotionTemplateMomentsVo(Long promotionId, WeShortLinkPromotionGetVo result) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateMoments> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateMoments::getDelFlag, 0);
        WeShortLinkPromotionTemplateMoments one = weShortLinkPromotionTemplateMomentsService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            WeShortLinkPromotionTemplateMomentsVo vo = BeanUtil.copyProperties(i, WeShortLinkPromotionTemplateMomentsVo.class);
            vo.setType(i.getScopeType());
            //群发朋友圈分类 0全部客户 1部分客户
            if (vo.getScopeType().equals(1)) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                List<String> list = Arrays.asList(i.getUserIds().split(","));
                sysUserQuery.setWeUserIds(list);
                AjaxResult<List<SysUserVo>> weUsersResult = qwSysUserClient.getUserListByWeUserIds(sysUserQuery);
                if (weUsersResult.getCode() == 200) {
                    List<SysUserVo> data = weUsersResult.getData();
                    Map<String, String> map = new HashMap<>(list.size());
                    data.stream().forEach(o -> map.put(o.getWeUserId(), o.getUserName()));
                    vo.setUsers(map);
                }
            }
            //标签
            if (StrUtil.isNotBlank(i.getLabelIds())) {
                LambdaQueryWrapper<WeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                tagQueryWrapper.eq(WeTag::getDelFlag, 0);
                tagQueryWrapper.in(WeTag::getTagId, i.getLabelIds().split(","));
                List<WeTag> list = weTagService.list(tagQueryWrapper);
                Map<String, String> map = new HashMap<>(list.size());
                list.stream().forEach(o -> map.put(o.getTagId(), o.getName()));
                vo.setLabels(map);
            }
            result.setMoments(vo);
        });
    }

    /**
     * 应用消息
     *
     * @param promotionId 短链推广Id
     * @return
     */
    private void getWeShortLinkPromotionTemplateAppMsgVo(Long promotionId, WeShortLinkPromotionGetVo result) {
        LambdaQueryWrapper<WeShortLinkPromotionTemplateAppMsg> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getPromotionId, promotionId);
        queryWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getDelFlag, 0);
        WeShortLinkPromotionTemplateAppMsg one = weShortLinkPromotionTemplateAppMsgService.getOne(queryWrapper);
        Optional.ofNullable(one).ifPresent(i -> {
            WeShortLinkPromotionTemplateAppMsgVo vo = BeanUtil.copyProperties(one, WeShortLinkPromotionTemplateAppMsgVo.class);
            String userIds = one.getUserIds();
            Optional.ofNullable(userIds).ifPresent(u -> {
                WeSopExecuteUserConditVo.ExecuteUserCondit user = new WeSopExecuteUserConditVo.ExecuteUserCondit();
                user.setChange(true);
                user.setWeUserIds(Arrays.asList(u.split(",")));
                vo.setExecuteUserCondit(user);
            });

            String deptIds = one.getDeptIds();
            String postIds = one.getPostIds();
            if (deptIds != null || postIds != null) {
                WeSopExecuteUserConditVo.ExecuteDeptCondit dept = new WeSopExecuteUserConditVo.ExecuteDeptCondit();
                dept.setChange(true);
                if (StrUtil.isNotBlank(deptIds)) {
                    dept.setDeptIds(Arrays.asList(deptIds.split(",")));
                }
                if (StrUtil.isNotBlank(postIds)) {
                    dept.setPosts(Arrays.asList(postIds.split(",")));
                }
                vo.setExecuteDeptCondit(dept);
            }
            result.setAppMsg(vo);
        });
    }

}
