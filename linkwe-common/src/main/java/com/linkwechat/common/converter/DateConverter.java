package com.linkwechat.common.converter;

import java.util.Date;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @Author: lxj
 * @Date: 2022/7/19
 * @Description: yyyy-MM-dd easyExcel 日期转换
 */
public class DateConverter implements Converter<Date> {

    private static  final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Class<?> supportJavaTypeKey() {
        return Converter.super.supportJavaTypeKey();
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return Converter.super.supportExcelTypeKey();
    }

    @Override
    public WriteCellData<?> convertToExcelData(Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
        String dateValue = sdf.format(value);
        return new WriteCellData<>(dateValue);
    }
}

