package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 登录用户
 * @author: HaoN
 * @create: 2020-11-01 20:58
 **/
@Data
public class WeLoginUserInfoDto extends  WeResultDto{
    private Integer usertype;

    private UserInfo user_info;

    @Data
    public class  UserInfo{
        private String userid;
        private String name;
        private String avatar;
    }
}
