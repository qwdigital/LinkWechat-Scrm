package com.linkwechat.domain.taskfission.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 任务裂变客户对象
 *
 * @author danmo
 * @date 2022-07-01
 */
@ApiModel
@Data
public class WeTaskFissionCustomerVo {

    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
     * 兑奖链接
     */
    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

    /**
     * 兑奖链接图片
     */
    @ApiModelProperty(value = "客户头像")
    private String avatar;
}
