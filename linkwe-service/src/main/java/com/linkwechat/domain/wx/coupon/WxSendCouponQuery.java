package com.linkwechat.domain.wx.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @date 2023年03月15日 14:18
 */
@ApiModel
@Data
public class WxSendCouponQuery {

    @ApiModelProperty(value = "批次号")
    private String stock_id;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "商户单据号(商户id+日期+流水号)",hidden = true)
    private String out_request_no;

    @ApiModelProperty(value = "公众账号ID ")
    private String appid;

    @ApiModelProperty(value = "所属商户号")
    private String stock_creator_mchid;

    @ApiModelProperty(value = "指定面额发券，面额(暂未开放)",hidden = true)
    private String coupon_value;

    @ApiModelProperty(value = "指定面额发券，券门槛(暂未开放)",hidden = true)
    private String coupon_minimum;
}
