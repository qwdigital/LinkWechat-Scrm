package com.linkwechat.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * excel导出模板表头设置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/14 10:35
 */
public class WeLeadsExportHeadsWriteHandler implements SheetWriteHandler {

    private final int lastCol;

    public WeLeadsExportHeadsWriteHandler(int lastCol) {
        this.lastCol = lastCol;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        // 起始行, 终止行, 起始列, 终止列
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, lastCol);
        sheet.addMergedRegion(cra);
        // 设置0行0列，这里可以设置一些自定义的样式，颜色，文本，背景等等
        Row row = sheet.createRow(0);
        row.setHeight((short) 600);
        Cell cell = row.createCell(0);


        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("*号信息必填，如为空则会导致响应数据导入失败");
        //todo 日期格式提示
        cell.setCellValue(strBuilder.toString());

        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }
}
