package com.linkwechat.domain.moments.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JobId换MomentsId的mq参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/13 10:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMomentsJobIdToMomentsIdRequest {

    /**
     * 异步任务id,24小时有效
     */
    private String jobId;

    /**
     * mq执行的次数
     */
    private int num;
}
