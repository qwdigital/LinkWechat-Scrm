package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeLxQrCodeLog;
import com.linkwechat.mapper.WeLxQrCodeLogMapper;
import com.linkwechat.service.IWeLxQrCodeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 拉新活码领取记录表(WeLxQrCodeLog)
 *
 * @author danmo
 * @since 2023-03-16 16:19:03
 */
@Service
public class WeLxQrCodeLogServiceImpl extends ServiceImpl<WeLxQrCodeLogMapper, WeLxQrCodeLog> implements IWeLxQrCodeLogService {

}
