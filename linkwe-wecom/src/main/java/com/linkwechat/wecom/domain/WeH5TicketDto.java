package com.linkwechat.wecom.domain;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

/**
 * @author danmo
 * @description h5签名
 * @date 2020/12/3 10:26
 **/
@Data
public class WeH5TicketDto extends WeResultDto {
    /**
     * 生成签名所需的jsapi_ticket，最长为512字节
     */
    private String ticket;
    /**
     * 凭证的有效时间（秒）
     */
    private Integer expiresIn;
}
