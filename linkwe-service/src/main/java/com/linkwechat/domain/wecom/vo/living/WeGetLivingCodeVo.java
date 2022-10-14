package com.linkwechat.domain.wecom.vo.living;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 创建直播返回对象
 * @date 2022年10月11日 16:09
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetLivingCodeVo extends WeResultVo {

    /**
     * 微信观看直播凭证，5分钟内可以重复使用，且仅能在微信上使用。开发者获取到该凭证后可以在微信H5页面或小程序进入直播或直播回放页
     */
    @ApiModelProperty("微信观看直播凭证，5分钟内可以重复使用，且仅能在微信上使用。开发者获取到该凭证后可以在微信H5页面或小程序进入直播或直播回放页")
    private String livingCode;
}
