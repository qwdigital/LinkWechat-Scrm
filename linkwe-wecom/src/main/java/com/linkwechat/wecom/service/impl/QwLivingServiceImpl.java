package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.living.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.living.*;
import com.linkwechat.wecom.client.WeLivingClient;
import com.linkwechat.wecom.service.IQwLivingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 直播业务类
 * @author danmo
 * @date 2022年10月12日 11:29
 */
@Service
public class QwLivingServiceImpl implements IQwLivingService {

    @Resource
    private WeLivingClient weLivingClient;

    @Override
    public WeAddLivingVo create(WeAddLivingQuery query) {
        return weLivingClient.createLiving(query);
    }

    @Override
    public WeResultVo modifyLiving(WeModifyLivingQuery query) {
        return weLivingClient.modifyLiving(query);
    }

    @Override
    public WeResultVo cancelLiving(WeLivingQuery query) {
        return weLivingClient.cancelLiving(query);
    }

    @Override
    public WeResultVo deleteReplayData(WeLivingQuery query) {
        return weLivingClient.deleteReplayData(query);
    }

    @Override
    public WeGetLivingCodeVo getLivingCode(WeGetLivingCodeQuery query) {
        return weLivingClient.getLivingCode(query);
    }

    @Override
    public WeLivingIdListVo getUserAllLivingId(WeGetUserAllLivingIdQuery query) {
        return weLivingClient.getUserAllLivingId(query);
    }

    @Override
    public WeLivingInfoVo getLivingInfo(WeLivingQuery query) {
        return weLivingClient.getLivingInfo(query);
    }

    @Override
    public WeLivingStatInfoVo getWatchStat(WeLivingQuery query) {
        return weLivingClient.getWatchStat(query);
    }

    @Override
    public WeGetLivingShareInfoVo getLivingShareInfo(WeGetLivingShareInfoQuery query) {
        return weLivingClient.getLivingShareInfo(query);
    }
}
