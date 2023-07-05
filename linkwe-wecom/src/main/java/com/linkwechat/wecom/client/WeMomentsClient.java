package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.moments.vo.MomentsSendResultVO;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * 朋友圈
 */
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeMomentsClient {


    /**
     * 创建朋友圈
     *
     * @param momentsParamDto
     * @return
     */
    @Post("/externalcontact/add_moment_task")
    MomentsResultDto addMomentTask(@JSONBody MomentsParamDto momentsParamDto);


    /**
     * 获取任务创建结果
     *
     * @param jobid
     * @return
     */
    @Get("/externalcontact/get_moment_task_result")
    MomentsCreateResultDto getMomentTaskResult(@Query("jobid") String jobid);


    /**
     * 朋友圈详情数据获取
     *
     * @param momentsListDetailParamDto
     * @return
     */
    @Post("/externalcontact/get_moment_list")
    MomentsListDetailResultDto get_moment_list(@JSONBody MomentsListDetailParamDto momentsListDetailParamDto);


    /**
     * 朋友圈互动数据获取
     *
     * @param momentsInteracteParamDto
     * @return
     */
    @Post("/externalcontact/get_moment_comments")
    MomentsInteracteResultDto get_moment_comments(@JSONBody MomentsInteracteParamDto momentsInteracteParamDto);


    /**
     * 获取客户朋友圈企业发表的列表
     *
     * @param momentsParamDto
     * @return
     */
    @Post("/externalcontact/get_moment_task")
    MomentsResultDto get_moment_task(@JSONBody MomentsParamDto momentsParamDto);


    /**
     * 获取客户朋友圈发表时选择的可见范围
     *
     * @param momentsParamDto
     * @return
     */
    @Post("/externalcontact/get_moment_customer_list")
    MomentsResultDto get_moment_customer_list(@JSONBody MomentsParamDto momentsParamDto);

    /**
     * 停止发表企业朋友圈
     *
     * @param dto 停止发表企业朋友圈参数
     * @return {@link WeResultVo}
     * @author WangYX
     * @date 2023/06/12 10:30
     */
    @Post("/externalcontact/cancel_moment_task")
    WeResultVo cancel_moment_task(@JSONBody MomentsCancelDTO dto);


    /**
     * 获取客户朋友圈发表后的可见客户列表
     *
     * @param dto 获取客户朋友圈发表后的可见客户列表
     * @return {@link WeResultVo}
     * @author WangYX
     * @date 2023/06/12 10:30
     */
    @Post("/externalcontact/get_moment_send_result")
    MomentsSendResultVO get_moment_send_result(@JSONBody MomentsSendResultDTO dto);
}
