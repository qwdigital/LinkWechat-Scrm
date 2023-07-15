package com.linkwechat.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;
import lombok.Data;

/**
 * 朋友圈客户送达状态转换
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/20 15:14
 */
@Data
public class WeMomentsCustomerStatusConverter implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        //对象属性转CellData
        Integer cellValue = context.getValue();
        if (cellValue == null) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(cellValue.equals(0) ? "已送达" : "未送达");
    }

}
