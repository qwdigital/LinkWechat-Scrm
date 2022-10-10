package com.linkwechat.domain.operation.vo;

import com.linkwechat.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客户聊天总数
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeSessionGroupTotalCntVo {

    @ApiModelProperty("日期")
    @Excel(name = "日期")
    private String xTime;

    @ApiModelProperty("群聊总数")
    @Excel(name = "群聊总数")
    private Integer chatTotal;

    @ApiModelProperty("群聊消息数")
    @Excel(name = "群聊消息数")
    private Integer msgTotal;

    @ApiModelProperty("发送消息群成员数")
    @Excel(name = "发送消息群成员数")
    private Integer memberHasMsg;
}
