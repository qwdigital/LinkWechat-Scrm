package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeTaskFissionStaff;

import java.util.List;

/**
 * 裂变任务员工列Service接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface IWeTaskFissionStaffService {
    /**
     * 查询裂变任务员工列
     *
     * @param id 裂变任务员工列ID
     * @return 裂变任务员工列
     */
    public WeTaskFissionStaff selectWeTaskFissionStaffById(Long id);

    /**
     * 查询裂变任务员工列列表
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 裂变任务员工列集合
     */
    public List<WeTaskFissionStaff> selectWeTaskFissionStaffList(WeTaskFissionStaff weTaskFissionStaff);

    /**
     * 新增裂变任务员工列
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 结果
     */
    public int insertWeTaskFissionStaff(WeTaskFissionStaff weTaskFissionStaff);

    /**
     * 批量新增裂变任务员工
     *
     * @param weTaskFissionStaffs 裂变任务员工列表
     * @return 结果
     */
    public int insertWeTaskFissionStaffList(List<WeTaskFissionStaff> weTaskFissionStaffs);

    /**
     * 修改裂变任务员工列
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 结果
     */
    public int updateWeTaskFissionStaff(WeTaskFissionStaff weTaskFissionStaff);

    /**
     * 批量删除裂变任务员工列
     *
     * @param ids 需要删除的裂变任务员工列ID
     * @return 结果
     */
    public int deleteWeTaskFissionStaffByIds(Long[] ids);

    /**
     * 删除裂变任务员工列信息
     *
     * @param id 裂变任务员工列ID
     * @return 结果
     */
    public int deleteWeTaskFissionStaffById(Long id);

    /**
     * t根据任务id获取员工信息列表
     *
     * @param taskId 任务id
     * @return
     */
    public List<WeTaskFissionStaff> selectWeTaskFissionStaffByTaskId(Long taskId);
}
