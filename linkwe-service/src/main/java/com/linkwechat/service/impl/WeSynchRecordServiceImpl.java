package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeSynchRecord;
import com.linkwechat.mapper.WeSynchRecordMapper;
import com.linkwechat.service.IWeSynchRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 同步记录表
 *
 * @author
 * @version 1.0.0
 * @date 2023/06/06 18:25
 */
@Service
public class WeSynchRecordServiceImpl extends ServiceImpl<WeSynchRecordMapper, WeSynchRecord> implements IWeSynchRecordService {

    @Override
    public Date findUpdateLatestTime(int synchType) {
        List<WeSynchRecord> records = this.list(new LambdaQueryWrapper<WeSynchRecord>()
                .eq(WeSynchRecord::getSynchType, synchType)
                .orderByDesc(WeSynchRecord::getSynchTime));
        if (CollectionUtil.isNotEmpty(records)) {
            return records.stream().findFirst().get().getSynchTime();
        }

        return null;
    }
}