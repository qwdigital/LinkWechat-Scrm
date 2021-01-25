package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeTaskFissionRecord;

import java.util.List;

/**
 * 裂变任务完成记录Mapper接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface WeTaskFissionRecordMapper {
    /**
     * 查询裂变任务完成记录
     *
     * @param id 裂变任务完成记录ID
     * @return 裂变任务完成记录
     */
    public WeTaskFissionRecord selectWeTaskFissionRecordById(Long id);

    /**
     * 查询裂变任务完成记录列表
     *
     * @param weTaskFissionRecord 裂变任务完成记录
     * @return 裂变任务完成记录集合
     */
    public List<WeTaskFissionRecord> selectWeTaskFissionRecordList(WeTaskFissionRecord weTaskFissionRecord);

    /**
     * 新增裂变任务完成记录
     *
     * @param weTaskFissionRecord 裂变任务完成记录
     * @return 结果
     */
    public int insertWeTaskFissionRecord(WeTaskFissionRecord weTaskFissionRecord);

    /**
     * 修改裂变任务完成记录
     *
     * @param weTaskFissionRecord 裂变任务完成记录
     * @return 结果
     */
    public int updateWeTaskFissionRecord(WeTaskFissionRecord weTaskFissionRecord);

    /**
     * 删除裂变任务完成记录
     *
     * @param id 裂变任务完成记录ID
     * @return 结果
     */
    public int deleteWeTaskFissionRecordById(Long id);

    /**
     * 批量删除裂变任务完成记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTaskFissionRecordByIds(Long[] ids);
}
