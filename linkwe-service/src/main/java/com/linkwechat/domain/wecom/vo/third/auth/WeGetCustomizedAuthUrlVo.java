package com.linkwechat.domain.wecom.vo.third.auth;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 获取带参授权链接
 * @date 2022/3/13 11:34
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetCustomizedAuthUrlVo extends WeResultVo {
    /**
     * 可用来生成二维码的授权url，需要开发者自行生成为二维码
     */
    private String qrcodeUrl;

    /**
     * 有效期（秒）。10天过期
     */
    private Long expiresIn;
}
