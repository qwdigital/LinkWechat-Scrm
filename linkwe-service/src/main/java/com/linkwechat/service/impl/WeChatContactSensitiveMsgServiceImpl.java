package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeChatContactSensitiveMsg;
import com.linkwechat.domain.msgaudit.query.WeSensitiveHitQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactSensitiveMsgVo;
import com.linkwechat.mapper.WeChatContactSensitiveMsgMapper;
import com.linkwechat.service.IWeChatContactSensitiveMsgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会话触发敏感词记录(WeChatContactSensitiveMsg)
 *
 * @author danmo
 * @since 2022-06-10 10:38:47
 */
@Service
public class WeChatContactSensitiveMsgServiceImpl extends ServiceImpl<WeChatContactSensitiveMsgMapper, WeChatContactSensitiveMsg> implements IWeChatContactSensitiveMsgService {

    @Override
    public List<WeChatContactSensitiveMsgVo> getListByQuery(WeSensitiveHitQuery query) {
        return this.baseMapper.getListByQuery(query);
    }
}
