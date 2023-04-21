package com.linkwechat.domain.shortlink.dto;

import com.linkwechat.domain.msg.QwAppMsgBody;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短链推广-朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/14 9:48
 */
@Data
public class WeShortLinkPromotionAppMsgDto extends QwAppMsgBody {

    /**
     * 短链推广Id
     */
    @ApiModelProperty(value = "短链推广Id")
    private Long shortLinkPromotionId;

    /**
     * 短链推广模板Id-朋友圈
     */
    @ApiModelProperty(value = "短链推广模板Id-朋友圈")
    private Long businessId;

}
