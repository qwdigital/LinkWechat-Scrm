package com.linkwechat.wecom.domain.weixin.dto;

import lombok.Data;

/**
 * @author danmo
 * @description
 * @date 2021/4/5 15:59
 **/
@Data
public class WxBaseResultDto {
    private Integer errcode;
    private String errmsg;
}
