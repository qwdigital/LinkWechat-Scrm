package com.linkwechat.domain.leads.leads.query;


import com.linkwechat.domain.leads.sea.query.VisibleRange;
import lombok.Data;

import java.util.List;

/**
 * 线索批量分配 请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/07 13:39
 */
@Data
public class WeLeadsAllocationRequest extends WeLeadsBaseRequest {

    /**
     * 线索是否全选（全选true，不全选false)
     */
    private boolean all = false;

    /**
     * 公海ID,all为true时，必填
     */
    private Long seaId;

    /**
     * 线索Id集合,all为false时，必填
     */
    private List<Long> leadsIds;

    /**
     * 全选时未被选中的线索Id
     */
    private List<Long> unLeadsIds;

    /**
     * 部门，包含企微的部门Id
     */
    private VisibleRange.DeptRange depts;

    /**
     * 岗位，包含岗位名称
     */
    private VisibleRange.PostRange posts;

    /**
     * 用户，企微的用户id
     */
    private VisibleRange.UserRange weUserIds;
}
