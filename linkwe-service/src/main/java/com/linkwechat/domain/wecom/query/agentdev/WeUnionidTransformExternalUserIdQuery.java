package com.linkwechat.domain.wecom.query.agentdev;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

/**
 * unionid转换为第三方external_userid
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/26 16:28
 */
@Data
public class WeUnionidTransformExternalUserIdQuery extends WeBaseQuery {

    /**
     * 微信客户的unionid
     */
    private String unionid;

    /**
     * 微信客户的openid
     */
    private String openid;

    /**
     * 小程序或公众号的主体类型：
     * 0表示主体名称是企业的，
     * 1表示主体名称是服务商的
     * <p>
     * 默认为1
     */
    private Integer subjectType = 0;
}
