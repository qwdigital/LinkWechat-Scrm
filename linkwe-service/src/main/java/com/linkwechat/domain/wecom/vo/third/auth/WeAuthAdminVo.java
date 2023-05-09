package com.linkwechat.domain.wecom.vo.third.auth;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @description 获取预授权码
 * @date 2022/3/4 11:34
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class WeAuthAdminVo extends WeResultVo {
    /**
     * 预授权码,最长为512字节
     */
    private List<Admin> admin;

    @Data
    public static class Admin{
        /**
         * 管理员的userid
         */
        private String userId;

        /**
         * 管理员的open_userid
         */
        private String openUserId;

        /**
         * 该管理员对应用的权限：0=发消息权限，1=管理权限
         */
        private Integer authType;
    }
}
