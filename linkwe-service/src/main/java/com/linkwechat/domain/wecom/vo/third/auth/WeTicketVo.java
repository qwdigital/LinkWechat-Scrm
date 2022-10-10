package com.linkwechat.domain.wecom.vo.third.auth;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 获取jsapi_ticket
 * @date 2022/3/4 11:34
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeTicketVo extends WeResultVo {
    /**
     * 生成签名所需的jsapi_ticket，最长为512字节
     */
    @ApiModelProperty("生成签名所需的jsapi_ticket")
    private String ticket;

    /**
     * 有效期（秒）
     */
    @ApiModelProperty("有效期（秒）")
    private Long expiresIn;
}
