package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeAllocateCustomer;
import com.linkwechat.mapper.WeAllocateCustomerMapper;
import com.linkwechat.service.IWeAllocateCustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 离职分配的客户列Service业务层处理
 *
 * @author ruoyi
 * @date 2020-10-24
 */
@Service
public class WeAllocateCustomerServiceImpl extends ServiceImpl<WeAllocateCustomerMapper,WeAllocateCustomer> implements IWeAllocateCustomerService
{

    @Override
    public void batchAddOrUpdate(List<WeAllocateCustomer> weAllocateCustomer) {
        this.baseMapper.batchAddOrUpdate(weAllocateCustomer);
    }
}

