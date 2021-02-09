package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/27 16:42
 */
@Data
public class WeTaskFissionPosterDTO {
    private String userId;
    private Long taskFissionId;
    private String fissionTargetId;
    private Long posterId;
}
