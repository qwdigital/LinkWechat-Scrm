package com.linkwechat.domain.wecom.entity.customer.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @Description 标签
 * @date 2021/12/3 0:13
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCorpTagEntity {
    /**
     * 标签id
     */
    private String id;

    /**
     * 标签名称
     */
    private String name;
    /**
     * 标签创建时间
     */
    private Long create_time;

    /**
     * 标签排序的次序值，order值大的排序靠前。有效的值范围是[0, 2^32)
     */
    private Integer order;

    /**
     * 标签是否已经被删除，只在指定tag_id/group_id进行查询时返回
     */
    private Boolean deleted;
}
