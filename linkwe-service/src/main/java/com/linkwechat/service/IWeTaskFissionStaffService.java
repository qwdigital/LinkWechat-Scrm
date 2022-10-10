package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeTaskFissionStaff;

import java.util.List;

/**
 * 裂变任务员工列表(WeTaskFissionStaff)
 *
 * @author danmo
 * @since 2022-06-28 13:48:55
 */
public interface IWeTaskFissionStaffService extends IService<WeTaskFissionStaff> {

    List<WeTaskFissionStaff> getListByTaskId(Long taskId);

    void delStaffByTaskId(Long taskId);

    void delStaffByTaskIds(List<Long> taskIds);
}
