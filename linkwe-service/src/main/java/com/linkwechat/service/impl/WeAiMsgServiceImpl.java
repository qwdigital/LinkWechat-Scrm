package com.linkwechat.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeAiMsgMapper;
import com.linkwechat.domain.WeAiMsg;
import com.linkwechat.service.IWeAiMsgService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ai助手消息表(WeAiMsg)
 *
 * @author makejava
 * @since 2023-12-01 15:12:13
 */
@Service
public class WeAiMsgServiceImpl extends ServiceImpl<WeAiMsgMapper, WeAiMsg> implements IWeAiMsgService {

public WeAiMsgServiceImpl() {}

@Autowired
private WeAiMsgMapper weAiMsgMapper; 
}
