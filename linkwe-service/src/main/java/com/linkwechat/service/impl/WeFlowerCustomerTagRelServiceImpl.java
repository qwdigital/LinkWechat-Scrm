package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeFlowerCustomerTagRel;
import com.linkwechat.mapper.WeFlowerCustomerTagRelMapper;
import com.linkwechat.service.IWeFlowerCustomerTagRelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户标签关系Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-19
 */
@Service
public class WeFlowerCustomerTagRelServiceImpl extends ServiceImpl<WeFlowerCustomerTagRelMapper, WeFlowerCustomerTagRel> implements IWeFlowerCustomerTagRelService {


    @Override
    public void batchAddOrUpdate(List<WeFlowerCustomerTagRel> tagRels) {
        this.baseMapper.batchAddOrUpdate(tagRels);
    }

    @Override
    public List<WeFlowerCustomerTagRel> findNowAddWeFlowerCustomerTagRel(String externalUserId, String userId) {
        return this.baseMapper.findNowAddWeFlowerCustomerTagRel(externalUserId, userId);
    }

    @Override
    public List<WeFlowerCustomerTagRel> findRemoveWeFlowerCustomerTagRel(String externalUserId, String userId) {
        return this.baseMapper.findRemoveWeFlowerCustomerTagRel(externalUserId, userId);
    }

    @Override
    public List<String> getCountByTagIdAndUserId(List<String> tagIds, List<String> userIds) {
        return this.baseMapper.getCountByTagIdAndUserId(tagIds, userIds);
    }

    @Override
    public List<WeFlowerCustomerTagRel> getListByTagIdAndUserId(List<String> tagIds, List<String> userIds) {
        return this.baseMapper.getListByTagIdAndUserId(tagIds, userIds);
    }

    @Override
    public List<WeFlowerCustomerTagRel> findConcatNowAddWeFlowerCustomerTagRel(List<WeCustomer> weCustomers) {
        return this.baseMapper.findConcatNowAddWeFlowerCustomerTagRel(weCustomers);
    }

    @Override
    public void removeConcatNowAddWeFlowerCustomerTagRel(List<WeCustomer> weCustomers) {
        this.baseMapper.removeConcatNowAddWeFlowerCustomerTagRel(weCustomers);
    }
}
