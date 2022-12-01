package com.linkwechat.domain.wecom.callback;

import lombok.Data;

/**
 * 直播回掉
 */
@Data
public class WeBackLiveVo extends WeBackBaseVo{

    //直播企业微信端id
    private String LivingId;
}
