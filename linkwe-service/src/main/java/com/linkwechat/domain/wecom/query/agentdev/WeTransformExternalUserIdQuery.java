package com.linkwechat.domain.wecom.query.agentdev;

import lombok.Data;

/**
 * @author leejoker
 */
@Data
public class WeTransformExternalUserIdQuery {
    /**
     * 代开发自建应用的调用接口凭证
     */
    private String access_token;
    /**
     * 代开发自建应用获取到的外部联系人ID
     */
    private String external_userid;
}
