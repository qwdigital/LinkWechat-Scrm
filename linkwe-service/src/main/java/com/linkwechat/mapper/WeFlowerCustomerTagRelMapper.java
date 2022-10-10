package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeFlowerCustomerTagRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeFlowerCustomerTagRelMapper extends BaseMapper<WeFlowerCustomerTagRel> {

    /**
     * 批量保存或更新
     * @param tagRels
     */
    
    void batchAddOrUpdate(@Param("tagRels") List<WeFlowerCustomerTagRel> tagRels);


    /**
     * 获取当天新增的标签
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findNowAddWeFlowerCustomerTagRel(@Param("externalUserId") String externalUserId,@Param("userId") String userId);

    /**
     * 获取移除的标签
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findRemoveWeFlowerCustomerTagRel(@Param("externalUserId") String externalUserId,@Param("userId") String userId);

}
