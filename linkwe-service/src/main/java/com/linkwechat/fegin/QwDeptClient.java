package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.fallback.QwDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author leejoker
 * @date 2022/3/29 23:00
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwDeptFallbackFactory.class, contextId = "linkwe-wecom-dept")
public interface QwDeptClient {
    /**
     * 获取部门列表
     *
     * @param query
     * @return
     */
    @PostMapping("/dept/list")
    AjaxResult<WeDeptVo> getDeptList(@RequestBody WeDeptQuery query);

    /**
     * 获取部门ID列表
     *
     * @param query
     * @return
     */
    @PostMapping("/simplelist")
    AjaxResult<WeDeptIdVo> getDeptSimpleList(@RequestBody WeDeptQuery query);
}
