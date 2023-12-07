package com.linkwechat.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.linkwechat.common.enums.leads.template.DatetimeTypeEnum;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.Set;

/**
 * 线索中心-导入线索模板-日期格式设置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/08/18 14:50
 */
public class WeLeasExportDateHandler implements SheetWriteHandler {

    /**
     * map的key对应导出列的顺序 从0开始
     * value 对应数据格式
     * <p>
     *
     * @see com.linkwechat.common.enums.leads.template.DatetimeTypeEnum value对应的格式
     * </p>
     */
    private final Map<Integer, Integer> map;

    public WeLeasExportDateHandler(Map<Integer, Integer> map) {
        this.map = map;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        // 获取到当前的sheet
        Sheet sheet = writeSheetHolder.getSheet();

        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();

        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            DatetimeTypeEnum datetimeTypeEnum = DatetimeTypeEnum.of(value);

            //设置样式
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(datetimeTypeEnum.getFormat()));
            //设置样式给指定列
            sheet.setDefaultColumnStyle(key, dateCellStyle);
        }
    }
}
