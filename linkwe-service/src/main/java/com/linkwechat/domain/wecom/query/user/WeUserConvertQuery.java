package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description userid与openid互换入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserConvertQuery extends WeBaseQuery {

    /**
     * 成员UserID
     */
    private String userid;

    /**
     * 在使用企业支付之后，返回结果的openid
     */
    private String openid;
}
