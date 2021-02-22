package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeTaskFissionStaff;

import java.util.List;

/**
 * 裂变任务员工列Mapper接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface WeTaskFissionStaffMapper {
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
     * 新增裂变任务员工
     *
     * @param weTaskFissionStaff 裂变任务员工
     * @return 结果
     */
    public int insertWeTaskFissionStaff(WeTaskFissionStaff weTaskFissionStaff);

    /**
     * 批量新增裂变任务员工
     *
     * @param weTaskFissionStaffList 裂变任务员工List
     * @return 结果
     */
    public int insertWeTaskFissionStaffList(List<WeTaskFissionStaff> weTaskFissionStaffList);

    /**
     * 修改裂变任务员工列
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 结果
     */
    public int updateWeTaskFissionStaff(WeTaskFissionStaff weTaskFissionStaff);

    /**
     * 删除裂变任务员工列
     *
     * @param id 裂变任务员工列ID
     * @return 结果
     */
    public int deleteWeTaskFissionStaffById(Long id);

    /**
     * 批量删除裂变任务员工列
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTaskFissionStaffByIds(Long[] ids);
}
