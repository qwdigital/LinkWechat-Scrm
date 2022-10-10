package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTaskFissionRecord;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionProgressVo;
import com.linkwechat.mapper.WeTaskFissionRecordMapper;
import com.linkwechat.service.IWeTaskFissionRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 裂变任务记录(WeTaskFissionRecord)
 *
 * @author danmo
 * @since 2022-06-28 13:48:54
 */
@Service
public class WeTaskFissionRecordServiceImpl extends ServiceImpl<WeTaskFissionRecordMapper, WeTaskFissionRecord> implements IWeTaskFissionRecordService {

public WeTaskFissionRecordServiceImpl() {}

@Autowired
private WeTaskFissionRecordMapper weTaskFissionRecordMapper;

    @Override
    public WeTaskFissionRecord getTaskFissionRecord(Long taskFissionId, String unionId, String nickName) {
        WeTaskFissionRecord record = WeTaskFissionRecord.builder()
                .taskFissionId(taskFissionId)
                .customerId(unionId)
                .customerName(nickName).build();
        List<WeTaskFissionRecord> searchExists = selectWeTaskFissionRecordList(record);
        WeTaskFissionRecord recordInfo;
        if (CollectionUtils.isNotEmpty(searchExists)) {
            recordInfo = searchExists.get(0);
        } else {
            if (save(record)) {
                recordInfo = record;
            } else {
                throw new WeComException("生成海报异常：插入裂变记录失败");
            }
        }
        return recordInfo;
    }

    @Override
    public List<WeTaskFissionRecord> selectWeTaskFissionRecordList(WeTaskFissionRecord weTaskFissionRecord) {
        LambdaQueryWrapper<WeTaskFissionRecord> wrapper = new LambdaQueryWrapper<>();
        if(weTaskFissionRecord.getTaskFissionId() != null){
            wrapper.eq(WeTaskFissionRecord::getTaskFissionId,weTaskFissionRecord.getTaskFissionId());
        }
        if(StringUtils.isNotEmpty(weTaskFissionRecord.getCustomerId())){
            wrapper.eq(WeTaskFissionRecord::getCustomerId,weTaskFissionRecord.getCustomerId());
        }
        if(weTaskFissionRecord.getFissNum() != null){
            wrapper.eq(WeTaskFissionRecord::getFissNum,weTaskFissionRecord.getFissNum());
        }
        wrapper.eq(WeTaskFissionRecord::getDelFlag,0);
        return list(wrapper);
    }

    @Override
    public void updateWeTaskFissionRecord(WeTaskFissionRecord record) {
        updateById(record);
    }

    @Override
    public WeTaskFissionProgressVo getTaskProgress(Long taskFissionId, String unionId) {
        return this.baseMapper.getTaskProgress(taskFissionId,unionId);
    }

    @Override
    public List<WeTaskFissionRecord> statisticRecords(Long taskFissionId, Date startTime, Date endTime) {
        LambdaQueryWrapper<WeTaskFissionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeTaskFissionRecord::getTaskFissionId, taskFissionId).
                between(WeTaskFissionRecord::getCreateTime, startTime, endTime).
                orderByAsc(WeTaskFissionRecord::getCreateTime);
        return list(wrapper);
    }
}
