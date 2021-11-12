package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeSynchRecord;
import com.linkwechat.wecom.mapper.WeSynchRecordMapper;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import org.springframework.stereotype.Service;

@Service
public class WeSynchRecordServiceImpl extends ServiceImpl<WeSynchRecordMapper, WeSynchRecord> implements IWeSynchRecordService {
}
