package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.fegin.QwMomentsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author leejoker
 * @date 2022/4/29 22:01
 */
@Component
@Slf4j
public class QwMomentsFallbackFactory implements QwMomentsClient {
    @Override
    public AjaxResult<MomentsResultDto> addMomentTask(MomentsParamDto momentsParamDto) {
        return null;
    }

    @Override
    public AjaxResult<MomentsCreateResultDto> getMomentTaskResult(String jobid) {
        return null;
    }

    @Override
    public AjaxResult<MomentsListDetailResultDto> momentList(MomentsListDetailParamDto momentsListDetailParamDto) {
        return null;
    }

    @Override
    public AjaxResult<MomentsInteracteResultDto> comments(MomentsInteracteParamDto momentsInteracteParamDto) {
        return null;
    }

    @Override
    public AjaxResult<MomentsResultDto> get_moment_task(MomentsParamDto momentsParamDto) {
        return null;
    }

    @Override
    public AjaxResult<MomentsResultDto> get_moment_customer_list(MomentsParamDto momentsParamDto) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> cancel_moment_task(CancelMomentTaskDto cancelMomentTaskDto) {
        return null;
    }
}
