package com.linkwechat.service;

import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;

import java.util.List;

public interface IWeMessagePushService {

    /**
     * 推送信息给员工附带自己的url链接，跳转H5
     * @param toUser
     * @param textContent
     * @param taskType
     * @param isJumpUrl 是否需要跳转url true:是;false:否
     */
    void pushMessageSelfH5(List<String> toUser, String textContent, Integer taskType,boolean isJumpUrl);


    /**
     * 通过企业微信官方的方式推送消息
     */
    void officialPushMessage(WeAddGroupMessageQuery query);
}
