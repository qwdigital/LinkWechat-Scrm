package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服接口入参
 * @date 2021/12/13 10:27
 **/
@ApiModel
@Data
public class QwKfListQuery extends WeBaseQuery {

    @ApiModelProperty("分页，偏移量, 默认为0")
    private Integer offset = 0;

    @ApiModelProperty("分页，预期请求的数据量，默认为100，取值范围 1 ~ 100")
    private Integer limit = 100;
}
