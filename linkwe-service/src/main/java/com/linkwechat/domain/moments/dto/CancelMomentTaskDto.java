package com.linkwechat.domain.moments.dto;

import lombok.Data;

/**
 * 停止发表企业朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/16 14:08
 */
@Data
public class CancelMomentTaskDto {

    /**
     * 朋友圈id，可通过获取客户朋友圈企业发表的列表接口获取朋友圈企业发表的列表
     */
    private String moment_id;
}
