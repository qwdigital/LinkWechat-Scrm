package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.fegin.QwSysDeptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Component
@Slf4j
public class QwSysDeptFallbackFactory implements QwSysDeptClient {
    @Override
    public AjaxResult<List<SysDept>> findSysDeptByIds(@RequestParam(value = "deptIds")String deptIds) {
        return null;
    }
}
