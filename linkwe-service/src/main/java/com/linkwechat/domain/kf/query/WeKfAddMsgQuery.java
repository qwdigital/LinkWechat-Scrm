package com.linkwechat.domain.kf.query;

import com.linkwechat.domain.wx.WxBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author danmo
 * @Description 客服任务入参
 * @date 2021/12/13 10:57
 **/
@ApiModel
@Data
public class WeKfAddMsgQuery extends WxBaseQuery {
    /**
     * 客服ID
     */
    @NotBlank(message = "客服ID不能为空")
    @ApiModelProperty("客服ID")
    private String poolId;


    @NotBlank(message = "类型不能为空")
    @ApiModelProperty("评价类型")
    private String evaluationType;
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @ApiModelProperty("内容")
    private String content;
}
