package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupTagRel;

/**
 * 群标签关系(WeGroupTagRel)
 *
 * @author danmo
 * @since 2022-04-06 11:09:56
 */
@Repository()
@Mapper
public interface WeGroupTagRelMapper extends BaseMapper<WeGroupTagRel> {


}

