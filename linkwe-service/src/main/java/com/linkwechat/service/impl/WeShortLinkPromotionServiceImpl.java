package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.domain.*;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.*;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeShortLinkPromotionMapper;
import com.linkwechat.service.*;
import com.linkwechat.service.impl.strategic.shortlink.MomentsPromotion;
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
    private QwFileClient qwFileClient;
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
    public Long ts(WeShortLinkPromotionAddQuery query) throws IOException {
        WeShortLinkPromotion weShortLinkPromotion = BeanUtil.copyProperties(query, WeShortLinkPromotion.class);
        Optional.ofNullable(query.getMaterialId()).ifPresent(i -> weShortLinkPromotion.setMaterialId(i));
        weShortLinkPromotion.setDelFlag(0);
        weShortLinkPromotion.setTaskStatus(0);
        weShortLinkPromotionMapper.insert(weShortLinkPromotion);

        PromotionType promotionType = new MomentsPromotion();
        WeMessageTemplate weMessageTemplate = promotionType.getPromotionUrl(weShortLinkPromotion.getId(), query.getShortLinkId(), query.getStyle(), query.getMaterialId());
        weShortLinkPromotion.setUrl(weMessageTemplate.getPicUrl());
        return weShortLinkPromotion.getId();
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
            vo.setShortLink(weShortLinkListVo);

            Optional.ofNullable(vo.getMaterialId()).ifPresent(m -> {
                WeMaterial weMaterial = weMaterialMapper.selectById(m);
                Optional.ofNullable(weMaterial).ifPresent(item -> vo.setMaterialUrl(item.getMaterialUrl()));
            });

            //任务状态: 0待推广 1推广中 2已结束 3暂存中
            if (i.getTaskStatus() != 3) {
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
            }
            return vo;
        }).orElse(null);
        return result;
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
