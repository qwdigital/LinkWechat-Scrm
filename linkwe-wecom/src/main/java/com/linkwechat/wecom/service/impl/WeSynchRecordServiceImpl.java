package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeSynchRecord;
import com.linkwechat.wecom.mapper.WeSynchRecordMapper;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WeSynchRecordServiceImpl extends ServiceImpl<WeSynchRecordMapper, WeSynchRecord> implements IWeSynchRecordService {


    @Override
    public Date findUpdateLatestTime(int synchType) {
        List<WeSynchRecord> records = this.list(new LambdaQueryWrapper<WeSynchRecord>()
                .eq(WeSynchRecord::getSynchType, synchType)
                .orderByDesc(WeSynchRecord::getSynchTime));
        if(CollectionUtil.isNotEmpty(records)){
           return records.stream().findFirst().get().getSynchTime();
        }

        return null;
    }
}
