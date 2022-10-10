package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.domain.operation.query.WePageStateQuery;
import com.linkwechat.domain.operation.vo.WePageCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeUserBehaviorData;

/**
 * 联系客户统计数据 (WeUserBehaviorData)
 *
 * @author danmo
 * @since 2022-04-30 23:28:05
 */

@Repository()
@Mapper
public interface WeUserBehaviorDataMapper extends BaseMapper<WeUserBehaviorData> {

    List<WePageCountVo> getDayCountDataByTime(WePageStateQuery query);
}

