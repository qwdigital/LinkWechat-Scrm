package com.linkwechat.mapper;

import com.linkwechat.domain.WeCustomerLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeCustomerLinkCount;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_customer_link(获客助手)】的数据库操作Mapper
* @createDate 2023-07-04 17:41:13
* @Entity com.linkwechat.domain.WeCustomerLink
*/
public interface WeCustomerLinkMapper extends BaseMapper<WeCustomerLink> {

    /**
     * 获取相关连接下添加的客户
     * @param weCustomerLinkCount
     * @return
     */
    List<WeCustomerLinkCount> findLinkWeCustomer(@Param("weCustomerLinkCount") WeCustomerLinkCount weCustomerLinkCount);
}




