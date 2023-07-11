package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.moments.dto.*;
import com.linkwechat.domain.moments.vo.MomentsSendResultVO;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.client.WeMomentsClient;
import com.linkwechat.wecom.service.IQwMomentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @date 2022/5/2 18:27
 */
@Service
@Slf4j
public class QwMomentsServiceImpl implements IQwMomentsService {
    @Resource
    private WeMomentsClient weMomentsClient;

    @Override
    public MomentsResultDto addMomentTask(MomentsParamDto momentsParamDto) {
        return weMomentsClient.addMomentTask(momentsParamDto);
    }

    @Override
    public MomentsCreateResultDto getMomentTaskResult(String jobid) {
        return weMomentsClient.getMomentTaskResult(jobid);
    }

    @Override
    public MomentsListDetailResultDto getMomentList(MomentsListDetailParamDto momentsListDetailParamDto) {
        return weMomentsClient.get_moment_list(momentsListDetailParamDto);
    }

    @Override
    public MomentsInteracteResultDto getMomentComments(MomentsInteracteParamDto momentsInteracteParamDto) {
        return weMomentsClient.get_moment_comments(momentsInteracteParamDto);
    }

    @Override
    public MomentsResultDto getMomentTask(MomentsParamDto momentsParamDto) {
        return weMomentsClient.get_moment_task(momentsParamDto);
    }

    @Override
    public MomentsResultDto getMomentCustomerList(MomentsParamDto momentsParamDto) {
        return weMomentsClient.get_moment_customer_list(momentsParamDto);
    }

    @Override
    public WeResultVo cancelMomentTask(MomentsCancelDTO dto) {
        return weMomentsClient.cancel_moment_task(dto);
    }

    @Override
    public MomentsSendResultVO getMomentSendResult(MomentsSendResultDTO dto) {
        return weMomentsClient.get_moment_send_result(dto);
    }
}
