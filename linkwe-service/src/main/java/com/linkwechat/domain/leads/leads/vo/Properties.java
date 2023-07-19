package com.linkwechat.domain.leads.leads.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义字段属性
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 15:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Properties {

    /**
     * 线索模板表Id
     */
    private Long id;

    /**
     * 表项名称
     */
    private String key;

    /**
     * 表项Id
     */
    private String keyEn;

    /**
     * 值
     */
    private String value;

    /**
     * 表项名称
     */
    private String name;
}
