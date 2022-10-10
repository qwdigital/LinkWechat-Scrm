package com.linkwechat.domain.wecom.query;

import lombok.Data;

/**
 * @author danmo
 * @Description 获取凭证
 * @date 2021/12/2 18:27
 **/
@Data
public class WeSuiteTokenQuery{
    /**
     * 应用id
     */
    private String suite_id;

    /**
     * 应用secret
     */
    private String suite_secret;

    /**
     * 企业微信后台推送的ticket
     */
    private String suite_ticket;
}
