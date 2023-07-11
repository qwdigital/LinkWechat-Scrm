package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

/**
 * 自定义字段属性
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 15:12
 */
@Data
public class Properties {

    private Long id;

    private String key;

    private String keyEn;

    private String value;

    private String name;
}
