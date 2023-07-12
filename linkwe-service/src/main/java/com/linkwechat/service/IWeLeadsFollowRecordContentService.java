package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;

/**
 * 线索跟进记录内容表服务接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:55
 */
public interface IWeLeadsFollowRecordContentService extends IService<WeLeadsFollowRecordContent> {

    /**
     * 成员主动领取
     *
     * @param recordId 跟进记录Id
     * @param seaId    公海Id
     * @return
     * @author WangYX
     * @date 2023/07/12 10:13
     */
    void memberToReceive(Long recordId, Long seaId);
}

