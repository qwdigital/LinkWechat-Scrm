package com.linkwechat.wecom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeGroupMsgListQuery;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;
import com.linkwechat.wecom.domain.vo.WeGroupMessageListVo;
import com.linkwechat.wecom.mapper.WeGroupMessageListMapper;
import com.linkwechat.wecom.service.IWeGroupMessageListService;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 群发消息列Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Service
public class WeGroupMessageListServiceImpl extends ServiceImpl<WeGroupMessageListMapper, WeGroupMessageList> implements IWeGroupMessageListService {

    @Autowired
    private WeCustomerMessagePushClient messagePushClient;

    @Autowired
    private IWeUserService weUserService;

    @Override
    public PageInfo<WeGroupMessageListVo> queryList(WeGroupMessageList weGroupMessageList) {
        PageInfo<WeGroupMessageListVo> listPageInfo = new PageInfo<>();
        LambdaQueryWrapper<WeGroupMessageList> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(weGroupMessageList.getMsgId())) {
            lqw.eq(WeGroupMessageList::getMsgId, weGroupMessageList.getMsgId());
        }
        if (StringUtils.isNotBlank(weGroupMessageList.getChatType())) {
            lqw.eq(WeGroupMessageList::getChatType, weGroupMessageList.getChatType());
        }
        if (StringUtils.isNotBlank(weGroupMessageList.getUserId())) {
            lqw.eq(WeGroupMessageList::getUserId, weGroupMessageList.getUserId());
        }
        if (weGroupMessageList.getSendTime() != null) {
            lqw.le(WeGroupMessageList::getSendTime, weGroupMessageList.getBeginTime());
            lqw.gt(WeGroupMessageList::getSendTime, weGroupMessageList.getEndTime());
        }
        if (weGroupMessageList.getCreateType() != null) {
            lqw.eq(WeGroupMessageList::getCreateType, weGroupMessageList.getCreateType());
        }

        List<WeGroupMessageList> list = this.list(lqw);
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> msgIds = list.stream().map(WeGroupMessageList::getMsgId).collect(Collectors.toList());
            List<String> userIds = list.stream().map(WeGroupMessageList::getUserId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            List<WeUser> userList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(userIds)){
                userList.addAll(weUserService.list(new LambdaQueryWrapper<WeUser>().in(WeUser::getUserId, userIds)));
            }
            List<WeGroupMessageDetailVo> groupMsgDetailList = this.baseMapper.getGroupMsgDetails(msgIds);
            Map<String, WeGroupMessageDetailVo> groupMsgDetailMap = groupMsgDetailList.stream().collect(Collectors.toMap(WeGroupMessageDetailVo::getMsgId, item -> item));
            List<WeGroupMessageListVo> resultList = list.stream().map(groupMessage -> {
                WeGroupMessageDetailVo weGroupMessageDetailVo = groupMsgDetailMap.get(groupMessage.getMsgId());
                WeGroupMessageListVo weGroupMessageListVo = new WeGroupMessageListVo();
                BeanUtil.copyProperties(groupMessage, weGroupMessageListVo);
                weGroupMessageListVo.setAttachments(weGroupMessageDetailVo.getAttachments());
                weGroupMessageListVo.setSenderTotalNums(weGroupMessageDetailVo.getSenders().size());
                List<WeGroupMessageTask> senderList = weGroupMessageDetailVo.getSenders().stream().filter(sender -> ObjectUtil.equal(0, sender.getStatus())).collect(Collectors.toList());
                weGroupMessageListVo.setSenderNums(senderList.size());
                weGroupMessageListVo.setServiceTotalMember(weGroupMessageDetailVo.getExtralInfos().size());
                List<WeGroupMessageSendResult> sendResults = weGroupMessageDetailVo.getExtralInfos().stream().filter(item -> ObjectUtil.equal(0, item.getStatus())).collect(Collectors.toList());
                weGroupMessageListVo.setServiceMember(sendResults.size());

                if (StringUtils.isNotEmpty(groupMessage.getUserId()) && CollectionUtil.isNotEmpty(userList)) {
                    userList.forEach(user -> {
                        if (ObjectUtil.equal(user.getUserId(), groupMessage.getUserId())) {
                            weGroupMessageListVo.setUserName(user.getName());
                        }
                    });
                }
                return weGroupMessageListVo;
            }).collect(Collectors.toList());
            listPageInfo.setList(resultList);
        }
        listPageInfo.setTotal(new PageInfo<>(list).getTotal());
        return listPageInfo;
    }


    @Override
    public WeGroupMsgListDto getGroupMsgList(String chatType, Long startTime, Long endTime, String cursor) {
        WeGroupMsgListQuery query = new WeGroupMsgListQuery();
        query.setChat_type(chatType);
        query.setStart_time(startTime);
        query.setEnd_time(endTime);
        query.setCursor(cursor);
        WeGroupMsgListDto groupMsgList = messagePushClient.getGroupMsgList(query);
        if (groupMsgList != null && ObjectUtil.equal(0, groupMsgList.getErrcode())) {
            if (StringUtils.isNotEmpty(groupMsgList.getNextCursor())) {
                WeGroupMsgListDto clildMsgList = getGroupMsgList(query.getChat_type(), query.getStart_time(), query.getEnd_time(), groupMsgList.getNextCursor());
                List<WeGroupMsgDto> list = groupMsgList.getGroupMsgList();
                list.addAll(clildMsgList.getGroupMsgList());
                groupMsgList.setGroupMsgList(list);
                groupMsgList.setNextCursor(clildMsgList.getNextCursor());
            }
        }
        return groupMsgList;
    }

    @Override
    public WeGroupMessageDetailVo getGroupMsgDetail(String msgId) {
        WeGroupMessageDetailVo detailVo = new WeGroupMessageDetailVo();
        WeGroupMessageList weGroupMessage = getOne(new LambdaQueryWrapper<WeGroupMessageList>().eq(WeGroupMessageList::getMsgId, msgId));
        BeanUtil.copyProperties(weGroupMessage, detailVo);
        if(StringUtils.isNotEmpty(detailVo.getUserId())){
            WeUser weUser = weUserService.getOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, detailVo.getUserId()).last("limit 1"));
            detailVo.setUserName(weUser.getName());
        }
        WeGroupMessageDetailVo groupMsgDetail = this.baseMapper.getGroupMsgDetail(msgId);
        detailVo.setAttachments(groupMsgDetail.getAttachments());
        detailVo.setSenders(groupMsgDetail.getSenders());
        detailVo.setExtralInfos(groupMsgDetail.getExtralInfos());
        return detailVo;
    }
}
