package com.linkwechat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @date 2023年07月06日 22:58
 */
@ApiModel
@Data
public class PostBaseQuery {

    @ApiModelProperty(value = "页码", hidden = true)
    private Integer pageIndex = 1;

    @ApiModelProperty(value = "条数", hidden = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段", hidden = true)
    private String sortField;

    @ApiModelProperty(value = "排序方式 asc、desc", hidden = true)
    private String sort;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间", example = "yyyy-MM-dd", hidden = true)
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间", example = "yyyy-MM-dd", hidden = true)
    private Date endTime;
}
