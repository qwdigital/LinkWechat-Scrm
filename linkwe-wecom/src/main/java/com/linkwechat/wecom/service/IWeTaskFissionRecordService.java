package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeTaskFissionRecord;

import java.util.Date;
import java.util.List;

/**
 * 裂变任务记录Service接口
 *
 * @author ruoyi
 * @date 2021-01-27
 */
public interface IWeTaskFissionRecordService extends IService<WeTaskFissionRecord> {
    /**
     * 查询裂变任务记录
     *
     * @param id 裂变任务记录ID
     * @return 裂变任务记录
     */
    public WeTaskFissionRecord selectWeTaskFissionRecordById(Long id);

    /**
     * 查询裂变任务记录列表
     *
     * @param weTaskFissionRecord 裂变任务记录
     * @return 裂变任务记录集合
     */
    public List<WeTaskFissionRecord> selectWeTaskFissionRecordList(WeTaskFissionRecord weTaskFissionRecord);

    /**
     * 新增裂变任务记录
     *
     * @param weTaskFissionRecord 裂变任务记录
     * @return 结果
     */
    public int insertWeTaskFissionRecord(WeTaskFissionRecord weTaskFissionRecord);

    /**
     * 修改裂变任务记录
     *
     * @param weTaskFissionRecord 裂变任务记录
     * @return 结果
     */
    public int updateWeTaskFissionRecord(WeTaskFissionRecord weTaskFissionRecord);

    /**
     * 批量删除裂变任务记录
     *
     * @param ids 需要删除的裂变任务记录ID
     * @return 结果
     */
    public int deleteWeTaskFissionRecordByIds(Long[] ids);

    /**
     * 删除裂变任务记录信息
     *
     * @param id 裂变任务记录ID
     * @return 结果
     */
    public int deleteWeTaskFissionRecordById(Long id);


    public WeTaskFissionRecord selectWeTaskFissionRecordByIdAndCustomerId(Long id, String customerId);

    public List<WeTaskFissionRecord> statisticRecords(Long taskFissionId, Date startTime, Date endTime);
}
