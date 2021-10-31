package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import org.apache.ibatis.annotations.Param;

/**
 * 客户标签关系Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
public interface WeFlowerCustomerTagRelMapper  extends BaseMapper<WeFlowerCustomerTagRel>
{
    /**
     * 查询客户标签关系
     * 
     * @param id 客户标签关系ID
     * @return 客户标签关系
     */
    public WeFlowerCustomerTagRel selectWeFlowerCustomerTagRelById(Long id);

    /**
     * 查询客户标签关系列表
     * 
     * @param weFlowerCustomerTagRel 客户标签关系
     * @return 客户标签关系集合
     */
    public List<WeFlowerCustomerTagRel> selectWeFlowerCustomerTagRelList(WeFlowerCustomerTagRel weFlowerCustomerTagRel);

    /**
     * 新增客户标签关系
     * 
     * @param weFlowerCustomerTagRel 客户标签关系
     * @return 结果
     */
    public int insertWeFlowerCustomerTagRel(WeFlowerCustomerTagRel weFlowerCustomerTagRel);

    /**
     * 修改客户标签关系
     * 
     * @param weFlowerCustomerTagRel 客户标签关系
     * @return 结果
     */
    public int updateWeFlowerCustomerTagRel(WeFlowerCustomerTagRel weFlowerCustomerTagRel);

    /**
     * 删除客户标签关系
     * 
     * @param id 客户标签关系ID
     * @return 结果
     */
    public int deleteWeFlowerCustomerTagRelById(Long id);

    /**
     * 批量删除客户标签关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeFlowerCustomerTagRelByIds(Long[] ids);


    /**
     * 批量插入
     * @param weFlowerCustomerTagRels
     * @return
     */
    public int batchInsetWeFlowerCustomerTagRel(List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels);


    void batchAddOrUpdate(@Param("tagRels") List<WeFlowerCustomerTagRel> tagRels);
}
