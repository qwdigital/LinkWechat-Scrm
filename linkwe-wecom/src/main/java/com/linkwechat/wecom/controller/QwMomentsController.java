package com.linkwechat.wecom.controller;

import com.dtflys.forest.annotation.PostRequest;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.wecom.service.IQwMomentsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @date 2022/4/29 21:49
 */
@RestController
@RequestMapping("moments")
public class QwMomentsController {
    @Resource
    private IQwMomentsService qwMomentsService;

    /**
     * 创建朋友圈
     *
     * @param momentsParamDto
     * @return
     */
    @PostMapping
    public AjaxResult<MomentsResultDto> addMomentTask(@RequestBody MomentsParamDto momentsParamDto) {
        return AjaxResult.success(qwMomentsService.addMomentTask(momentsParamDto));
    }


    /**
     * 获取任务创建结果
     *
     * @param jobid
     * @return
     */
    @GetMapping("/{jobid}")
    public AjaxResult<MomentsCreateResultDto> getMomentTaskResult(@PathVariable("jobid") String jobid) {
        return AjaxResult.success(qwMomentsService.getMomentTaskResult(jobid));
    }


    /**
     * 朋友圈详情数据获取
     *
     * @param momentsListDetailParamDto
     * @return
     */
    @PostMapping("/list")
    public AjaxResult<MomentsListDetailResultDto> momentList(
            @RequestBody MomentsListDetailParamDto momentsListDetailParamDto) {
        return AjaxResult.success(qwMomentsService.getMomentList(momentsListDetailParamDto));
    }


    /**
     * 朋友圈互动数据获取
     *
     * @param momentsInteracteParamDto
     * @return
     */
    @PostMapping("/comments")
    public AjaxResult<MomentsInteracteResultDto> comments(
            @RequestBody MomentsInteracteParamDto momentsInteracteParamDto) {
        return AjaxResult.success(qwMomentsService.getMomentComments(momentsInteracteParamDto));
    }


    /**
     * 获取客户朋友圈企业发表的列表
     *
     * @param momentsParamDto
     * @return
     */
    @PostMapping("/get_moment_task")
    public AjaxResult<MomentsResultDto> get_moment_task(@RequestBody MomentsParamDto momentsParamDto) {
        return AjaxResult.success(qwMomentsService.getMomentTask(momentsParamDto));
    }


    /**
     * 获取客户朋友圈发表时选择的可见范围
     *
     * @param momentsParamDto
     * @return
     */
    @PostMapping("/get_moment_customer_list")
    public AjaxResult<MomentsResultDto> get_moment_customer_list(@RequestBody MomentsParamDto momentsParamDto) {
        return AjaxResult.success(qwMomentsService.getMomentCustomerList(momentsParamDto));
    }
}
