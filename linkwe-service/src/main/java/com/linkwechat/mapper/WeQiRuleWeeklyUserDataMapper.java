package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyDetailListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyDetailListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeQiRuleWeeklyUserData;

/**
 * 会话质检周报员工数据表(WeQiRuleWeeklyUserData)
 *
 * @author danmo
 * @since 2023-05-18 17:36:34
 */
@Repository()
@Mapper
public interface WeQiRuleWeeklyUserDataMapper extends BaseMapper<WeQiRuleWeeklyUserData> {


    List<WeQiRuleWeeklyDetailListVo> getWeeklyDetailList(WeQiRuleWeeklyDetailListQuery query);
}

