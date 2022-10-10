package com.linkwechat.domain.wecom.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 获取access_token
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCorpTokenVo extends WeResultVo {
    /**
     * 获取到的凭证，最长为512字节
     */
    private String accessToken;

    /**
     * 服务商的access_token
     */
    private String providerAccessToken;

    /**
     * 第三方应用access_token
     */
    private String suiteAccessToken;

    /**
     * 获取到的凭证，最长为512字节
     */
    private Integer expiresIn;


    /**
     * 管理员
     */
    private List<Admin> admin;


    @Data
    public static  class Admin{
         //管理员的userid
        private String userid;
        //管理员的open_userid
        private String open_userid;
        //该管理员对应用的权限：0=发消息权限，1=管理权限
        private Integer auth_type;
    }
}
