package com.linkwechat.domain.wecom.query.living;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 直播接口入参
 * @date 2022/10/10 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetUserAllLivingIdQuery extends WeBaseQuery {

    /**
     * 企业成员的userid
     */
    @ApiModelProperty("企业成员的userid")
    private String userid;

    /**
     * 上一次调用时返回的next_cursor，第一次拉取可以不填
     */
    @ApiModelProperty("上一次调用时返回的next_cursor，第一次拉取可以不填")
    private String cursor;

    /**
     * 每次拉取的数据量，建议填20，默认值和最大值都为100
     */
    @ApiModelProperty("每次拉取的数据量，建议填20，默认值和最大值都为100")
    private Integer limit;
}
