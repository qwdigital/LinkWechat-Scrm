package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeChatContactSensitiveMsg;
import com.linkwechat.domain.msgaudit.query.WeSensitiveHitQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactSensitiveMsgVo;

import java.util.List;

/**
 * 会话触发敏感词记录(WeChatContactSensitiveMsg)
 *
 * @author danmo
 * @since 2022-06-10 10:38:47
 */
public interface IWeChatContactSensitiveMsgService extends IService<WeChatContactSensitiveMsg> {

    List<WeChatContactSensitiveMsgVo> getListByQuery(WeSensitiveHitQuery query);
}
