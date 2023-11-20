package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeFlowerCustomerTagRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeFlowerCustomerTagRelMapper extends BaseMapper<WeFlowerCustomerTagRel> {

    /**
     * 批量保存或更新
     *
     * @param tagRels
     */

    void batchAddOrUpdate(@Param("tagRels") List<WeFlowerCustomerTagRel> tagRels);


    /**
     * 获取当天新增的标签
     *
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findNowAddWeFlowerCustomerTagRel(@Param("externalUserId") String externalUserId, @Param("userId") String userId);


    /**
     * 批量获取员工对应的标签
     * @param weCustomers
     * @return
     */
    List<WeFlowerCustomerTagRel> findConcatNowAddWeFlowerCustomerTagRel(@Param("weCustomers") List<WeCustomer> weCustomers);



    /**
     * 批量删除客户对应的标签关系
     * @param weCustomers
     * @return
     */
    void removeConcatNowAddWeFlowerCustomerTagRel(@Param("weCustomers") List<WeCustomer> weCustomers);



    /**
     * 获取移除的标签
     *
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findRemoveWeFlowerCustomerTagRel(@Param("externalUserId") String externalUserId, @Param("userId") String userId);


    /**
     * 通过标签Id和用户Id获取客户的数量 （不去重）
     *
     * @param tagIds  标签Id集合
     * @param userIds 企微用户Id集合
     * @return {@link int}
     * @author WangYX
     * @date 2023/06/13 16:15
     */
    List<String> getCountByTagIdAndUserId(@Param("tagIds") List<String> tagIds, @Param("userIds") List<String> userIds);


    /**
     * 通过标签Id和用户Id获取客户 （不去重）
     *
     * @param tagIds  标签Id集合
     * @param userIds 企微用户Id集合
     * @return {@link List<WeFlowerCustomerTagRel>}
     * @author WangYX
     * @date 2023/06/30 18:35
     */
    List<WeFlowerCustomerTagRel> getListByTagIdAndUserId(@Param("tagIds") List<String> tagIds, @Param("userIds") List<String> userIds);

}
