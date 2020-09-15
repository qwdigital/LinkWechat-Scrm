package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeFollowUserDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;

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
    WeFollowUserDto getFollowUserList();


    /**
     * 获取指定企业服务管理人员所有用的客户id
     * @param userId 企业服务管理人员id（具有外部联系功能的员工）
     * @return
     */
    @Request(url = "/externalcontact/list")
    WeCustomerDto list(@Query("userid") String userId);


    /**
     * 根据客户id获取客户详情
     * @param externalUserid
     * @return
     */
    @Request(url = "/externalcontact/get")
    WeCustomerDto get(@Query("external_userid")String externalUserid);


    /**
     *  修改客户备注信息
     * @param weCustomerRemark
     * @return
     */
    @Request(url="/externalcontact/remark",
            type = "POST"
    )
    WeResultDto remark(@DataObject WeCustomerDto.WeCustomerRemark weCustomerRemark);


}
