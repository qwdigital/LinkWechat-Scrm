package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeSynchRecord;

import java.util.Date;

public interface IWeSynchRecordService extends IService<WeSynchRecord> {
    Date findUpdateLatestTime(int synchType);
}
