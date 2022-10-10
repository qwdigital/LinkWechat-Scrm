package com.linkwechat.web.domain.vo;

import lombok.Data;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/19 12:49
 */
@Data
public class CorpVo {

    private String corpId;

    private String corpName;

    private String agentId;

    /**公众号id*/
    private String appId;

    /**公众号密钥*/
    private String secret;

    public CorpVo() {}

    public CorpVo(String corpId, String corpName, String agentId) {
        this.corpId = corpId;
        this.corpName = corpName;
        this.agentId = agentId;

    }
}
