package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerSeas;
import com.linkwechat.wecom.domain.vo.CustomerSeasCountVo;
import com.linkwechat.wecom.domain.vo.CustomerSeasRecordVo;
import java.util.List;


public interface IWeCustomerSeasService extends IService<WeCustomerSeas> {


    CustomerSeasCountVo countCustomerSeas();

    List<CustomerSeasRecordVo> findSeasRecord(Integer groupByType);


    void remidUser(List<String> addUserIds,Integer customerNum);

}
