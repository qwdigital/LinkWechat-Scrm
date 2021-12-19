package com.linkwechat.wecom.domain.vo.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 标签出参
 * @date 2021/11/8 21:34
 **/
@ApiModel
@Data
public class WeTagVo {

    @ApiModelProperty("标签Id")
    private String tagId;

    @ApiModelProperty("标签名称")
    private String tagName;
}
