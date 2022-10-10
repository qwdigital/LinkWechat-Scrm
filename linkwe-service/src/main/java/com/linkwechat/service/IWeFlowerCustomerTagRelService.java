package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeFlowerCustomerTagRel;

import java.util.List;

public interface IWeFlowerCustomerTagRelService extends IService<WeFlowerCustomerTagRel> {

    /**
     * 批量保存或更新
     * @param tagRels
     */
    void batchAddOrUpdate(List<WeFlowerCustomerTagRel> tagRels);


    /**
     *  获取当天新增的标签
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findNowAddWeFlowerCustomerTagRel(String externalUserId,String userId);


    /**
     * 获取移除的标签
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findRemoveWeFlowerCustomerTagRel(String externalUserId,String userId);


}
