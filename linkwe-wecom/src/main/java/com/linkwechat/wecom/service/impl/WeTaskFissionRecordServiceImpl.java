package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeTaskFissionRecord;
import com.linkwechat.wecom.mapper.WeTaskFissionRecordMapper;
import com.linkwechat.wecom.service.IWeTaskFissionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 裂变任务记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-27
 */
@Service
public class WeTaskFissionRecordServiceImpl extends ServiceImpl<WeTaskFissionRecordMapper, WeTaskFissionRecord> implements IWeTaskFissionRecordService {
    @Autowired
    private WeTaskFissionRecordMapper weTaskFissionRecordMapper;

    /**
     * 查询裂变任务记录
     *
     * @param id 裂变任务记录ID
     * @return 裂变任务记录
     */
    @Override
    public WeTaskFissionRecord selectWeTaskFissionRecordById(Long id) {
        return weTaskFissionRecordMapper.selectWeTaskFissionRecordById(id);
    }

    /**
     * 查询裂变任务记录列表
     *
     * @param weTaskFissionRecord 裂变任务记录
     * @return 裂变任务记录
     */
    @Override
    public List<WeTaskFissionRecord> selectWeTaskFissionRecordList(WeTaskFissionRecord weTaskFissionRecord) {
        return weTaskFissionRecordMapper.selectWeTaskFissionRecordList(weTaskFissionRecord);
    }

    /**
     * 新增裂变任务记录
     *
     * @param weTaskFissionRecord 裂变任务记录
     * @return 结果
     */
    @Override
    public int insertWeTaskFissionRecord(WeTaskFissionRecord weTaskFissionRecord) {
        return weTaskFissionRecordMapper.insertWeTaskFissionRecord(weTaskFissionRecord);
    }

    /**
     * 修改裂变任务记录
     *
     * @param weTaskFissionRecord 裂变任务记录
     * @return 结果
     */
    @Override
    public int updateWeTaskFissionRecord(WeTaskFissionRecord weTaskFissionRecord) {
        return weTaskFissionRecordMapper.updateWeTaskFissionRecord(weTaskFissionRecord);
    }

    /**
     * 批量删除裂变任务记录
     *
     * @param ids 需要删除的裂变任务记录ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionRecordByIds(Long[] ids) {
        return weTaskFissionRecordMapper.deleteWeTaskFissionRecordByIds(ids);
    }

    /**
     * 删除裂变任务记录信息
     *
     * @param id 裂变任务记录ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionRecordById(Long id) {
        return weTaskFissionRecordMapper.deleteWeTaskFissionRecordById(id);
    }

    @Override
    public WeTaskFissionRecord selectWeTaskFissionRecordByIdAndCustomerId(Long id, String customerId) {
        return weTaskFissionRecordMapper.selectOne(new LambdaQueryWrapper<WeTaskFissionRecord>()
                .eq(WeTaskFissionRecord::getTaskFissionId, id)
                .eq(WeTaskFissionRecord::getCustomerId, customerId));
    }

    @Override
    public List<WeTaskFissionRecord> statisticRecords(Long taskFissionId, Date startTime, Date endTime) {
        LambdaQueryWrapper<WeTaskFissionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeTaskFissionRecord::getTaskFissionId, taskFissionId).
                between(WeTaskFissionRecord::getCreateTime, startTime, endTime).
                orderByAsc(WeTaskFissionRecord::getCreateTime);
        return weTaskFissionRecordMapper.selectList(wrapper);
    }
}
