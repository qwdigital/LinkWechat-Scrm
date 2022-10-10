package com.linkwechat.domain.wecom.query.customer.tag;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * @author danmo
 * @Description 编辑客户企业标签入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMarkTagQuery extends WeBaseQuery {
    /**
     * 添加外部联系人的userid
     */
    private String userid;

    /**
     * 外部联系人userid
     */
    private String external_userid;

    /**
     * 要标记的标签列表
     */
    private List<String> add_tag;

    /**
     * 要移除的标签列表
     */
    private List<String> remove_tag;
}
