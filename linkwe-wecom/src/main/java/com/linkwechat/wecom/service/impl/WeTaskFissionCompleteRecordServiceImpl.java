package com.linkwechat.wecom.service.impl;

import com.linkwechat.wecom.domain.WeTaskFissionCompleteRecord;
import com.linkwechat.wecom.mapper.WeTaskFissionCompleteRecordMapper;
import com.linkwechat.wecom.service.IWeTaskFissionCompleteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 裂变任务完成记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-27
 */
@Service
public class WeTaskFissionCompleteRecordServiceImpl implements IWeTaskFissionCompleteRecordService {
    @Autowired
    private WeTaskFissionCompleteRecordMapper weTaskFissionCompleteRecordMapper;

    /**
     * 查询裂变任务完成记录
     *
     * @param id 裂变任务完成记录ID
     * @return 裂变任务完成记录
     */
    @Override
    public WeTaskFissionCompleteRecord selectWeTaskFissionCompleteRecordById(Long id) {
        return weTaskFissionCompleteRecordMapper.selectWeTaskFissionCompleteRecordById(id);
    }

    /**
     * 查询裂变任务完成记录列表
     *
     * @param weTaskFissionCompleteRecord 裂变任务完成记录
     * @return 裂变任务完成记录
     */
    @Override
    public List<WeTaskFissionCompleteRecord> selectWeTaskFissionCompleteRecordList(WeTaskFissionCompleteRecord weTaskFissionCompleteRecord) {
        return weTaskFissionCompleteRecordMapper.selectWeTaskFissionCompleteRecordList(weTaskFissionCompleteRecord);
    }

    /**
     * 新增裂变任务完成记录
     *
     * @param weTaskFissionCompleteRecord 裂变任务完成记录
     * @return 结果
     */
    @Override
    public int insertWeTaskFissionCompleteRecord(WeTaskFissionCompleteRecord weTaskFissionCompleteRecord) {
        return weTaskFissionCompleteRecordMapper.insertWeTaskFissionCompleteRecord(weTaskFissionCompleteRecord);
    }

    /**
     * 修改裂变任务完成记录
     *
     * @param weTaskFissionCompleteRecord 裂变任务完成记录
     * @return 结果
     */
    @Override
    public int updateWeTaskFissionCompleteRecord(WeTaskFissionCompleteRecord weTaskFissionCompleteRecord) {
        return weTaskFissionCompleteRecordMapper.updateWeTaskFissionCompleteRecord(weTaskFissionCompleteRecord);
    }

    /**
     * 批量删除裂变任务完成记录
     *
     * @param ids 需要删除的裂变任务完成记录ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionCompleteRecordByIds(Long[] ids) {
        return weTaskFissionCompleteRecordMapper.deleteWeTaskFissionCompleteRecordByIds(ids);
    }

    /**
     * 删除裂变任务完成记录信息
     *
     * @param id 裂变任务完成记录ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionCompleteRecordById(Long id) {
        return weTaskFissionCompleteRecordMapper.deleteWeTaskFissionCompleteRecordById(id);
    }
}
