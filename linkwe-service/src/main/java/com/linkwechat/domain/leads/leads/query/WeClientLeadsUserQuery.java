package com.linkwechat.domain.leads.leads.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 线索
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/04 16:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeClientLeadsUserQuery extends com.linkwechat.domain.leads.query.WeClientLeadsBaseQuery {

    /**
     * 所处tab：1我的线索，2历史任务，3我的引流，4所属公海
     */
    @NotNull(message = "所处tab必填")
    private Integer position;

    /**
     * 用户Id
     */
    private Long userId;

}
