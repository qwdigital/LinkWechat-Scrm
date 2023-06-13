package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeQiRuleMsgNotice;
import com.linkwechat.domain.qirule.query.WeQiRuleNoticeListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleNoticeListVo;

import java.util.List;

/**
 * 质检规则通知表(WeQiRuleMsgNotice)
 *
 * @author danmo
 * @since 2023-05-10 09:51:51
 */
public interface IWeQiRuleMsgNoticeService extends IService<WeQiRuleMsgNotice> {

    List<WeQiRuleNoticeListVo> getNoticeList(WeQiRuleNoticeListQuery query);
}
