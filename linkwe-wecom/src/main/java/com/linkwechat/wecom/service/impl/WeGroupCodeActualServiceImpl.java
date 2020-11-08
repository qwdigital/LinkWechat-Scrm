package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeGroupCodeActualMapper;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;

/**
 * 实际群码Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@Service
public class WeGroupCodeActualServiceImpl extends ServiceImpl<WeGroupCodeActualMapper,WeGroupCodeActual> implements IWeGroupCodeActualService
{
//    @Autowired
//    private WeGroupCodeActualMapper weGroupCodeActualMapper;

//    /**
//     * 查询实际群码
//     *
//     * @param id 实际群码ID
//     * @return 实际群码
//     */
//    @Override
//    public WeGroupCodeActual selectWeGroupCodeActualById(Long id)
//    {
//        return weGroupCodeActualMapper.selectWeGroupCodeActualById(id);
//    }
//
//    /**
//     * 查询实际群码列表
//     *
//     * @param weGroupCodeActual 实际群码
//     * @return 实际群码
//     */
//    @Override
//    public List<WeGroupCodeActual> selectWeGroupCodeActualList(WeGroupCodeActual weGroupCodeActual)
//    {
//        return weGroupCodeActualMapper.selectWeGroupCodeActualList(weGroupCodeActual);
//    }

//    /**
//     * 新增实际群码
//     *
//     * @param weGroupCodeActual 实际群码
//     * @return 结果
//     */
//    @Override
//    public int insertWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual)
//    {
//        this.sa
//        return this.baseMapper.insertWeGroupCodeActual(weGroupCodeActual);
//    }
//
//    /**
//     * 修改实际群码
//     *
//     * @param weGroupCodeActual 实际群码
//     * @return 结果
//     */
//    @Override
//    public int updateWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual)
//    {
//        return this.baseMapper.updateWeGroupCodeActual(weGroupCodeActual);
//    }

//    /**
//     * 批量删除实际群码
//     *
//     * @param ids 需要删除的实际群码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeGroupCodeActualByIds(Long[] ids)
//    {
//        return weGroupCodeActualMapper.deleteWeGroupCodeActualByIds(ids);
//    }
//
//    /**
//     * 删除实际群码信息
//     *
//     * @param id 实际群码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeGroupCodeActualById(Long id)
//    {
//        return weGroupCodeActualMapper.deleteWeGroupCodeActualById(id);
//    }
}
