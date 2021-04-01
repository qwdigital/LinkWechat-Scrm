package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 群发参数相关
 * @author: HaoN
 * @create: 2021-03-31 17:20
 **/
@Data
public class TemporaryMaterialDto {
    private  String url;
    private String  type;
    private  String name;
}
