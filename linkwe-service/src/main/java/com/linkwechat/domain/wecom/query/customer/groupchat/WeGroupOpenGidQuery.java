package com.linkwechat.domain.wecom.query.customer.groupchat;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 客户群opengid转换入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGroupOpenGidQuery extends WeBaseQuery {
    /**
     * 小程序在微信获取到的群ID
     */
    private String opengid;

}
