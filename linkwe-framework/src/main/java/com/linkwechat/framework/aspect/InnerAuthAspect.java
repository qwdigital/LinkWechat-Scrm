package com.linkwechat.framework.aspect;

import cn.hutool.core.util.StrUtil;
import com.linkwechat.common.constant.SecurityConstants;
import com.linkwechat.common.exception.InnerAuthException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.annotation.InnerAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 内部服务调用验证处理
 *
 * @author xueyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered
{
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable
    {
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConstants.INNER, source))
        {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String corpId = ServletUtils.getRequest().getHeader(SecurityConstants.CORP_ID);
        String corpName = ServletUtils.getRequest().getHeader(SecurityConstants.CORP_NAME);
        String userId = ServletUtils.getRequest().getHeader(SecurityConstants.USER_ID);
        String userName = ServletUtils.getRequest().getHeader(SecurityConstants.USER_NAME);
        String userType = ServletUtils.getRequest().getHeader(SecurityConstants.USER_TYPE);
        // 用户信息验证
        if (innerAuth.isUser() && (StrUtil.hasBlank(corpId, corpName, userId, userName, userType))) {
            throw new InnerAuthException("没有设置用户信息，不允许访问");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
