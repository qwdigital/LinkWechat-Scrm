package com.linkwechat.converter;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.linkwechat.common.enums.CustomerAddWay;

/**
 * 客户添加阶段
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/23 16:47
 */
public class CustomerAddWayConverter implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        CustomerAddWay of = CustomerAddWay.of(context.getValue());
        if (BeanUtil.isEmpty(of)) {
            return null;
        }

        return new WriteCellData<>(of.getVal());
    }

}
