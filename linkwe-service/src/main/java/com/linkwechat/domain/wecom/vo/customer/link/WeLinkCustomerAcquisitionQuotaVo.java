package com.linkwechat.domain.wecom.vo.customer.link;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

/**
 * 查询剩余使用量
 */
@Data
public class WeLinkCustomerAcquisitionQuotaVo extends WeResultVo {

    /**
     * 历史累计使用量
     */
    private long total;

    /**
     *剩余使用量
     */
    private long balance;
}
