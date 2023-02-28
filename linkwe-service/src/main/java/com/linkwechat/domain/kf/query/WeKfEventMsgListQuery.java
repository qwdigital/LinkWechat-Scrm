package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/1/24 19:05
 */
@ApiModel
@Data
public class WeKfEventMsgListQuery extends BaseEntity {
    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("接待场景id")
    private List<String> scenesIds;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("接待员工id")
    private List<String> userIds;

    @ApiModelProperty("是否为企业客户: 0-否 1-是")
    private Integer corpCustomer;
}
