package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.fallback.QwSysDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author danmo
 * @date 2022/11/29 22:58
 */
@FeignClient(value = "${wecom.serve.linkwe-auth}", fallback = QwSysDeptFallbackFactory.class, contextId = "linkwe-auth-dept")
public interface QwSysDeptClient {

    @PostMapping("/system/dept/getListByDeptIds")
    AjaxResult<List<SysDeptVo>> getListByDeptIds(@RequestBody SysDeptQuery query);
}
