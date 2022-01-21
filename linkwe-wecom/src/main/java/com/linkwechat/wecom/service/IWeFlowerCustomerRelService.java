package com.linkwechat.wecom.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;

/**
 * 具有外部联系人功能企业员工也客户的关系Service接口
 *
 * @author ruoyi
 * @date 2020-09-19
 */
public interface IWeFlowerCustomerRelService extends IService<WeFlowerCustomerRel>
{


    /**
     * 成员添加客户统计
     * @param weFlowerCustomerRel
     * @return Map
     */
    public Map<String,Object> getUserAddCustomerStat(WeFlowerCustomerRel weFlowerCustomerRel);

}
