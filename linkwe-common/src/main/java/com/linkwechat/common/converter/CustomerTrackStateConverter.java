package com.linkwechat.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * 客户商机阶段
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/23 16:43
 */
public class CustomerTrackStateConverter implements Converter<Integer> {
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        //1:待跟进; 2:跟进中;3:已成交;4:无意向;5:已流失
        Integer value = context.getValue();
        String result = null;
        switch (value) {
            case 1:
                result = "待跟进";
                break;
            case 2:
                result = "跟进中";
                break;
            case 3:
                result = "已成交";
                break;
            case 4:
                result = "无意向";
                break;
            case 5:
                result = "已流失";
                break;
        }
        return new WriteCellData<>(result);
    }
}
