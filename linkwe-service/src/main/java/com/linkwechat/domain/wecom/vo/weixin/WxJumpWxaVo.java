package com.linkwechat.domain.wecom.vo.weixin;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 *   获取 scheme 码返回值
 * @date 2023年01月06日 17:05
 */
@ApiModel
@Data
public class WxJumpWxaVo extends WeResultVo {

    @ApiModelProperty("生成的小程序 scheme 码")
    private String openLink;
}
