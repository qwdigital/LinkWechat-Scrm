package com.linkwechat.domain.wecom.query.customer.moment;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @Description 获取企业全部的发表列表
 * @date 2021/12/2 16:11
 **/
@ApiModel
@Data
public class WeMomentQuery extends WeBaseQuery {

    @ApiModelProperty("朋友圈记录开始时间")
    private Long start_time;

    @ApiModelProperty("朋友圈记录结束时间")
    private Long end_time;

    @ApiModelProperty("朋友圈创建人的userid")
    private String creator;

    @ApiModelProperty("朋友圈类型。0：企业发表 1：个人发表 2：所有")
    private Integer filter_type = 1;

    @ApiModelProperty("用于分页查询的游标")
    private String cursor;

    @ApiModelProperty("返回的最大记录数")
    private Integer limit = 100;

    @ApiModelProperty("朋友圈id")
    private String moment_id;

    @ApiModelProperty("企业发表成员userid")
    private String userid;
}
