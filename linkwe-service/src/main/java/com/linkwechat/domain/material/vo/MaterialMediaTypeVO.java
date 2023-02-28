package com.linkwechat.domain.material.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 素材媒体类型
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/21 14:33
 */
@Data
@Builder
public class MaterialMediaTypeVO {

    /**
     * 素材媒体类型
     */
    private Integer type;

    /**
     * 素材媒体类型名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
