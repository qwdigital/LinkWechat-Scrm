package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeAiMsg;
import com.linkwechat.mapper.WeAiMsgMapper;
import com.linkwechat.service.IWeAiMsgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ai助手消息表(WeAiMsg)
 *
 * @author makejava
 * @since 2023-12-01 15:12:13
 */
@Service
public class WeAiMsgServiceImpl extends ServiceImpl<WeAiMsgMapper, WeAiMsg> implements IWeAiMsgService {


    @Override
    public List<WeAiMsg> getSessionList(Long userId, String content) {
        return this.baseMapper.getSessionList(userId,content);
    }
}
