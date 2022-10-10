package com.linkwechat.domain.moments.dto;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomentsInteracteParamDto  extends WeBaseQuery {
    private String moment_id;
    private String userid;
}
