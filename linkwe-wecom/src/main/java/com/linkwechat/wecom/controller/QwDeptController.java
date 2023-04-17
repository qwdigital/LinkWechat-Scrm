package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.wecom.service.IQwDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/3/23 21:08
 */
@Slf4j
@RestController
@RequestMapping("dept")
public class QwDeptController {
    @Resource
    private IQwDeptService deptService;

    /**
     * 获取部门列表
     *
     * @param query
     * @return
     */
    @PostMapping("/list")
    public AjaxResult<WeDeptVo> getDeptList(@RequestBody WeDeptQuery query) {
        WeDeptVo weDeptList = deptService.getDeptList(query);
        return AjaxResult.success(weDeptList);
    }

    /**
     * 获取部门ID列表
     *
     * @param query
     * @return
     */
    @PostMapping("/simplelist")
    public AjaxResult<WeDeptIdVo> getDeptSimpleList(@RequestBody WeDeptQuery query) {
        WeDeptIdVo weDeptList = deptService.getDeptSimpleList(query);
        return AjaxResult.success(weDeptList);
    }

    /**
     * 获取部门详情
     *
     * @param query
     * @return
     */
    @PostMapping("/get")
    public AjaxResult<WeDeptInfoVo> getDeptDetail(@RequestBody WeDeptQuery query) {
        WeDeptInfoVo weDeptList = deptService.getDeptDetail(query);
        return AjaxResult.success(weDeptList);
    }
}
