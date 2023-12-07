package com.linkwechat.domain.leads.sea.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;


/**
 * @author caleb
 * @since 2023/5/9
 */
@Data
@HeadRowHeight(30)
@ColumnWidth(20)
public class WeLeadsSeaDataDetailVO {

    @ExcelProperty(value = "日期", index = 0)
    private String dateTime;

    @ExcelProperty(value = "总领取量", index = 1)
    private Integer allReceiveNum;

    @ExcelProperty(value = "总跟进量", index = 2)
    private Integer allFollowNum;

    @ExcelProperty(value = "当日领取量", index = 3)
    private Integer todayReceiveNum;

    @ExcelProperty(value = "当日跟进量", index = 4)
    private Integer todayFollowNum;

    @ExcelProperty(value = "当日跟进率", index = 5)
    private String todayFollowRatio;

}
