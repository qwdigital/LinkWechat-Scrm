package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeTaskFissionReward;

/**
 * 任务裂变奖励(WeTaskFissionReward)
 *
 * @author danmo
 * @since 2022-06-28 13:48:55
 */
@Repository()
@Mapper
public interface WeTaskFissionRewardMapper extends BaseMapper<WeTaskFissionReward> {


}

