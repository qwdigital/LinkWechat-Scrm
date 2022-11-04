package com.linkwechat.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeAgentMsgMapper;
import com.linkwechat.domain.WeAgentMsg;
import com.linkwechat.service.IWeAgentMsgService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 应用消息表(WeAgentMsg)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
@Service
public class WeAgentMsgServiceImpl extends ServiceImpl<WeAgentMsgMapper, WeAgentMsg> implements IWeAgentMsgService {

public WeAgentMsgServiceImpl() {}

@Autowired
private WeAgentMsgMapper weAgentMsgMapper; 
}
