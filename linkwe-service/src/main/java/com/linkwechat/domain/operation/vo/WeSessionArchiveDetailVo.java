package com.linkwechat.domain.operation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 客户分析
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeSessionArchiveDetailVo {

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户头像")
    private String customerAvatar;

    @ApiModelProperty("同意会话时间")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date openChatTime;
}
