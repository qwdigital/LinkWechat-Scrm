package com.linkwechat.domain.envelopes.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 红包基本信息
 */
@Data
@Builder
public class RedEnvelopesBaseInfoVo {
    //机构名称
    private String corpName;
    //企业logo
    private String logo;

//    //公众号id
//    private String weAppId;
//
//    //公众号密钥
//    private String weAppSecret;
}
