package com.linkwechat.domain.wecom.query.customer.tag;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

/**
 * @author danmo
 * @Description 编辑企业客户标签入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeUpdateCorpTagQuery extends WeBaseQuery {
    /**
     * 标签或标签组的id
     */
    private String id;

    /**
     * 新的标签或标签组名称，最长为30个字符
     */
    private String name;

    /**
     * 标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)
     */
    private Integer order;
}
