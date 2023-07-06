package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.vo.SysAreaVo;
import com.linkwechat.fallback.QwSysAreaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author danmo
 * @date 2022/4/29 22:58
 */
@FeignClient(value = "${wecom.serve.linkwe-auth}", fallback = QwSysAreaFallbackFactory.class, contextId = "linkwe-auth-user")
public interface QwSysAreaClient {

    @GetMapping("/system/area/getChildListById")
    AjaxResult<List<SysAreaVo>> getChildListById(@RequestParam("id") Integer id);



    /**
     * 根据区域Id，获取区域数据
     *
     * @param id
     * @return {@link AjaxResult< SysAreaVo>}
     * @author WangYX
     * @date 2022/10/17 14:39
     */
    @GetMapping("/system/area/getAreaById/{id}")
    AjaxResult<SysAreaVo> getAreaById(@PathVariable("id") Integer id);

}
