package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.living.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.living.*;
import com.linkwechat.fegin.QwLivingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 直播
 * @date 2022/3/13 20:57
 **/
@Component
@Slf4j
public class QwLivingFallbackFactory implements QwLivingClient {


    @Override
    public AjaxResult<WeAddLivingVo> create(WeAddLivingQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> modifyLiving(WeModifyLivingQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> cancelLiving(WeLivingQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> deleteReplayData(WeLivingQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGetLivingCodeVo> getLivingCode(WeGetLivingCodeQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLivingIdListVo> getUserAllLivingId(WeGetUserAllLivingIdQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLivingInfoVo> getLivingInfo(WeLivingQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLivingStatInfoVo> getWatchStat(WeLivingQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGetLivingShareInfoVo> getLivingShareInfo(WeGetLivingShareInfoQuery query) {
        return null;
    }
}
