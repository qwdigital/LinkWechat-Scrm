package com.linkwechat.domain.qirule.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会话质检周报详情列表出参
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/

@ApiModel
@Data
public class WeQiRuleWeeklyDetailListVo {

    @ApiModelProperty("成员ID")
    @ExcelIgnore
    private String userId;

    @ApiModelProperty("成员名称")
    @ExcelProperty("成员名称")
    private String userName;

    @ApiModelProperty("所属部门")
    @ExcelProperty("所属部门")
    private String deptName;

    @ApiModelProperty(value = "客户会话数")
    @ExcelIgnore
    private String chatNum;

    @ApiModelProperty(value = "客群会话数")
    @ExcelIgnore
    private String groupChatNum;

    @ApiModelProperty(value = "成员回复次数")
    @ExcelIgnore
    private String replyNum;

    @ApiModelProperty(value = "成员超时次数")
    @ExcelProperty("超时次数")
    private String timeOutNum;

    @ApiModelProperty(value = "成员超时率")
    @ExcelProperty("超时率")
    private String timeOutRate;

    @ApiModelProperty(value = "客户会话超时率")
    @ExcelProperty("客户会话超时率")
    private String chatTimeOutRate;

    @ApiModelProperty(value = "客群会话超时率")
    @ExcelProperty("客群会话超时率")
    private String groupChatTimeOutRate;
}
