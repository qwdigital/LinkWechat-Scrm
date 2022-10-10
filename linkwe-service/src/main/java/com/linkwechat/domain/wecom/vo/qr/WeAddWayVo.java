package com.linkwechat.domain.wecom.vo.qr;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

/**
 * @author danmo
 * @description 添加活码返回对象
 * @date 2021/12/2 15:57
 **/
@Data
public class WeAddWayVo extends WeResultVo {
    /**
     * 新增联系方式的配置id
     */
    private String configId;

    /**
     * 联系我二维码链接，仅在scene为2时返回
     */
    private String qrCode;
}
