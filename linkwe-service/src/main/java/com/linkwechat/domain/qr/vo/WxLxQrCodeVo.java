package com.linkwechat.domain.qr.vo;

import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.tag.vo.WeTagVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 拉新活码详情出参
 * @date 2023/03/07 22:15
 **/
@ApiModel
@Data
public class WxLxQrCodeVo {

    @ApiModelProperty(value = "二维码地址")
    private String qrCode;

}
