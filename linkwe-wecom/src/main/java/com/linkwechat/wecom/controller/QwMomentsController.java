package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.service.IQwMomentsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author danmo
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

    /**
     * 停止朋友圈任务
     *
     * @param momentsCancelDTO 停止朋友圈任务参数
     * @return {@link AjaxResult< WeResultVo>}
     * @author WangYX
     * @date 2023/06/12 10:33
     */
    @PostMapping("/cancel_moment_task")
    public AjaxResult<WeResultVo> cancel_moment_task(@RequestBody MomentsCancelDTO momentsCancelDTO) {
        return AjaxResult.success(qwMomentsService.cancelMomentTask(momentsCancelDTO));
    }


    /**
     * 获取客户朋友圈发表后的可见客户列表
     *
     * @param momentsSendResultDTO 获取客户朋友圈发表后的可见客户列表
     * @return {@link AjaxResult< WeResultVo>}
     * @author WangYX
     * @date 2023/06/12 10:33
     */
    @PostMapping("/get_moment_send_result")
    public AjaxResult<WeResultVo> get_moment_send_result(@RequestBody MomentsSendResultDTO momentsSendResultDTO) {
        return AjaxResult.success(qwMomentsService.getMomentSendResult(momentsSendResultDTO));
    }


}
