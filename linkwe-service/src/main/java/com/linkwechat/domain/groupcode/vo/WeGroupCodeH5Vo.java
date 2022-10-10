package com.linkwechat.domain.groupcode.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupCodeH5Vo {
    private String activityName;
    private String tipMsg;
    private String guide;
    private String actualQRCode;
    private String isOpenTip;
    private String serviceQrCode;
    private String groupName;
}
