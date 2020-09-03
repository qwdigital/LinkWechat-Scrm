package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.WeUserDto;

/**
 * @description: 企业微信通讯录成员
 * @author: HaoN
 * @create: 2020-08-27 16:42
 **/
public interface WeUserClient {

    /**
     * 创建用户
     * @param weUserDto
     * @return
     */
    @Request(url="/user/create",
            type = "POST"
    )
    WeResultDto createUser(@DataObject WeUserDto weUserDto);


    /**
     * 根据用户账号,获取用户详情信息
     * @param userid
     * @return
     */
    @Request(url = "/user/get")
    WeUserDto getUserByUserId(@Query("userid") String userid);


    /**
     * 更新通讯录用户
     * @param weUserDto
     * @return
     */
    @Request(url="/user/update",
            type = "POST"
    )
    WeResultDto updateUser(@DataObject WeUserDto weUserDto);


    /**
     * 根据账号删除指定用户
     * @param userid
     * @return
     */
    @Request(url = "/user/delete")
    WeResultDto deleteUserByUserId(@Query("userid")String userid);
}
