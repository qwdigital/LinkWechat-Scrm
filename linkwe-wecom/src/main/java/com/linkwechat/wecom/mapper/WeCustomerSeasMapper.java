package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerSeas;
import com.linkwechat.wecom.domain.vo.CustomerSeasCountVo;
import com.linkwechat.wecom.domain.vo.CustomerSeasRecordVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WeCustomerSeasMapper extends BaseMapper<WeCustomerSeas> {

    CustomerSeasCountVo countCustomerSeas();

    List<CustomerSeasRecordVo> findSeasRecord(@Param("groupByType") Integer groupByType);
}
