package com.linkwechat.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * 朋友圈执行员工状态转换
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/20 12:48
 */
public class WeMomentsUserStatusConverter implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        //对象属性转CellData
        Integer cellValue = context.getValue();
        if (cellValue == null) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(cellValue.equals(0) ? "未执行" : "已执行");
    }

}
