package com.linkwechat.common.core.domain.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author danmo
 * @description
 * @date 2020/12/9 14:18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchQueryVo {
    /**
     * 索引名
     */
    private String idxName;
    /**
     * 需要反射的实体类型，用于对查询结果的封装
     */
    private String className;
    /**
     * 具体条件
     */
    private Map<String, Map<String,Object>> query;
}
