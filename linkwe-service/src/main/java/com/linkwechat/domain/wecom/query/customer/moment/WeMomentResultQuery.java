package com.linkwechat.domain.wecom.query.customer.moment;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 朋友圈创建结果
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeMomentResultQuery extends WeBaseQuery {
    /**
     * 发表范围
     */
    private String jobid;
}
