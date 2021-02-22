package com.linkwechat.common.core.domain.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author danmo
 * @description
 * @date 2020/12/9 14:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchEntity {
    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private Map data;
}
