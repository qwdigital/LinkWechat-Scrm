package com.linkwechat.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeTaskFissionCompleteRecordMapper;
import com.linkwechat.domain.WeTaskFissionCompleteRecord;
import com.linkwechat.service.IWeTaskFissionCompleteRecordService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 裂变任务完成记录(WeTaskFissionCompleteRecord)
 *
 * @author danmo
 * @since 2022-07-19 10:21:08
 */
@Service
public class WeTaskFissionCompleteRecordServiceImpl extends ServiceImpl<WeTaskFissionCompleteRecordMapper, WeTaskFissionCompleteRecord> implements IWeTaskFissionCompleteRecordService {

public WeTaskFissionCompleteRecordServiceImpl() {}

@Autowired
private WeTaskFissionCompleteRecordMapper weTaskFissionCompleteRecordMapper; 
}
