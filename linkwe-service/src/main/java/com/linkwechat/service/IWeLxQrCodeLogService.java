package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeLxQrCodeLog;
import com.linkwechat.domain.qr.query.WeLxQrCodeQuery;
import com.linkwechat.domain.qr.vo.WeLxQrCodeReceiveListVo;
import com.linkwechat.domain.qr.vo.WeLxQrCodeReceiveVo;

import java.util.List;

/**
 * 拉新活码领取记录表(WeLxQrCodeLog)
 *
 * @author danmo
 * @since 2023-03-16 16:19:03
 */
public interface IWeLxQrCodeLogService extends IService<WeLxQrCodeLog> {

    List<WeLxQrCodeReceiveListVo> receiveListStatistics(WeLxQrCodeQuery query);

    WeLxQrCodeReceiveVo receiveTotalStatistics(WeLxQrCodeQuery query);
}
