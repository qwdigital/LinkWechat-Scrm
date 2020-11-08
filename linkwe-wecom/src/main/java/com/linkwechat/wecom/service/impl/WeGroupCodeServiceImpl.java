package com.linkwechat.wecom.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeGroupCodeMapper;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.service.IWeGroupCodeService;

/**
 * 客户群活码Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@Service
public class WeGroupCodeServiceImpl extends ServiceImpl<WeGroupCodeMapper,WeGroupCode> implements IWeGroupCodeService
{

    @Autowired
    private IWeGroupCodeActualService iWeGroupCodeActualService;
//    @Autowired
//    private WeGroupCodeMapper weGroupCodeMapper;
//
//    /**
//     * 查询客户群活码
//     *
//     * @param id 客户群活码ID
//     * @return 客户群活码
//     */
//    @Override
//    public WeGroupCode selectWeGroupCodeById(Long id)
//    {
//        return weGroupCodeMapper.selectWeGroupCodeById(id);
//    }
//
//    /**
//     * 查询客户群活码列表
//     *
//     * @param weGroupCode 客户群活码
//     * @return 客户群活码
//     */
//    @Override
//    public List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode)
//    {
//        return weGroupCodeMapper.selectWeGroupCodeList(weGroupCode);
//    }

    /**
     * 新增客户群活码
     * 
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    @Override
    public void insertWeGroupCode(WeGroupCode weGroupCode)
    {

        if(this.save(weGroupCode)){
            List<WeGroupCodeActual> actualList = weGroupCode.getActualList();
            if(CollectionUtil.isNotEmpty(actualList)){
                iWeGroupCodeActualService.saveBatch(actualList);
            }
        }

    }

    /**
     * 修改客户群活码
     * 
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    @Override
    public void updateWeGroupCode(WeGroupCode weGroupCode)
    {
         if(this.updateById(weGroupCode)){
             List<WeGroupCodeActual> actualList = weGroupCode.getActualList();
             if(CollectionUtil.isNotEmpty(actualList)){
                 iWeGroupCodeActualService.updateBatchById(actualList);
             }
         }
    }
//
//    /**
//     * 批量删除客户群活码
//     *
//     * @param ids 需要删除的客户群活码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeGroupCodeByIds(Long[] ids)
//    {
//        return weGroupCodeMapper.deleteWeGroupCodeByIds(ids);
//    }
//
//    /**
//     * 删除客户群活码信息
//     *
//     * @param id 客户群活码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeGroupCodeById(Long id)
//    {
//        return weGroupCodeMapper.deleteWeGroupCodeById(id);
//    }
}
