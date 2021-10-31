package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.query.WeGroupMsgListQuery;
import com.linkwechat.wecom.domain.vo.WeGroupMessageListVo;
import com.linkwechat.wecom.mapper.WeGroupMessageListMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<WeGroupMessageListVo> getGroupMsgDetail(Long msgTemplateId) {
        return this.baseMapper.getGroupMsgDetails(ListUtil.toList(msgTemplateId));
    }


    /*@Override
    public boolean saveGroupMessage(WeAddGroupMessageQuery query) {
        //存一个假msgId
        String msgId = IdUtils.simpleUUID();

        WeGroupMessageList groupMessage = new WeGroupMessageList();
        BeanUtil.copyProperties(query,groupMessage);
        if(query.getSendTime() != null){
            groupMessage.setIsTask(1);
        }
        groupMessage.setMsgId(msgId);
        JSONObject condition = new JSONObject();
        condition.put("toUser",query.getToUser());
        condition.put("toParty",query.getToParty());
        condition.put("toTag",query.getToTag());
        groupMessage.setFilterCondition(condition.toJSONString());
        groupMessage.setMsgId(msgId);

        //保存消息
        boolean groupMessageSaveResult = saveOrUpdate(groupMessage);

        if(!groupMessageSaveResult){
            throw new WeComException("创建失败");
        }

        //保存附件信息
        List<WeGroupMessageAttachments> attachmentsList = query.getAttachmentsList().stream().map(attachment -> {
            WeGroupMessageAttachments attachments = new WeGroupMessageAttachments();
            BeanUtil.copyProperties(attachment, attachments);
            attachments.setMsgId(msgId);
            return attachments;
        }).collect(Collectors.toList());
        attachmentsService.saveBatch(attachmentsList);


        WeAddMsgTemplateQuery templateQuery = new WeAddMsgTemplateQuery();
        templateQuery.setAttachments(query.getAttachmentsList());
        templateQuery.setChat_type(query.getChatType());
        Set<String> userIdSet = new HashSet<>();
        List<WeGroupMessageSendResult> sendResultList = new ArrayList<>();
        if(StringUtils.isNotEmpty(query.getToUser())){
            Set<String> toUser = Arrays.stream(query.getToUser().split(",")).collect(Collectors.toSet());
            userIdSet.addAll(toUser);
        }
        if(StringUtils.isNotEmpty(query.getToParty())){
            Arrays.stream(query.getToParty().split(",")).forEach(part ->{
                WeUserListDto weUserListDto = weUserClient.simpleList(Long.parseLong(part), 1);
                List<String> userIds = weUserListDto.getUserlist().stream().map(WeUserDto::getUserid).collect(Collectors.toList());
                userIdSet.addAll(userIds);
            });
        }
        if(ObjectUtil.equal("single",query.getChatType())){
            //查询客户数据
            WeCustomer weCustomer = new WeCustomer();
            weCustomer.setUserIds(String.join(",", userIdSet));
            weCustomer.setTagIds(query.getToTag());
            weCustomer.setStatus(0);
            List<WeCustomer> customerList = weCustomerService.selectWeCustomerAllList(weCustomer);
            List<WeFlowerCustomerRel> customerRelList = customerList.stream().map(WeCustomer::getWeFlowerCustomerRels).flatMap(Collection::stream).collect(Collectors.toList());
            Map<String, List<WeFlowerCustomerRel>> customerUserList = customerRelList.stream().collect(Collectors.groupingBy(WeFlowerCustomerRel::getUserId));
            userIdSet.clear();
            userIdSet.addAll(customerUserList.keySet());
            List<String> eids = customerList.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList());
            if(eids.size() > 10000){
                throw new WeComException("最多可发送1万个客户");
            }
            templateQuery.setExternal_userid(eids);

            if(CollectionUtil.isNotEmpty(customerRelList)){
                customerRelList.forEach(customerRel ->{
                    WeGroupMessageSendResult result = new WeGroupMessageSendResult();
                    result.setMsgId(msgId);
                    result.setStatus(0);
                    result.setUserId(customerRel.getUserId());
                    result.setExternalUserid(customerRel.getExternalUserid());
                    sendResultList.add(result);
                });
            }
        }else {
            templateQuery.setSender(query.getToUser());
            userIdSet.clear();
            userIdSet.addAll(Arrays.asList(query.getToUser().split(",").clone()));
            WeGroup weGroup = new WeGroup();
            weGroup.setUserIds(query.getToUser());
            List<WeGroup> weGroupList = weGroupService.selectWeGroupList(weGroup);
            if(CollectionUtil.isNotEmpty(weGroupList)){
                weGroupList.stream().map(WeGroup::getChatId).forEach(chatId ->{
                    WeGroupMessageSendResult result = new WeGroupMessageSendResult();
                    result.setMsgId(msgId);
                    result.setStatus(0);
                    result.setChatId(chatId);
                    sendResultList.add(result);
                });
            }
        }

        //群发消息成员发送任务保存
        List<WeGroupMessageTask> taskList = userIdSet.stream().map(userId -> {
            WeGroupMessageTask messageTask = new WeGroupMessageTask();
            messageTask.setMsgId(msgId);
            messageTask.setUserId(userId);
            messageTask.setStatus(0);
            return messageTask;
        }).collect(Collectors.toList());
        groupMessageTaskService.saveBatch(taskList);

        //群发消息成员执行结果保存
        sendResultService.saveBatch(sendResultList);

        //SendMessageResultDto resultDto = messagePushClient.addMsgTemplate(templateQuery);

        return groupMessageSaveResult;
    }*/
}
