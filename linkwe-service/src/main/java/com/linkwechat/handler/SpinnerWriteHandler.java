package com.linkwechat.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.Map;

/**
 * 线索中心-导出模板-实现固定下拉选框
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/13 16:36
 */
public class SpinnerWriteHandler implements SheetWriteHandler {

    /**
     * map的key对应导出列的顺序 从0开始
     * strings为下拉选框的值
     */
    private final Map<Integer, String[]> dropDownMap;

    public SpinnerWriteHandler(Map<Integer, String[]> dropDownMap) {
        this.dropDownMap = dropDownMap;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 获取到当前的sheet
        Sheet sheet = writeSheetHolder.getSheet();
        /// 开始设置下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 设置下拉框
        for (Map.Entry<Integer, String[]> entry : dropDownMap.entrySet()) {
            // 起始行、终止行、起始列、终止列
            CellRangeAddressList addressList = new CellRangeAddressList(1, 65535, entry.getKey(), entry.getKey());
            // 设置下拉框数据
            DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            // 处理Excel兼容性问题
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(dataValidation);
        }
    }
}