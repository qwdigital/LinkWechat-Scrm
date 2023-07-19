package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.fegin.QwSysDeptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Component
@Slf4j
public class QwSysDeptFallbackFactory implements QwSysDeptClient {
    @Override
    public AjaxResult<List<SysDept>> findSysDeptByIds(@RequestParam(value = "deptIds") String deptIds) {
        return null;
    }

    @Override
    public AjaxResult<List<SysDeptVo>> getListByDeptIds(SysDeptQuery query) {
        return null;
    }

    @Override
    public AjaxResult addDept(SysDeptQuery query) {
        return null;
    }

    @Override
    public AjaxResult delDept(SysDeptQuery query) {
        return null;
    }

    @Override
    public AjaxResult updateDept(SysDeptQuery query) {
        return null;
    }

    @Override
    public AjaxResult<List<SysDept>> getListWithOutPermission(SysDept query) {
        return null;
    }
}
