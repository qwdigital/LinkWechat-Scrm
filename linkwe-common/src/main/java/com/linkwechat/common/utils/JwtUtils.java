package com.linkwechat.common.utils;

import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.constant.TokenConstants;
import com.linkwechat.common.core.text.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

/**
 * Jwt工具类
 *
 * @author ruoyi
 */
public class JwtUtils {
    public static String secret = TokenConstants.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取企业Id
     *
     * @param token 令牌
     * @return 企业Id
     */
    public static String getCorpId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.CORP_ID.getCode());
    }

    /**
     * 根据身份信息获取企业Id
     *
     * @param claims 身份信息
     * @return 企业Id
     */
    public static String getCorpId(Claims claims) {
        return getValue(claims, SecurityConstants.Details.CORP_ID.getCode());
    }

    /**
     * 根据令牌获取企业账号
     *
     * @param token 令牌
     * @return 企业账号
     */
    public static String getCorpName(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.CORP_NAME.getCode());
    }

    /**
     * 根据身份信息获取企业账号
     *
     * @param claims 身份信息
     * @return 企业账号
     */
    public static String getCorpName(Claims claims) {
        return getValue(claims, SecurityConstants.Details.CORP_NAME.getCode());
    }


    /**
     * 根据令牌获取租户id
     *
     * @param token 身份信息
     * @return 租户ID
     */
    public static Integer getTenantId(String token) {
        Claims claims = parseToken(token);
        Integer tenantId = getIntValue(claims, SecurityConstants.Details.TENANT_ID.getCode());
        if(tenantId != null){
            return tenantId;
        }else {
            return null;
        }
    }
    /**
     * 根据身份信息获取企业账号
     *
     * @param claims 身份信息
     * @return 企业账号
     */
   public static Integer getTenantId(Claims claims) {
       Integer tenantId = getIntValue(claims, SecurityConstants.Details.TENANT_ID.getCode());
       if(tenantId != null){
           return tenantId;
       }else {
           return null;
       }
    }

    /**
     * 根据令牌获取用户Id
     *
     * @param token 令牌
     * @return 用户Id
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.USER_ID.getCode());
    }

    /**
     * 根据身份信息获取用户Id
     *
     * @param claims 身份信息
     * @return 用户Id
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.Details.USER_ID.getCode());
    }

    /**
     * 根据令牌获取用户账号
     *
     * @param token 令牌
     * @return 用户账号
     */
    public static String getUserName(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.USER_NAME.getCode());
    }

    /**
     * 根据身份信息获取用户账号
     *
     * @param claims 身份信息
     * @return 用户账号
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, SecurityConstants.Details.USER_NAME.getCode());
    }

    /**
     * 根据令牌获取用户信息
     *
     * @param token 令牌
     * @return 用户账号
     */
    public static String getLoginUser(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.LOGIN_USER.getCode());
    }

    /**
     * 根据身份信息获取用户用户信息
     *
     * @param claims 身份信息
     * @return 用户账号
     */
    public static String getLoginUser(Claims claims) {
        return getValue(claims, SecurityConstants.Details.LOGIN_USER.getCode());
    }

    /**
     * 根据身份信息获取用户类型
     *
     * @param claims 身份信息
     * @return 用户类型
     */
    public static String getUserType(Claims claims) {
        return getValue(claims, SecurityConstants.Details.USER_TYPE.getCode());
    }

    /**
     * 根据令牌获取用户类型
     *
     * @param token 令牌
     * @return 用户类型
     */
    public static String getUserType(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.USER_TYPE.getCode());
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户Id
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.Details.USER_KEY.getCode());
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户Id
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.Details.USER_KEY.getCode());
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }
    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static Integer getIntValue(Claims claims, String key) {
        return Convert.toInt(claims.get(key));
    }
}