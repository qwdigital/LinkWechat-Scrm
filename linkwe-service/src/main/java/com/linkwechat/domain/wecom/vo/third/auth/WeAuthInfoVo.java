package com.linkwechat.domain.wecom.vo.third.auth;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @description 授权信息
 * @date 2022/3/4 11:34
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class WeAuthInfoVo extends WeResultVo {

    /**
     * 授权方（企业）access_token
     */
    private String accessToken;
    /**
     * 有效期
     */
    private Long expiresIn;

    /**
     * 企业微信永久授权码,最长为512字节
     */
    private String permanentCode;

    /**
     * 代理服务商企业信息
     */
    private DealerCorpInfo dealerCorpInfo;

    /**
     * 授权方企业信息
     */
    private WeAuthCorpInfoVo authCorpInfo;

    /**
     * 授权的应用信息
     */
    private AuthInfo authInfo;

    /**
     * 授权管理员的信息
     */
    private AuthUserInfo authUserInfo;

    /**
     * 推广二维码安装相关信息，扫推广二维码安装时返回
     */
    private RegisterCodeInfo registerCodeInfo;

    @Data
    public static class DealerCorpInfo {
        /**
         * 代理服务商企业微信id
         */
        private String corpId;
        /**
         * 代理服务商企业微信名称
         */
        private String corpName;
    }

    @Data
    public static class AuthInfo{
        private List<WeAuthAgentInfoVo> agent;
    }

    @Data
    public static class AuthUserInfo{
        /**
         * 管理员的userid
         */
        private String userId;

        private String openUserId;

        private String name;

        private String avatar;
    }

    @Data
    public static class RegisterCodeInfo{
        /**
         * 注册码
         */
        private String registerCode;
        /**
         * 推广包ID
         */
        private String templateId;

        /**
         * 仅当获取注册码指定该字段时才返回
         */
        private String state;
    }

    /**
     * 安装应用时，扫码或者授权链接中带的state值
     */
    private String state;
}
