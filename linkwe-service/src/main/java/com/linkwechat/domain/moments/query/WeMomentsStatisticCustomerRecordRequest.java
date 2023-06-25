package com.linkwechat.domain.moments.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 朋友圈统计-客户记录列表请求参数
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/14 18:12
 */
@Data
@ApiModel("朋友圈统计-客户记录列表请求参数")
public class WeMomentsStatisticCustomerRecordRequest {

    /**
     * 朋友圈任务Id
     */
    @NotNull(message = "朋友圈任务Id必填")
    @ApiModelProperty(value = "朋友圈任务Id")
    private Long weMomentsTaskId;

    /**
     * 企微员工Id集合
     */
    @ApiModelProperty(value = "企微员工Id集合")
    private String weUserIds;

    /**
     * 送达状态 0已送达 1未送达
     */
    @ApiModelProperty(value = "送达状态 0已送达 1未送达")
    private Integer deliveryStatus;

}
