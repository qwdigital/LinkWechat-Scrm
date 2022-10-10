package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupStatistic;

/**
 * 群聊数据统计数据
 * (WeGroupStatistic)
 *
 * @author danmo
 * @since 2022-04-30 23:28:18
 */

@Repository()
@Mapper
public interface WeGroupStatisticMapper extends BaseMapper<WeGroupStatistic> {


}

