package com.linkwechat.domain.wecom.query;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author danmo
 * @Description 企微请求基础入参
 * @date 2021/12/2 18:27
 **/
@Data
public class WeBaseQuery {

    /**
     * 企业id 必填
     */
    protected String corpid;

    /**
     * 应用id 选填
     */
    protected String agentid;

}
