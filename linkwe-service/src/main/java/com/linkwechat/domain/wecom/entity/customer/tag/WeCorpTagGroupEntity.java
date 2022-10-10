package com.linkwechat.domain.wecom.entity.customer.tag;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 标签组
 * @date 2021/12/3 0:13
 **/
@Data
public class WeCorpTagGroupEntity {
    /**
     * 标签组id
     */
    private String group_id;

    /**
     * 标签组名称
     */
    private String group_name;
    /**
     * 标签组创建时间
     */
    private Long create_time;

    /**
     * 标签组排序的次序值，order值大的排序靠前。有效的值范围是[0, 2^32)
     */
    private Integer order;

    /**
     * 标签组是否已经被删除，只在指定tag_id进行查询时返回
     */
    private Boolean deleted;

    /**
     * 标签组内的标签列表
     */
    private List<WeCorpTagEntity> tag;
}
