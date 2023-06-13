package com.linkwechat.domain.wecom.query.third.auth;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 获取企业永久授权码
 * @date 2022/3/4 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetPermanentCodeQuery extends WeBaseQuery {
    /**
     * 临时授权码
     */
    private String auth_code;


    public WeGetPermanentCodeQuery() {
    }

    /**
     * 设置参入
     * @param authCode 临时授权码
     */
    public WeGetPermanentCodeQuery(String authCode) {
        this.auth_code = authCode;
    }
}
