package com.linkwechat.wecom.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import org.apache.ibatis.annotations.Param;

/**
 * 具有外部联系人功能企业员工也客户的关系Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
public interface WeFlowerCustomerRelMapper extends BaseMapper<WeFlowerCustomerRel>
{
    /**
     * 查询具有外部联系人功能企业员工也客户的关系
     * 
     * @param id 具有外部联系人功能企业员工也客户的关系ID
     * @return 具有外部联系人功能企业员工也客户的关系
     */
    public WeFlowerCustomerRel selectWeFlowerCustomerRelById(Long id);

    /**
     * 查询具有外部联系人功能企业员工也客户的关系列表
     * 
     * @param weFlowerCustomerRel 具有外部联系人功能企业员工也客户的关系
     * @return 具有外部联系人功能企业员工也客户的关系集合
     */
    public List<WeFlowerCustomerRel> selectWeFlowerCustomerRelList(WeFlowerCustomerRel weFlowerCustomerRel);

    /**
     * 新增具有外部联系人功能企业员工也客户的关系
     * 
     * @param weFlowerCustomerRel 具有外部联系人功能企业员工也客户的关系
     * @return 结果
     */
    public int insertWeFlowerCustomerRel(WeFlowerCustomerRel weFlowerCustomerRel);

    /**
     * 修改具有外部联系人功能企业员工也客户的关系
     * 
     * @param weFlowerCustomerRel 具有外部联系人功能企业员工也客户的关系
     * @return 结果
     */
    public int updateWeFlowerCustomerRel(WeFlowerCustomerRel weFlowerCustomerRel);

    /**
     * 删除具有外部联系人功能企业员工也客户的关系
     * 
     * @param id 具有外部联系人功能企业员工也客户的关系ID
     * @return 结果
     */
    public int deleteWeFlowerCustomerRelById(Long id);

    /**
     * 批量删除具有外部联系人功能企业员工也客户的关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeFlowerCustomerRelByIds(Long[] ids);


    /**
     * 批量插入
     * @param weFlowerCustomerRels
     * @return
     */
    public int batchInsetWeFlowerCustomerRel(@Param("weFlowerCustomerRels") List<WeFlowerCustomerRel> weFlowerCustomerRels);


    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    public int batchLogicDeleteByIds(@Param("ids") List<Long> ids);

    /**
     * 成员添加客户统计
     * @param weFlowerCustomerRel
     * @return
     */
    public List<Map<String,Object>> getUserAddCustomerStat(WeFlowerCustomerRel weFlowerCustomerRel);
}
