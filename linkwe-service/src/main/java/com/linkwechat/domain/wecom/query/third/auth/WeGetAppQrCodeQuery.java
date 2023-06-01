package com.linkwechat.domain.wecom.query.third.auth;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 获取应用推广二维码
 * @date 2022/3/4 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetAppQrCodeQuery extends WeBaseQuery {
    /**
     * 第三方应用id
     */
    private String appid;

    /**
     * state值，用于区分不同的安装渠道
     */
    private String state;

    /**
     * 二维码样式选项，默认为不带说明外框小尺寸。0：带说明外框的二维码，适合于实体物料，1：带说明外框的二维码，适合于屏幕类，2：不带说明外框（小尺寸），3：不带说明外框（中尺寸），4：不带说明外框（大尺寸）
     */
    private Integer style = 3;

    /**
     * 结果返回方式，默认为返回二维码图片buffer。1：二维码图片buffer，2：二维码图片url
     */
    private Integer result_type = 2;

    public WeGetAppQrCodeQuery() {
    }

    /**
     * 设置参入
     * @param suiteId 第三方应用id
     * @param state state值
     */
    public WeGetAppQrCodeQuery(String suiteId, String state) {
        this.state = state;
    }
}
