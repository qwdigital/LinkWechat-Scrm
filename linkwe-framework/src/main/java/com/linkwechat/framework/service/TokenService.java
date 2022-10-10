package com.linkwechat.framework.service;

import com.linkwechat.common.constant.CacheConstants;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.domain.model.WxLoginUser;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.JwtUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.ip.IpUtils;
import com.linkwechat.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author danmo
 */
@Component
public class TokenService {

    @Autowired
    private RedisService redisService;

    protected static final Integer MILLIS_SECOND = 1000;

    protected static final Integer MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static Integer expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Integer MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();

        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        String userType = loginUser.getSysUser().getUserType();
        String corpId = loginUser.getCorpId();
        String corpName = loginUser.getCorpName();
        loginUser.setToken(token);
        loginUser.setUserId(userId);
        loginUser.setUserName(userName);
        loginUser.setUserType(userType);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.CORP_ID, corpId);
        claimsMap.put(SecurityConstants.CORP_NAME, corpName);
        claimsMap.put(SecurityConstants.USER_ID, userId);
        claimsMap.put(SecurityConstants.USER_NAME, userName);
        claimsMap.put(SecurityConstants.USER_TYPE, userType);
        claimsMap.put(SecurityConstants.LOGIN_TYPE,"LinkWeChatAPI");


        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userKey = JwtUtils.getUserKey(token);
                return redisService.getCacheObject(getTokenKey(userKey));
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userKey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser 用户信息
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }


    public Map<String, Object> createToken(WxLoginUser wxLoginUser) {
        wxLoginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(wxLoginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, wxLoginUser.getOpenId());
        claimsMap.put(SecurityConstants.LOGIN_TYPE,"LinkWeChatWXAPI");
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", wxLoginUser.getExpireTime());
        return rspMap;
    }

    private void refreshToken(WxLoginUser wxLoginUser) {
        wxLoginUser.setLoginTime(System.currentTimeMillis());
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(wxLoginUser.getOpenId());
        redisService.setCacheObject(userKey, wxLoginUser, wxLoginUser.getExpireTime().intValue(), TimeUnit.SECONDS);
    }

    public WxLoginUser getWxLoginToken(String openId){
        String userKey = getTokenKey(openId);
        Object cacheObject = redisService.getCacheObject(userKey);
        if(cacheObject != null){
            return (WxLoginUser) cacheObject;
        }else {
            return null;
        }
    }
}
