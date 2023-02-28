package com.linkwechat.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.constant.AuthorityConstants;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.constant.TokenConstants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全服务工具类
 * 
 * @author ruoyi
 */
public class SecurityUtils
{

    /**
     * 获取企业Id
     * @return
     */
    public static String getCorpId() {
        return SecurityContextHolder.getCorpId();
    }

    /**
     * 获取企业名称
     * @return
     */
    public static String getCorpName() {
        return SecurityContextHolder.getCorpName();
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户
     */
    public static String getUserName() {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType() {
        return SecurityContextHolder.getUserType();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.Details.LOGIN_USER.getCode(), LoginUser.class);
    }

    public static WxLoginUser getWxLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.Details.LOGIN_USER.getCode(), WxLoginUser.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为超管用户
     *
     * @return 结果
     */
    public static boolean isAdminUser() {
        return ObjectUtil.equal(AuthorityConstants.UserType.ADMIN.getCode(),getUserType());
        //return StringUtils.equals(AuthorityConstants.UserType.ADMIN.getCode(), getUserType());
    }

    /**
     * 是否为超管租户
     *
     * @return 结果
     */
    public static boolean isAdminTenant() {
       // return StringUtils.equals(AuthorityConstants.TenantType.ADMIN.getCode(), getIsLessor());
        return StringUtils.equals(AuthorityConstants.TenantType.ADMIN.getCode(), null);
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
