package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 统一返回体
 * @author: HaoN
 * @create: 2020-08-27 15:59
 **/
@Data
public class WeResultDto {
    private Long id;
    private Integer errcode;
    private String errmsg;
}
