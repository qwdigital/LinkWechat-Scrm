package com.linkwechat.domain.moments.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.linkwechat.converter.WeMomentsCustomerStatusConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 朋友圈客户列表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/20 15:09
 */
@Data
public class WeMomentsCustomerVO {

    /**
     * 员工名称
     */
    @ColumnWidth(20)
    @ExcelProperty("对应成员")
    @ApiModelProperty(value = "员工名称")
    private String userName;

    /**
     * 客户名称
     */
    @ColumnWidth(20)
    @ExcelProperty("客户名称")
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 送达状态 0已送达 1未送达
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "送达状态", converter = WeMomentsCustomerStatusConverter.class)
    @ApiModelProperty(value = "送达状态")
    private Integer deliveryStatus;

}
