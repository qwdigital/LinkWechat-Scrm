package com.linkwechat.domain.task.query;

import lombok.Data;

/**
 * 待办任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/24 9:55
 */
@Data
public class WeTasksRequest {

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 员工企微Id
     */
    private String weUserId;


    /**
     * 线索中心-线索Id
     */
    private Long leadsId;


}
