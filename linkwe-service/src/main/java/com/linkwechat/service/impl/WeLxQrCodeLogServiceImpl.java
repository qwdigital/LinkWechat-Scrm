package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeLxQrCodeLog;
import com.linkwechat.domain.qr.query.WeLxQrCodeQuery;
import com.linkwechat.domain.qr.vo.WeLxQrCodeReceiveListVo;
import com.linkwechat.domain.qr.vo.WeLxQrCodeReceiveVo;
import com.linkwechat.mapper.WeLxQrCodeLogMapper;
import com.linkwechat.service.IWeLxQrCodeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 拉新活码领取记录表(WeLxQrCodeLog)
 *
 * @author danmo
 * @since 2023-03-16 16:19:03
 */
@Service
public class WeLxQrCodeLogServiceImpl extends ServiceImpl<WeLxQrCodeLogMapper, WeLxQrCodeLog> implements IWeLxQrCodeLogService {

    @Override
    public List<WeLxQrCodeReceiveListVo> receiveListStatistics(WeLxQrCodeQuery query) {
        return this.baseMapper.receiveListStatistics(query);
    }

    @Override
    public WeLxQrCodeReceiveVo receiveTotalStatistics(WeLxQrCodeQuery query) {
        return this.baseMapper.receiveTotalStatistics(query);
    }
}
