package com.linkwechat.domain.kf.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服客户场景实时数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfSceneRealCntVo {

    @ApiModelProperty("时间")
    @ExcelProperty("日期")
    private String xTime;

    @ApiModelProperty("访问客户总数")
    @ExcelProperty("访问客户总数")
    private Integer visitTotalCnt;

    @ApiModelProperty("咨询客户总数")
    @ExcelProperty("咨询客户总数")
    private Integer consultTotalCnt;

    @ApiModelProperty("接待客户总数")
    @ExcelProperty("接待客户总数")
    private Integer receptionTotalCnt;

    @ApiModelProperty("今日新增访问客户")
    @ExcelProperty("今日新增访问客户")
    private Integer tdVisitCnt;

    @ApiModelProperty("今日新增咨询客户")
    @ExcelProperty("今日新增咨询客户")
    private Integer tdConsultCnt;

    @ApiModelProperty("今日新增接待客户")
    @ExcelProperty("今日新增接待客户")
    private Integer tdReceptionCnt;

}
