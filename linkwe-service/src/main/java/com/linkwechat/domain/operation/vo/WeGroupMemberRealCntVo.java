package com.linkwechat.domain.operation.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客群成员实时数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeGroupMemberRealCntVo {

    @ApiModelProperty("日期")
    @Excel(name = "日期")
    @ExcelProperty("日期")
    private String xTime;

    @ApiModelProperty("客群成员总数")
    @Excel(name = "客群成员总数")
    @ExcelProperty("客群成员总数")
    private Integer totalCnt;

    @ApiModelProperty("新增客群成员数")
    @Excel(name = "新增客群成员数")
    @ExcelProperty("新增客群成员数")
    private Integer addCnt;

    @ApiModelProperty("流失客群成员数")
    @Excel(name = "流失客群成员数")
    @ExcelProperty("流失客群成员数")
    private Integer quitCnt;

}
