package com.linkwechat.domain.operation.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客群实时数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeGroupRealCntVo {

    @ApiModelProperty("日期")
    @Excel(name = "日期")
    @ExcelProperty("日期")
    private String xTime;

    @ApiModelProperty("客群总数")
    @Excel(name = "客群总数")
    @ExcelProperty("客群总数")
    private Integer totalCnt;

    @ApiModelProperty("新增客群数")
    @Excel(name = "新增客群数")
    @ExcelProperty("新增客群数")
    private Integer addCnt;

    @ApiModelProperty("解散客群数")
    @Excel(name = "解散客群数")
    @ExcelProperty("解散客群数")
    private Integer dissolveCnt;

}
