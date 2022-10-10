package com.linkwechat.domain.wecom.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 获取加入企业二维码
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCorpQrVo extends WeResultVo {
    /**
     * 二维码链接，有效期7天
     */
    private String joinQrcode;
}
