package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeTaskFissionRecord;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionProgressVo;

import java.util.Date;
import java.util.List;

/**
 * 裂变任务记录(WeTaskFissionRecord)
 *
 * @author danmo
 * @since 2022-06-28 13:48:54
 */
public interface IWeTaskFissionRecordService extends IService<WeTaskFissionRecord> {

    WeTaskFissionRecord getTaskFissionRecord(Long taskFissionId, String unionId, String nickName);

    List<WeTaskFissionRecord> selectWeTaskFissionRecordList(WeTaskFissionRecord weTaskFissionRecord);

    void updateWeTaskFissionRecord(WeTaskFissionRecord record);

    WeTaskFissionProgressVo getTaskProgress(Long taskFissionId, String unionId);

    List<WeTaskFissionRecord> statisticRecords(Long taskFissionId, Date startTime, Date endTime);
}
