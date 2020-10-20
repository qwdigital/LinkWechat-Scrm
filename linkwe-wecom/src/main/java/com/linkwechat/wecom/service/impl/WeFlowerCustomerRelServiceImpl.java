package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeFlowerCustomerRelMapper;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;

/**
 * 具有外部联系人功能企业员工也客户的关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
@Service
public class WeFlowerCustomerRelServiceImpl extends ServiceImpl<WeFlowerCustomerRelMapper,WeFlowerCustomerRel> implements IWeFlowerCustomerRelService
{
    @Autowired
    private WeFlowerCustomerRelMapper weFlowerCustomerRelMapper;

    /**
     * 查询具有外部联系人功能企业员工也客户的关系
     * 
     * @param id 具有外部联系人功能企业员工也客户的关系ID
     * @return 具有外部联系人功能企业员工也客户的关系
     */
    @Override
    public WeFlowerCustomerRel selectWeFlowerCustomerRelById(Long id)
    {
        return weFlowerCustomerRelMapper.selectWeFlowerCustomerRelById(id);
    }

    /**
     * 查询具有外部联系人功能企业员工也客户的关系列表
     * 
     * @param weFlowerCustomerRel 具有外部联系人功能企业员工也客户的关系
     * @return 具有外部联系人功能企业员工也客户的关系
     */
    @Override
    public List<WeFlowerCustomerRel> selectWeFlowerCustomerRelList(WeFlowerCustomerRel weFlowerCustomerRel)
    {
        return weFlowerCustomerRelMapper.selectWeFlowerCustomerRelList(weFlowerCustomerRel);
    }

    /**
     * 新增具有外部联系人功能企业员工也客户的关系
     * 
     * @param weFlowerCustomerRel 具有外部联系人功能企业员工也客户的关系
     * @return 结果
     */
    @Override
    public int insertWeFlowerCustomerRel(WeFlowerCustomerRel weFlowerCustomerRel)
    {
        weFlowerCustomerRel.setCreateTime(DateUtils.getNowDate());
        return weFlowerCustomerRelMapper.insertWeFlowerCustomerRel(weFlowerCustomerRel);
    }

    /**
     * 修改具有外部联系人功能企业员工也客户的关系
     * 
     * @param weFlowerCustomerRel 具有外部联系人功能企业员工也客户的关系
     * @return 结果
     */
    @Override
    public int updateWeFlowerCustomerRel(WeFlowerCustomerRel weFlowerCustomerRel)
    {
        return weFlowerCustomerRelMapper.updateWeFlowerCustomerRel(weFlowerCustomerRel);
    }

    /**
     * 批量删除具有外部联系人功能企业员工也客户的关系
     * 
     * @param ids 需要删除的具有外部联系人功能企业员工也客户的关系ID
     * @return 结果
     */
    @Override
    public int deleteWeFlowerCustomerRelByIds(Long[] ids)
    {
        return weFlowerCustomerRelMapper.deleteWeFlowerCustomerRelByIds(ids);
    }

    /**
     * 删除具有外部联系人功能企业员工也客户的关系信息
     * 
     * @param id 具有外部联系人功能企业员工也客户的关系ID
     * @return 结果
     */
    @Override
    public int deleteWeFlowerCustomerRelById(Long id)
    {
        return weFlowerCustomerRelMapper.deleteWeFlowerCustomerRelById(id);
    }


    /**
     * 批量插入
     * @param WeFlowerCustomerRels
     * @return
     */
    @Override
    public int batchInsetWeFlowerCustomerRel(List<WeFlowerCustomerRel> WeFlowerCustomerRels) {
        return weFlowerCustomerRelMapper.batchInsetWeFlowerCustomerRel(WeFlowerCustomerRels);
    }


    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    @Override
    public int batchLogicDeleteByIds(List<Long> ids) {
        return weFlowerCustomerRelMapper.batchLogicDeleteByIds(ids);
    }
}
