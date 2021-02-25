package com.linkwechat.wecom.service.impl;

import java.io.InputStream;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.CosConfig;
import com.linkwechat.common.config.ServerConfig;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.file.FileUploadUtils;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.mapper.WeGroupCodeActualMapper;
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

    @Autowired
    private WeGroupCodeMapper weGroupCodeMapper;

    @Autowired
    private WeGroupCodeActualMapper weGroupCodeActualMapper;

    @Autowired
    private CosConfig cosConfig;

    /**
     * 查询客户群活码
     *
     * @param id 客户群活码ID
     * @return 客户群活码
     */
    @Override
    public WeGroupCode selectWeGroupCodeById(Long id)
    {
        // 获取群活码

        return weGroupCodeMapper.selectWeGroupCodeById(id);
    }

    /**
     * 根据群活码id查询实际码列表
     *
     * @param groupCodeId 群活码id
     * @return 结果
     */
    @Override
    public List<WeGroupCodeActual> selectActualListByGroupCodeId(Long groupCodeId) {
        return weGroupCodeActualMapper.selectActualListByGroupCodeId(groupCodeId);
    }

    /**
     * 查询客户群活码列表
     *
     * @param weGroupCode 客户群活码
     * @return 客户群活码
     */
    @Override
    public List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode)
    {
        List<WeGroupCode> weGroupCodeList = weGroupCodeMapper.selectWeGroupCodeList(weGroupCode);
        for (WeGroupCode item: weGroupCodeList) {
            List<WeGroupCodeActual> actualList = weGroupCodeActualMapper.selectActualListByGroupCodeId(item.getId());
            item.setActualList(actualList);
        }
        return weGroupCodeList;
    }

    /**
     * 根据群活码id查询群活码列表
     *
     * @param ids id列表
     * @return 结果
     */
    @Override
    public List<WeGroupCode> selectWeGroupCodeListByIds(List<String> ids) {
        return weGroupCodeMapper.selectWeGroupCodeListByIds(ids);
    }

    /**
     * 新增客户群活码
     * 
     * @param weGroupCode 客户群活码
     */
    @Override
    public void insertWeGroupCode(WeGroupCode weGroupCode)
    {
        try {
            weGroupCodeMapper.insertWeGroupCode(weGroupCode);
        } catch (Exception e) {
            throw new WeComException(e.getMessage());
        }

    }

    /**
     * 修改客户群活码
     * 
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    @Override
    public int updateWeGroupCode(WeGroupCode weGroupCode)
    {
        int rows = weGroupCodeMapper.updateWeGroupCode(weGroupCode);
        // 同时更新对应的实际码
        if (rows > 0) {
            List<WeGroupCodeActual> actualList = weGroupCode.getActualList();
            if(CollectionUtil.isNotEmpty(actualList)){
                iWeGroupCodeActualService.updateBatchById(actualList);
            }
        }
         return rows;
    }

    /**
     * 批量删除客户群活码
     *
     * @param ids 需要删除的客户群活码ID
     * @return 结果
     */
    @Override
    public int deleteWeGroupCodeByIds(Long[] ids)
    {
        weGroupCodeActualMapper.deleteActualListByGroupCodeIds(ids);
        return weGroupCodeMapper.deleteWeGroupCodeByIds(ids);
    }

    /**
     * 删除客户群活码信息
     *
     * @param id 客户群活码ID
     * @return 结果
     */
    @Override
    public int deleteWeGroupCodeById(Long id)
    {
        return weGroupCodeMapper.deleteWeGroupCodeById(id);
    }

    /**
     * 检测活码名称是否唯一
     *
     * @param weGroupCode 活码对象
     * @return 结果
     */
    @Override
    public boolean checkActivityNameUnique(WeGroupCode weGroupCode) {
        int rows =  weGroupCodeMapper.checkActivityNameUnique(weGroupCode.getActivityName());
        return rows == 0;
    }
}
