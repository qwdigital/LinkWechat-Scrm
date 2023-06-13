package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.domain.qirule.query.WeQiRuleNoticeListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleNoticeListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeQiRuleMsgNotice;

/**
 * 质检规则通知表(WeQiRuleMsgNotice)
 *
 * @author danmo
 * @since 2023-05-10 09:51:51
 */
@Repository()
@Mapper
public interface WeQiRuleMsgNoticeMapper extends BaseMapper<WeQiRuleMsgNotice> {


    List<WeQiRuleNoticeListVo> getNoticeList(WeQiRuleNoticeListQuery query);

}

