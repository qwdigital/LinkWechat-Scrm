package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupUserStatistic;

/**
 * 群聊群主数据统计数据(WeGroupUserStatistic)
 *
 * @author danmo
 * @since 2022-06-13 16:59:10
 */
@Repository()
@Mapper
public interface WeGroupUserStatisticMapper extends BaseMapper<WeGroupUserStatistic> {


}

