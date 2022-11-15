package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeKfCustomer;

/**
 * 客服客户表(WeKfCustomer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:33
 */
@Repository()
@Mapper
public interface WeKfCustomerMapper extends BaseMapper<WeKfCustomer> {


}

