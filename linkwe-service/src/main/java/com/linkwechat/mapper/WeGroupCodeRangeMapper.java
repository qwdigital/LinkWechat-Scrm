package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupCodeRange;

/**
 * 客户群活码范围(WeGroupCodeRange)
 *
 * @author danmo
 * @since 2023-06-26 17:47:12
 */
@Repository()
@Mapper
public interface WeGroupCodeRangeMapper extends BaseMapper<WeGroupCodeRange> {


}

