package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeSynchRecord;
import java.util.Date;

public interface IWeSynchRecordService extends IService<WeSynchRecord> {
    Date findUpdateLatestTime(int synchType);
}
