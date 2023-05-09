package com.linkwechat.domain.wecom.query.third.auth;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 获取企业授权信息
 * @date 2022/3/4 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetAuthInfoQuery extends WeBaseQuery {
    /**
     * 授权企业id
     */
    private String auth_corpid;

    /**
     * 永久授权码
     */
    private String permanent_code;


    public WeGetAuthInfoQuery() {
    }

    /**
     * 设置参入
     * @param corpId 授权企业id
     * @param suiteId 应用ID
     */
    public WeGetAuthInfoQuery(String corpId, String suiteId) {
        this.setCorpid(corpId);
        this.auth_corpid = corpId;
    }
}
