package com.linkwechat.handler;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.*;

/**
 * EasyExcel文本素材模板导出拦截器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/12/16 12:27
 */
public class TextMaterialCellWriteHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {

        Cell cell = context.getCell();
        int columnIndex = cell.getColumnIndex();
        int rowIndex = cell.getRowIndex();

        if (rowIndex == 0 && (columnIndex == 0 || columnIndex == 1)) {
            //行高
            Row row = context.getRow();
            row.setHeight((short) 1600);

            //设置富文本字体颜色
            CreationHelper creationHelper = context.getWriteSheetHolder().getSheet().getWorkbook().getCreationHelper();
            RichTextString richTextString = creationHelper.createRichTextString(cell.getStringCellValue());
            // 前1个字红色
            Font font = context.getWriteSheetHolder().getSheet().getWorkbook().createFont();
            font.setColor(Font.COLOR_RED);
            richTextString.applyFont(9, 10, font);
            cell.setCellValue(richTextString);

            //设置样式
            WriteCellData<String> firstCellData = (WriteCellData<String>) context.getFirstCellData();
            WriteCellStyle writeCellStyleData = new WriteCellStyle();
            writeCellStyleData.setHorizontalAlignment(HorizontalAlignment.LEFT);
            writeCellStyleData.setWrapped(true);
            //设置单元格不可修改
            writeCellStyleData.setLocked(true);

            firstCellData.setWriteCellStyle(writeCellStyleData);

        } else {

            WriteCellData<String> firstCellData = (WriteCellData<String>) context.getFirstCellData();
            //样式设置
            WriteCellStyle writeCellStyleData = new WriteCellStyle();
            //居中对齐
            writeCellStyleData.setHorizontalAlignment(HorizontalAlignment.CENTER);

            //边框
            writeCellStyleData.setBorderTop(BorderStyle.THIN);
            writeCellStyleData.setBorderBottom(BorderStyle.THIN);
            writeCellStyleData.setBorderLeft(BorderStyle.THIN);
            writeCellStyleData.setBorderRight(BorderStyle.THIN);

            //背景色：黄色
            writeCellStyleData.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            writeCellStyleData.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

            //设置单元格不可修改
            writeCellStyleData.setLocked(true);

            firstCellData.setWriteCellStyle(writeCellStyleData);

            //设置富文本字体颜色
            CreationHelper creationHelper = context.getWriteSheetHolder().getSheet().getWorkbook().getCreationHelper();
            RichTextString richTextString = creationHelper.createRichTextString(cell.getStringCellValue());

            //字体加粗
            Font afterFont = context.getWriteSheetHolder().getSheet().getWorkbook().createFont();
            afterFont.setFontHeightInPoints((short) 12);
            afterFont.setBold(true);
            richTextString.applyFont(afterFont);
            cell.setCellValue(richTextString);

            // 前1个字红色
            Font font = context.getWriteSheetHolder().getSheet().getWorkbook().createFont();
            font.setColor(Font.COLOR_RED);
            richTextString.applyFont(0, 1, font);
            cell.setCellValue(richTextString);
        }
    }


}
