package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.*;
import com.linkwechat.wecom.domain.dto.customer.*;
import com.linkwechat.wecom.domain.query.GroupChatStatisticQuery;
import com.linkwechat.wecom.domain.query.UserBehaviorDataQuery;


import java.util.Map;

/**
 * @description: 获取配置客户联系人功能的成员
 * @author: HaoN
 * @create: 2020-09-15 14:15
 **/
public interface WeCustomerClient {

    /**
     * 获取配置了客户联系功能的成员列表
     * @return
     */
    @Request(url = "/externalcontact/get_follow_user_list")
    FollowUserList getFollowUserList();


    /**
     * 获取指定企业服务管理人员所有用的客户id
     * @param userId 企业服务管理人员id（具有外部联系功能的员工）
     * @return
     */
    @Request(url = "/externalcontact/list")
    ExternalUserList list(@Query("userid") String userId);


    /**
     * 根据客户id获取客户详情
     * @param externalUserid
     * @return
     */
    @Request(url = "/externalcontact/get")
    ExternalUserDetail get(@Query("external_userid") String externalUserid);

    /**
     * 根据企业成员id批量获取客户详情
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/batch/get_by_user", type = "POST")
    ExternalUserList getByUser(@DataObject Map<String,Object> query);


    /**
     *  修改客户备注信息
     * @param weCustomerRemark
     * @return
     */
    @Request(url="/externalcontact/remark",
            type = "POST"
    )
    WeResultDto remark(@DataObject WeCustomerDto.WeCustomerRemark weCustomerRemark);


    /**
     * 编辑客户标签
     * @return
     */
    @Request(url = "/externalcontact/mark_tag",
             type = "POST"
    )
    WeResultDto makeCustomerLabel(@DataObject CutomerTagEdit cutomerTagEdit);


    /**
     * 客户发送欢迎语
     */
    @Request(url = "/externalcontact/send_welcome_msg",
            type = "POST")
    WeResultDto sendWelcomeMsg(@DataObject WeWelcomeMsg wxCpWelcomeMsg);


    /**
     * unionid转换external_userid
     * @return
     */
    @Request(url = "/externalcontact/unionid_to_external_userid",
            type = "POST")
    ExternalUserDetail unionidToExternalUserid(@DataObject ExternalUserDetail.ExternalContact  unionid);


    /**
     * 联系客户统计
     * @return
     */
    @Request(url = "/externalcontact/get_user_behavior_data",
            type = "POST")
    UserBehaviorDataDto getUserBehaviorData(@JSONBody UserBehaviorDataQuery query);

    /**
     * 群聊数据统计（按群主聚合的方式）
     * @return
     */
    @Request(url = "/externalcontact/groupchat/statistic",
            type = "POST")
    GroupChatStatisticDto getGroupChatStatistic(@JSONBody GroupChatStatisticQuery query);

    /**
     * 群聊数据统计(按自然日聚合的方式)
     * @return
     */
    @Request(url = "/externalcontact/groupchat/statistic_group_by_day",
            type = "POST")
    GroupChatStatisticDto getGroupChatStatisticGroupByDay(@JSONBody GroupChatStatisticQuery query);
}
