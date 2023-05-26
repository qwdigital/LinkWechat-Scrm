package com.linkwechat.service.impl;

import com.linkwechat.domain.qirule.query.WeQiRuleNoticeListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleNoticeListVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeQiRuleMsgNoticeMapper;
import com.linkwechat.domain.WeQiRuleMsgNotice;
import com.linkwechat.service.IWeQiRuleMsgNoticeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 质检规则通知表(WeQiRuleMsgNotice)
 *
 * @author danmo
 * @since 2023-05-10 09:51:51
 */
@Service
public class WeQiRuleMsgNoticeServiceImpl extends ServiceImpl<WeQiRuleMsgNoticeMapper, WeQiRuleMsgNotice> implements IWeQiRuleMsgNoticeService {


    @Override
    public List<WeQiRuleNoticeListVo> getNoticeList(WeQiRuleNoticeListQuery query) {
       return this.baseMapper.getNoticeList(query);
    }
}
