package com.linkwechat.wecom.domain.dto.moments;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MomentsInteracteParamDto {
    private String moment_id;
    private String userid;
}
