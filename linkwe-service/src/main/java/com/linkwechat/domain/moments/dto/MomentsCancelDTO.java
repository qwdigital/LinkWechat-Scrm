package com.linkwechat.domain.moments.dto;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 朋友圈任务取消
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/12 10:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomentsCancelDTO extends WeBaseQuery {
    /**
     * 朋友圈Id
     */
    private String moment_id;
}
