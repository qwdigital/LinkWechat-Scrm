package com.linkwechat.domain.wecom.query.customer.tag;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;

import java.util.List;

/**
 * @author danmo
 * @Description 获取企业标签列表入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCorpTagListQuery extends WeBaseQuery {
    /**
     * 要查询的标签id
     */
    private List<String> tag_id;

    /**
     * 要查询的标签组id，返回该标签组以及其下的所有标签信息
     */
    private List<String> group_id;



}
