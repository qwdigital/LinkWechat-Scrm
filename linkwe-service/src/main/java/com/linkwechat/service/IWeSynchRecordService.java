package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeSynchRecord;

import java.util.Date;

/**
 * 同步记录表
 *
 * @author
 * @version 1.0.0
 * @date 2023/06/06 18:23
 */
public interface IWeSynchRecordService extends IService<WeSynchRecord> {

    /**
     * 获取同步时间
     *
     * @param synchType 同步类型
     * @return {@link Date}
     * @author
     * @date 2023/06/06 18:24
     */
    Date findUpdateLatestTime(int synchType);
}
