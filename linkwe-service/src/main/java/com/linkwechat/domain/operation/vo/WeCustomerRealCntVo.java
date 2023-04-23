package com.linkwechat.domain.operation.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客户实时数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeCustomerRealCntVo {

    @ApiModelProperty("日期")
    @Excel(name = "日期")
    @ExcelProperty("日期")
    private String xTime;

    @ApiModelProperty("客户总数")
    @Excel(name = "客户总数")
    @ExcelProperty("客户总数")
    private Integer totalCnt;

    @ApiModelProperty("新增客户数")
    @Excel(name = "新增客户数")
    @ExcelProperty("新增客户数")
    private Integer addCnt;

    //跟进客户数
    @ExcelIgnore
    private Integer gjCnt;

    @ApiModelProperty("流失客户数")
    @Excel(name = "流失客户数")
    @ExcelProperty("流失客户数")
    private Integer lostCnt;

    @ApiModelProperty("净增客户数")
    @Excel(name = "净增客户数")
    @ExcelProperty("净增客户数")
    private Integer netCnt;

    @ApiModelProperty("发送申请数")
    @Excel(name = "发送申请数")
    @ExcelProperty("发送申请数")
    private Integer applyCnt;

}
