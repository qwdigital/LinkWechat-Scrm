package com.linkwechat.domain.wecom.query;

import lombok.Data;

/**
 * @author danmo
 * @Description 获取凭证
 * @date 2021/12/2 18:27
 **/
@Data
public class WeCorpTokenQuery extends WeBaseQuery{

    /**
     * 授权方corpid
     */
    private String auth_corpid;

    /**
     * 永久授权码
     */
    private String permanent_code;

}
