package com.linkwechat.domain.wecom.vo.third.auth;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 获取第三方应用二维码
 * @date 2022/3/4 11:34
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetQrCodeVo extends WeResultVo {
    /**
     * 二维码url地址
     */
    private String qrCode;
}
