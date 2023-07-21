package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.enums.WeMsgTypeEnum;
import com.linkwechat.common.enums.task.WeTasksTitleEnum;
import com.linkwechat.common.enums.task.WeTasksTypeEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.task.entity.WeTasks;
import com.linkwechat.domain.task.vo.WeTasksVO;
import com.linkwechat.mapper.WeLeadsSeaMapper;
import com.linkwechat.mapper.WeTasksMapper;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeTasksService;
import com.linkwechat.service.QwAppSendMsgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 待办任务 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-07-20
 */
@Service
public class WeTasksServiceImpl extends ServiceImpl<WeTasksMapper, WeTasks> implements IWeTasksService {

    @Resource
    private WeLeadsSeaMapper weLeadsSeaMapper;
    @Resource
    private IWeCorpAccountService weCorpAccountService;
    @Resource
    private QwAppSendMsgService qwAppSendMsgService;

    @Override
    public List<WeTasksVO> myList() {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getUserId, SecurityUtils.getLoginUser().getUserId());
        queryWrapper.eq(WeTasks::getStatus, 0);
        queryWrapper.eq(WeTasks::getVisible, 1);
        queryWrapper.orderByDesc(WeTasks::getCreateTime);
        List<WeTasks> weTasks = this.baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(weTasks, WeTasksVO.class);
    }

    @Override
    public List<WeTasksVO> history() {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getUserId, SecurityUtils.getLoginUser().getUserId());
        queryWrapper.ne(WeTasks::getStatus, 0);
        queryWrapper.eq(WeTasks::getVisible, 1);
        queryWrapper.orderByDesc(WeTasks::getCreateTime);
        List<WeTasks> weTasks = this.baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(weTasks, WeTasksVO.class);
    }

    @Override
    public void add(WeTasks weTasks) {
        weTasks.setId(IdUtil.getSnowflakeNextId());
        weTasks.setSendTime(LocalDateTime.now());
        weTasks.setStatus(0);
        weTasks.setDelFlag(Constants.COMMON_STATE);
        this.baseMapper.insert(weTasks);
    }

    @Override
    public void finish(Long id) {
        LambdaUpdateWrapper<WeTasks> updateWrapper = Wrappers.lambdaUpdate(WeTasks.class);
        updateWrapper.eq(WeTasks::getId, id);
        updateWrapper.set(WeTasks::getStatus, 1);
        this.update(updateWrapper);
    }

    @Override
    public void addLeadsLongTimeNotFollowUp(Long leadsId, String name, Long userId, String weUserId, Long seaId) {
        WeLeadsSea sea = weLeadsSeaMapper.selectById(seaId);
        //不自动回收
        if (sea.getIsAutoRecovery().equals(0)) {
            return;
        }
        Integer first = sea.getFirst();
        DateTime dateTime = DateUtil.offsetDay(DateUtil.beginOfDay(DateUtil.date()), first - 1);
        WeTasks build = WeTasks.builder()
                .id(IdUtil.getSnowflakeNextId())
                .userId(userId)
                .weUserId(weUserId)
                .type(WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getCode())
                .title(WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getTitle())
                .content(new JSONObject().put("客户", name).toString())
                .sendTime(dateTime.toLocalDateTime())
                //TODO 链接等待前端给
                .url(null)
                .status(0)
                .delFlag(Constants.COMMON_STATE)
                .visible(0)
                .leadsId(leadsId)
                .build();
        this.save(build);
    }

    @Override
    public void cancelLeadsLongTimeNotFollowUp(Long leadsId, Long userId) {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getType, WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getCode());
        queryWrapper.eq(WeTasks::getLeadsId, leadsId);
        queryWrapper.eq(WeTasks::getUserId, userId);
        queryWrapper.eq(WeTasks::getStatus, 0);
        WeTasks one = this.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            if (one.getVisible().equals(0)) {
                this.removeById(one.getId());
            } else {
                LambdaUpdateWrapper<WeTasks> updateWrapper = Wrappers.lambdaUpdate(WeTasks.class);
                updateWrapper.eq(WeTasks::getId, one.getId());
                updateWrapper.set(WeTasks::getStatus, 1);
                this.update(updateWrapper);
            }
        }
    }

    @Override
    public void cancelAndAddLeadsLongTimeNotFollowUp(Long leadsId, String name, Long userId, String weUserId, Long seaId) {
        this.cancelLeadsLongTimeNotFollowUp(leadsId, userId);
        this.addLeadsLongTimeNotFollowUp(leadsId, name, userId, weUserId, seaId);
    }

    @Override
    public void handlerLeadsLongTimeNotFollowUp() {
        DateTime dateTime = DateUtil.beginOfDay(DateUtil.date());
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getType, WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getCode());
        queryWrapper.eq(WeTasks::getSendTime, dateTime.toLocalDateTime());
        queryWrapper.eq(WeTasks::getStatus, 0);
        queryWrapper.eq(WeTasks::getVisible, 0);
        queryWrapper.eq(WeTasks::getDelFlag, Constants.COMMON_STATE);
        List<WeTasks> list = this.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            //修改线索状态为显示
            LambdaUpdateWrapper<WeTasks> updateWrapper = Wrappers.lambdaUpdate(WeTasks.class);
            updateWrapper.in(WeTasks::getId, list.stream().map(WeTasks::getId).collect(Collectors.toList()));
            updateWrapper.set(WeTasks::getVisible, 1);
            this.update(updateWrapper);
        }
        for (WeTasks weTasks : list) {
            //发送应用通知消
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            QwAppMsgBody body = new QwAppMsgBody();
            body.setCorpId(weCorpAccount.getCorpId());
            body.setCorpUserIds(Arrays.asList(weTasks.getWeUserId()));
            //类型
            body.setBusinessType(QwAppMsgBusinessTypeEnum.COMMON.getType());
            WeMessageTemplate messageTemplate = new WeMessageTemplate();
            messageTemplate.setMsgType(WeMsgTypeEnum.TASKCARD.getMessageType());
            messageTemplate.setTitle(WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getTitle());
            //描述
            JSONObject jsonObject = JSONObject.parseObject(weTasks.getContent());
            String desc = StrUtil.format(MessageConstants.LEADS_LONG_TIME_NOT_FOLLOW_UP, DateUtil.date().toDateStr(), jsonObject.get("客户"));
            messageTemplate.setDescription(desc);
            //TODO 链接，等待前端给, 参数要加上
            messageTemplate.setLinkUrl(null);
            messageTemplate.setBtntxt("详情");
            body.setMessageTemplates(messageTemplate);
            qwAppSendMsgService.appMsgSend(body);
        }

    }


}
