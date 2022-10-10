package com.linkwechat.domain.wecom.query.third.auth;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

/**
 * @author danmo
 * @description 获取应用的管理员列表
 * @date 2022/3/4 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAuthAdminQuery extends WeBaseQuery {
    /**
     * 授权企业id
     */
    private String auth_corpid;

    public WeAuthAdminQuery() {
    }

    /**
     * 设置参入
     * @param corpId 授权企业id
     * @param agentId 应用ID
     */
    public WeAuthAdminQuery(String corpId, String agentId) {
        this.setCorpid(corpId);
        this.setAgentid(agentId);
        this.auth_corpid = corpId;
    }
}
