package com.linkwechat.wecom.service;

import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;

/**
 * @author leejoker
 * @date 2022/5/2 18:25
 */
public interface IQwMomentsService {
    /**
     * 创建朋友圈
     *
     * @param momentsParamDto
     * @return
     */
    MomentsResultDto addMomentTask(MomentsParamDto momentsParamDto);


    /**
     * 获取任务创建结果
     *
     * @param jobid
     * @return
     */
    MomentsCreateResultDto getMomentTaskResult(String jobid);


    /**
     * 朋友圈详情数据获取
     *
     * @param momentsListDetailParamDto
     * @return
     */
    MomentsListDetailResultDto getMomentList(MomentsListDetailParamDto momentsListDetailParamDto);


    /**
     * 朋友圈互动数据获取
     *
     * @param momentsInteracteParamDto
     * @return
     */
    MomentsInteracteResultDto getMomentComments(MomentsInteracteParamDto momentsInteracteParamDto);


    /**
     * 获取客户朋友圈企业发表的列表
     *
     * @param momentsParamDto
     * @return
     */
    MomentsResultDto getMomentTask(MomentsParamDto momentsParamDto);


    /**
     * 获取客户朋友圈发表时选择的可见范围
     *
     * @param momentsParamDto
     * @return
     */
    MomentsResultDto getMomentCustomerList(MomentsParamDto momentsParamDto);

    /**
     * 停止发表企业朋友圈
     *
     * @param cancelMomentTaskDto
     * @return
     */
    WeResultVo cancel_moment_task(CancelMomentTaskDto cancelMomentTaskDto);
}
