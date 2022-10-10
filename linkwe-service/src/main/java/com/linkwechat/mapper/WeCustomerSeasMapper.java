package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeCustomerSeas;
import com.linkwechat.domain.wecom.vo.customer.seas.CustomerSeasCountVo;
import com.linkwechat.domain.wecom.vo.customer.seas.CustomerSeasRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeCustomerSeasMapper  extends BaseMapper<WeCustomerSeas> {

    CustomerSeasCountVo countCustomerSeas();

    List<CustomerSeasRecordVo> findSeasRecord(@Param("groupByType") Integer groupByType);

}
