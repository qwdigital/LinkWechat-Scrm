package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 客服状态接口入参
 * @date 2021/12/13 10:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfStateQuery extends WeBaseQuery {

    /**
     * 客服帐号ID
     */
    private String open_kfid;

    /**
     * 微信客户的external_userid
     */
    private String external_userid;

    /**
     * 变更的目标状态
     */
    private Integer service_state;

    /**
     * 接待人员的userid, 当state=3时要求必填 openUserId
     */
    private String servicer_userid;
}
