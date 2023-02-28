package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeStrategicCrowdCustomerRel;

/**
 * 策略人群客户关联表(WeStrategicCrowdCustomerRel)
 *
 * @author danmo
 * @since 2022-07-30 23:56:17
 */
@Repository()
@Mapper
public interface WeStrategicCrowdCustomerRelMapper extends BaseMapper<WeStrategicCrowdCustomerRel> {


}

