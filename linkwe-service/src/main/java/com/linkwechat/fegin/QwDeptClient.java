package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.fallback.QwDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
     * 获取部门列表
     *
     * @param query
     * @return
     */
    @PostMapping("/dept/get")
    AjaxResult<WeDeptInfoVo> getDeptDetail(@RequestBody WeDeptQuery query);

}
