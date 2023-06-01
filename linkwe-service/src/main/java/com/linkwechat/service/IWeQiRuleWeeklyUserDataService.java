package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeQiRuleWeeklyUserData;
import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyDetailListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyDetailListVo;

import java.util.List;

/**
 * 会话质检周报员工数据表(WeQiRuleWeeklyUserData)
 *
 * @author danmo
 * @since 2023-05-18 17:36:35
 */
public interface IWeQiRuleWeeklyUserDataService extends IService<WeQiRuleWeeklyUserData> {

    List<WeQiRuleWeeklyDetailListVo> getWeeklyDetailList(WeQiRuleWeeklyDetailListQuery query);
}
