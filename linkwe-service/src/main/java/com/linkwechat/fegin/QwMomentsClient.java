package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.moments.vo.MomentsSendResultVO;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.fallback.QwMomentsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @date 2022/4/29 22:00
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwMomentsFallbackFactory.class)
public interface QwMomentsClient {
    /**
     * 创建朋友圈
     *
     * @param momentsParamDto
     * @return
     */
    @PostMapping("/moments")
    AjaxResult<MomentsResultDto> addMomentTask(@RequestBody MomentsParamDto momentsParamDto);


    /**
     * 获取任务创建结果
     *
     * @param jobid
     * @return
     */
    @GetMapping("/moments/{jobid}")
    AjaxResult<MomentsCreateResultDto> getMomentTaskResult(@PathVariable("jobid") String jobid);


    /**
     * 朋友圈详情数据获取
     *
     * @param momentsListDetailParamDto
     * @return
     */
    @PostMapping("/moments/list")
    AjaxResult<MomentsListDetailResultDto> momentList(@RequestBody MomentsListDetailParamDto momentsListDetailParamDto);


    /**
     * 朋友圈互动数据获取
     *
     * @param momentsInteracteParamDto
     * @return
     */
    @PostMapping("/moments/comments")
    AjaxResult<MomentsInteracteResultDto> comments(@RequestBody MomentsInteracteParamDto momentsInteracteParamDto);


    /**
     * 获取客户朋友圈企业发表的列表
     *
     * @param momentsParamDto
     * @return
     */
    @PostMapping("/moments/get_moment_task")
    AjaxResult<MomentsResultDto> get_moment_task(@RequestBody MomentsParamDto momentsParamDto);


    /**
     * 获取客户朋友圈发表时选择的可见范围
     *
     * @param momentsParamDto
     * @return
     */
    @PostMapping("/moments/get_moment_customer_list")
    AjaxResult<MomentsResultDto> get_moment_customer_list(@RequestBody MomentsParamDto momentsParamDto);


    /**
     * 停止朋友圈任务
     *
     * @param momentsCancelDTO 停止朋友圈任务参数
     * @return {@link AjaxResult<WeResultVo>}
     * @author WangYX
     * @date 2023/06/12 10:33
     */
    @PostMapping("/moments/cancel_moment_task")
    AjaxResult<WeResultVo> cancel_moment_task(@RequestBody MomentsCancelDTO momentsCancelDTO);


    /**
     * 获取客户朋友圈发表后的可见客户列表
     *
     * @param momentsSendResultDTO 获取客户朋友圈发表后的可见客户列表
     * @return {@link AjaxResult<MomentsSendResultVO>}
     * @author WangYX
     * @date 2023/06/12 10:33
     */
    @PostMapping("/moments/get_moment_send_result")
    AjaxResult<MomentsSendResultVO> get_moment_send_result(@RequestBody MomentsSendResultDTO momentsSendResultDTO);

}
