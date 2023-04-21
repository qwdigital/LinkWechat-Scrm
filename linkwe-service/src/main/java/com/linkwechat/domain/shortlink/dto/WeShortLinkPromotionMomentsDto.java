package com.linkwechat.domain.shortlink.dto;

import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.domain.WeMoments;
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
public class WeShortLinkPromotionMomentsDto extends WeMoments {

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

    /**
     * 当前用户
     */
    @ApiModelProperty(value = "当前用户", hidden = true)
    private LoginUser loginUser;


}
