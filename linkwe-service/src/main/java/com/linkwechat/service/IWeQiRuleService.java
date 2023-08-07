package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeQiRule;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import com.linkwechat.domain.qirule.query.*;
import com.linkwechat.domain.qirule.vo.*;

import java.util.List;

/**
 * 质检规则表(WeQiRule)
 *
 * @author danmo
 * @since 2023-05-05 16:57:30
 */
public interface IWeQiRuleService extends IService<WeQiRule> {

    /**
     * 新增质检规则
     *
     * @param query
     */
    void addQiRule(WeQiRuleAddQuery query);

    /**
     * 编辑质检规则
     *
     * @param query
     */
    void updateQiRule(WeQiRuleAddQuery query);

    /**
     * 质检规则详情
     *
     * @param id 规则ID
     */
    WeQiRuleDetailVo getQiRuleDetail(Long id);


    PageInfo<WeQiRuleListVo> getQiRulePageList(WeQiRuleListQuery query);
    /**
     * 质检规则列表
     *
     * @param query
     */
    List<WeQiRuleListVo> getQiRuleList(WeQiRuleListQuery query);

    /**
     * 删除质检规则
     *
     * @param ids 规则ID
     */
    void delQiRule(List<Long> ids);

    /**
     * 根据userId查询规则列表
     *
     * @param query
     * @return
     */
    List<WeQiRuleListVo> getQiRuleListByUserId(WeQiRuleListQuery query);

    /**
     * 质检规则概览统计
     *
     * @param id
     */
    WeQiRuleStatisticsViewVo qiRuleViewStatistics(Long id);

    /**
     * 质检规则统计列表查询
     *
     * @param query
     */
    List<WeQiRuleStatisticsTableVo> qiRuleTableStatistics(WeQiRuleStatisticsTableListQuery query);

    /**
     * 质检规则统计列表消息记录
     *
     * @param query
     * @return
     */
    List<WeChatContactMsgVo> getQiRuleTableStatisticsMsg(WeQiRuleStatisticsTableMsgQuery query);

    /**
     * 质检通知列表
     *
     * @param query
     */
    List<WeQiRuleNoticeListVo> getNoticeList(WeQiRuleNoticeListQuery query);

    /**
     * 质检通知设置回复状态
     *
     * @param qiRuleMsgId 质检消息ID
     */
    void updateReplyStatus(Long qiRuleMsgId);

    /**
     * 质检周报列表
     *
     * @param query
     * @return
     */
    List<WeQiRuleWeeklyListVo> getWeeklyList(WeQiRuleWeeklyListQuery query);

    /**
     * 质检周报详情数据
     *
     * @param id
     * @return
     */
    WeQiRuleWeeklyDetailVo getWeeklyDetail(Long id);

    List<WeQiRuleWeeklyDetailListVo> getWeeklyDetailList(WeQiRuleWeeklyDetailListQuery query);


}
