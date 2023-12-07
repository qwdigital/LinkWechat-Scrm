package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeadsManualAddRecord;
import com.linkwechat.domain.leads.leads.query.WeLeadsAddRequest;

/**
 * <p>
 * 线索手动入线记录 服务类
 * </p>
 *
 * @author WangYX
 * @since 2023-07-25
 */
public interface IWeLeadsManualAddRecordService extends IService<WeLeadsManualAddRecord> {

    /**
     * 线索手动新增
     *
     * @param request 请求参数
     * @return {@link Long} 线索Id
     * @author WangYX
     * @date 2023/07/25 17:34
     */
    Long manualAdd(WeLeadsAddRequest request);

}
