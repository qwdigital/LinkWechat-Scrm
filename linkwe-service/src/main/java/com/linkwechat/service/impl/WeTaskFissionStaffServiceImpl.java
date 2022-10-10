package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeTaskFissionStaffMapper;
import com.linkwechat.domain.WeTaskFissionStaff;
import com.linkwechat.service.IWeTaskFissionStaffService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 裂变任务员工列表(WeTaskFissionStaff)
 *
 * @author danmo
 * @since 2022-06-28 13:48:55
 */
@Service
public class WeTaskFissionStaffServiceImpl extends ServiceImpl<WeTaskFissionStaffMapper, WeTaskFissionStaff> implements IWeTaskFissionStaffService {

public WeTaskFissionStaffServiceImpl() {}

@Autowired
private WeTaskFissionStaffMapper weTaskFissionStaffMapper;

    @Override
    public List<WeTaskFissionStaff> getListByTaskId(Long taskId) {
        return list(new LambdaQueryWrapper<WeTaskFissionStaff>()
                .eq(WeTaskFissionStaff::getTaskFissionId,taskId)
                .eq(WeTaskFissionStaff::getDelFlag,0));
    }

    @Override
    public void delStaffByTaskId(Long taskId) {
        WeTaskFissionStaff weTaskFissionStaff = new WeTaskFissionStaff();
        weTaskFissionStaff.setDelFlag(1);
        update(weTaskFissionStaff,new LambdaQueryWrapper<WeTaskFissionStaff>()
                .eq(WeTaskFissionStaff::getTaskFissionId,taskId));
    }

    @Override
    public void delStaffByTaskIds(List<Long> taskIds) {
        WeTaskFissionStaff weTaskFissionStaff = new WeTaskFissionStaff();
        weTaskFissionStaff.setDelFlag(1);
        update(weTaskFissionStaff,new LambdaQueryWrapper<WeTaskFissionStaff>()
                .in(WeTaskFissionStaff::getTaskFissionId,taskIds));
    }
}
