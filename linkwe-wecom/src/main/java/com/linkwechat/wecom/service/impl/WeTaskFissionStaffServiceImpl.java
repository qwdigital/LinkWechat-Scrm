package com.linkwechat.wecom.service.impl;

import com.linkwechat.wecom.domain.WeTaskFissionStaff;
import com.linkwechat.wecom.mapper.WeTaskFissionStaffMapper;
import com.linkwechat.wecom.service.IWeTaskFissionStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 裂变任务员工列Service业务层处理
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Service
public class WeTaskFissionStaffServiceImpl implements IWeTaskFissionStaffService {
    @Autowired
    private WeTaskFissionStaffMapper weTaskFissionStaffMapper;

    /**
     * 查询裂变任务员工列
     *
     * @param id 裂变任务员工列ID
     * @return 裂变任务员工列
     */
    @Override
    public WeTaskFissionStaff selectWeTaskFissionStaffById(Long id) {
        return weTaskFissionStaffMapper.selectWeTaskFissionStaffById(id);
    }

    /**
     * 查询裂变任务员工列列表
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 裂变任务员工列
     */
    @Override
    public List<WeTaskFissionStaff> selectWeTaskFissionStaffList(WeTaskFissionStaff weTaskFissionStaff) {
        return weTaskFissionStaffMapper.selectWeTaskFissionStaffList(weTaskFissionStaff);
    }

    /**
     * 新增裂变任务员工列
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 结果
     */
    @Override
    public int insertWeTaskFissionStaff(WeTaskFissionStaff weTaskFissionStaff) {
        return weTaskFissionStaffMapper.insertWeTaskFissionStaff(weTaskFissionStaff);
    }

    @Override
    public int insertWeTaskFissionStaffList(List<WeTaskFissionStaff> weTaskFissionStaffs) {
        return weTaskFissionStaffMapper.insertWeTaskFissionStaffList(weTaskFissionStaffs);
    }

    /**
     * 修改裂变任务员工列
     *
     * @param weTaskFissionStaff 裂变任务员工列
     * @return 结果
     */
    @Override
    public int updateWeTaskFissionStaff(WeTaskFissionStaff weTaskFissionStaff) {
        return weTaskFissionStaffMapper.updateWeTaskFissionStaff(weTaskFissionStaff);
    }

    /**
     * 批量删除裂变任务员工列
     *
     * @param ids 需要删除的裂变任务员工列ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionStaffByIds(Long[] ids) {
        return weTaskFissionStaffMapper.deleteWeTaskFissionStaffByIds(ids);
    }

    /**
     * 删除裂变任务员工列信息
     *
     * @param id 裂变任务员工列ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionStaffById(Long id) {
        return weTaskFissionStaffMapper.deleteWeTaskFissionStaffById(id);
    }

    /**
     * t根据任务id获取员工信息列表
     * @param taskId 任务id
     * @return
     */
    @Override
    public List<WeTaskFissionStaff> selectWeTaskFissionStaffByTaskId(Long taskId) {
        WeTaskFissionStaff weTaskFissionStaff = new WeTaskFissionStaff();
        weTaskFissionStaff.setTaskFissionId(taskId);
        return weTaskFissionStaffMapper.selectWeTaskFissionStaffList(weTaskFissionStaff);
    }
}
