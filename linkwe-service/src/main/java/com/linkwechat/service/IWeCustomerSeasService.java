package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCustomerSeas;
import com.linkwechat.domain.wecom.vo.customer.seas.CustomerSeasCountVo;
import com.linkwechat.domain.wecom.vo.customer.seas.CustomerSeasRecordVo;

import java.util.List;


public interface IWeCustomerSeasService extends IService<WeCustomerSeas> {


    CustomerSeasCountVo countCustomerSeas();

    List<CustomerSeasRecordVo> findSeasRecord(Integer groupByType);


    void remidUser(List<String> addUserIds,Integer customerNum);

    List<WeCustomerSeas> findWeCustomerSeas(WeCustomerSeas weCustomerSea);


}
