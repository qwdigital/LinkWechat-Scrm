package com.linkwechat.domain.wecom.query.third.auth;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 设置授权配置
 * @date 2022/3/4 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeSetSessionInfoQuery extends WeBaseQuery {
    /**
     * 预授权码
     */
    private String pre_auth_code;

    /**
     * 本次授权过程中需要用到的会话信息
     * 授权类型：0 正式授权， 1 测试授权。 默认值为0。注意，请确保应用在正式发布后的授权类型为“正式授权”
     */
    private JSONObject session_info;
}
