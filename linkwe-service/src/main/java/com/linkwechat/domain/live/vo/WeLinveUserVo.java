package com.linkwechat.domain.live.vo;

import com.linkwechat.domain.live.WeLive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLinveUserVo {
    private String liveCode;
    private WeLive weLive;
}
