package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.mapper.WeTaskFissionMapper;
import com.linkwechat.wecom.service.IWeTaskFissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务宝Service业务层处理
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Service
public class WeTaskFissionServiceImpl implements IWeTaskFissionService {
    @Autowired
    private WeTaskFissionMapper weTaskFissionMapper;

    /**
     * 查询任务宝
     *
     * @param id 任务宝ID
     * @return 任务宝
     */
    @Override
    public WeTaskFission selectWeTaskFissionById(Long id) {
        return weTaskFissionMapper.selectWeTaskFissionById(id);
    }

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝
     */
    @Override
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission) {
        return weTaskFissionMapper.selectWeTaskFissionList(weTaskFission);
    }

    /**
     * 新增任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    @Override
    public int insertWeTaskFission(WeTaskFission weTaskFission) {
        weTaskFission.setCreateTime(DateUtils.getNowDate());
        return weTaskFissionMapper.insertWeTaskFission(weTaskFission);
    }

    /**
     * 修改任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    @Override
    public int updateWeTaskFission(WeTaskFission weTaskFission) {
        weTaskFission.setUpdateTime(DateUtils.getNowDate());
        return weTaskFissionMapper.updateWeTaskFission(weTaskFission);
    }

    /**
     * 批量删除任务宝
     *
     * @param ids 需要删除的任务宝ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionByIds(Long[] ids) {
        return weTaskFissionMapper.deleteWeTaskFissionByIds(ids);
    }

    /**
     * 删除任务宝信息
     *
     * @param id 任务宝ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionById(Long id) {
        return weTaskFissionMapper.deleteWeTaskFissionById(id);
    }
}
