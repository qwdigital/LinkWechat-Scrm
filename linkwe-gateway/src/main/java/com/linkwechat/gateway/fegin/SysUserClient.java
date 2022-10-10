package com.linkwechat.gateway.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author leejoker
 * @date 2022/3/29 23:00
 */
@FeignClient(value = "${wecom.serve.linkwe-auth}")
public interface SysUserClient {
    /**
     * 获取用户信息
     *
     * @return LoginUser
     */
    @GetMapping("/system/user/findCurrentLoginUser")
    AjaxResult<LoginUser> findCurrentLoginUser();
}
