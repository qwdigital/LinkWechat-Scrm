package com.linkwechat.domain.wecom.vo.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 成员列表
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeThirdLoginUserVo extends WeResultVo {

    /**
     * 登录用户的类型：1.创建者 2.内部系统管理员 3.外部系统管理员 4.分级管理员 5.成员
     */
    private Integer userType;
    /**
     * 登录用户的信息
     */
    private JSONObject userInfo;

    /**
     * 授权方企业信息
     */
    private JSONObject corpInfo;

    /**
     * 手该管理员在该提供商中能使用的应用列表，当登录用户为管理员时返回
     */
    private JSONArray agent;


    private JSONObject authInfo;

}
