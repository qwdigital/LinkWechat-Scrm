package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeTaskFissionRecord;

import java.util.List;

/**
 * 裂变任务记录Mapper接口
 *
 * @author ruoyi
 * @date 2021-01-27
 */
public interface WeTaskFissionRecordMapper extends BaseMapper<WeTaskFissionRecord> {
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
     * 删除裂变任务记录
     *
     * @param id 裂变任务记录ID
     * @return 结果
     */
    public int deleteWeTaskFissionRecordById(Long id);

    /**
     * 批量删除裂变任务记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTaskFissionRecordByIds(Long[] ids);
}
