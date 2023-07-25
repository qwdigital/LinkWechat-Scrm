package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordReplyRequest;
import com.linkwechat.domain.leads.record.vo.WeLeadsFollowRecordDetailVO;

/**
 * 线索跟进记录 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:54
 */
public interface IWeLeadsFollowRecordService extends IService<WeLeadsFollowRecord> {

    /**
     * 跟进记录详情
     *
     * @param id 跟进记录Id
     * @return {@link WeLeadsFollowRecordDetailVO}
     * @author WangYX
     * @date 2023/07/24 14:46
     */
    WeLeadsFollowRecordDetailVO detail(Long id);

    /**
     * 回复
     *
     * @param request 回复请求参数
     * @author WangYX
     * @date 2023/07/25 10:16
     */
    void reply(WeLeadsFollowRecordReplyRequest request);


}
