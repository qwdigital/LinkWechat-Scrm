package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeAllocateCustomer;
import java.util.List;

/**
 * 离职分配的客户列Service接口
 *
 * @author ruoyi
 * @date 2020-10-24
 */
public interface IWeAllocateCustomerService  extends IService<WeAllocateCustomer>
{
    void batchAddOrUpdate(List<WeAllocateCustomer> weAllocateCustomer);
}