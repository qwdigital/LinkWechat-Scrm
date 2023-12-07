package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeKfCustomerStat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 客服客户统计表(WeKfCustomerStat)
 *
 * @author danmo
 * @since 2022-11-28 16:48:24
 */
@Repository()
@Mapper
public interface WeKfCustomerStatMapper extends BaseMapper<WeKfCustomerStat> {


}

