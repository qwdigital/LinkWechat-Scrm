package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.mapper.WeFlowerCustomerRelMapper;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 具有外部联系人功能企业员工也客户的关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
@Service
public class WeFlowerCustomerRelServiceImpl extends ServiceImpl<WeFlowerCustomerRelMapper,WeFlowerCustomerRel> implements IWeFlowerCustomerRelService
{


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFollowUser(String userId, String externalUserid) {
        return this.update(WeFlowerCustomerRel.builder().status("1").build()
                ,new LambdaQueryWrapper<WeFlowerCustomerRel>()
                .eq(WeFlowerCustomerRel::getUserId,userId)
                .eq(WeFlowerCustomerRel::getExternalUserid,externalUserid));
    }
}
