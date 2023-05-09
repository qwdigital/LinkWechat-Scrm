package com.linkwechat.domain.corp.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 企业信息
 * @date 2022/4/11 23:11
 **/
@ApiModel
@Data
public class WeCorpAccountQuery {

    @ApiModelProperty("企业ID")
    private String corpId;

}
