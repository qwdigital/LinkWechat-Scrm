package com.linkwechat.domain.moments.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.Converter;
import com.linkwechat.converter.WeMomentsUserStatusConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 朋友圈员工
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/20 9:45
 */
@Data
public class WeMomentsUserVO {

    /**
     * 主键id
     */
    @ExcelIgnore
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 企微员工id
     */
    @ExcelIgnore
    @ApiModelProperty(value = "企微员工id")
    private String weUserId;

    /**
     * 员工名称
     */
    @ColumnWidth(20)
    @ExcelProperty("执行人名称")
    @ApiModelProperty(value = "员工名称")
    private String userName;

    /**
     * 部门id
     */
    @ExcelIgnore
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    /**
     * 部门名称
     */
    @ColumnWidth(20)
    @ExcelProperty("执行人所属部门")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 执行状态:0未执行，1已执行
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "执行状态", converter = WeMomentsUserStatusConverter.class)
    @ApiModelProperty(value = "执行状态:0未执行，1已执行")
    private Integer executeStatus;


    /**
     * 提醒执行次数
     */
    @ColumnWidth(20)
    @ExcelProperty("提醒执行次数")
    @ApiModelProperty(value = "提醒执行次数")
    private Integer executeCount;
}
