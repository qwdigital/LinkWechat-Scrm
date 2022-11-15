package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeStrategicCrowd;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdAnalyzelDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 策略人群信息表(WeStrategicCrowd)
 *
 * @author danmo
 * @since 2022-07-05 15:33:28
 */
@Repository()
@Mapper
public interface WeStrategicCrowdMapper extends BaseMapper<WeStrategicCrowd> {
    List<WeStrategicCrowdAnalyzelDataVo> getAnalyze(WeStrategicCrowdQuery query);
}

