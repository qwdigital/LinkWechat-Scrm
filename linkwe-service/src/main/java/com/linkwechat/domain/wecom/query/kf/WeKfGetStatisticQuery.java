package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 客服统计接口入参
 * @date 2022/10/11 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfGetStatisticQuery extends WeBaseQuery {

    @ApiModelProperty("客服帐号ID。不传入时返回的数据为企业维度汇总的数据")
    private String open_kfid;

    @ApiModelProperty("接待人员的userid")
    private String servicer_userid;

    @ApiModelProperty(value = "起始日期的时间戳，填这一天的0时0分0秒")
    private Long start_time;

    @ApiModelProperty(value = "结束日期的时间戳，填这一天的0时0分0秒")
    private Long end_time;
}
