package com.linkwechat.common.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoyijie
 * @since 2023/1/6 17:35
 */
@Slf4j
public class EasyExcelUtils {

    /**
     * 设置Excel头
     *
     * @param headList Excel头信息
     */
    public static List<List<String>> head(List<String> headList) {
        List<List<String>> list = new ArrayList<>();
        for (String value : headList) {
            List<String> head = new ArrayList<>();
            head.add(value);
            list.add(head);
        }
        return list;
    }

    /**
     * 设置表格信息
     *
     * @param dataList 查询出的数据
     * @param fileList 需要显示的字段
     */
    public static List<List<Object>> dataList(List dataList, List<String> fileList) {
        List<List<Object>> list = new ArrayList<>();
        for (Object o : dataList) {
            List<Object> data = new ArrayList<>();
            for (String fieldName : fileList) {
                /**通过反射根据需要显示的字段，获取对应的属性值*/
                data.add(getFieldValue(fieldName, o));
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 根据传入的字段获取对应的get方法，如name,对应的getName方法
     *
     * @param fieldName 字段名
     * @param o         对象
     */
    public static Object getFieldValue(String fieldName, Object o) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, o.getClass());
            Method readMethod = propertyDescriptor.getReadMethod();
            return readMethod.invoke(o);
        } catch (Exception e) {
            log.error("使用反射获取对象属性值失败", e);
            return null;
        }
    }

    /**
     * 默认表格风格
     */
    public static void defaultExcelStyle(ExcelWriterBuilder builder) {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        builder.registerWriteHandler(horizontalCellStyleStrategy)
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(25)) // 简单的列宽策略，列宽20
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25)); // 简单的行高策略：头行高，内容行高
    }

    /**
     * 简单写一个sheet数据
     *
     * @param head            表头
     * @param dataList        数据
     * @param outputStream    输出流
     * @param writeHandlerArr 需要注册的拦截器列表
     */
    public static void simpleWrite(List<String> head, List dataList, OutputStream outputStream, WriteHandler... writeHandlerArr) {
        simpleWrite(head, dataList, outputStream, true, writeHandlerArr);
    }

    /**
     * 简单写一个sheet数据
     *
     * @param head            表头
     * @param dataList        数据 可为List<List<String>>或List<DemoData> DemoData为一个java对象
     * @param outputStream    输出流
     * @param autoCloseStream 写完是否关闭流
     * @param writeHandlerArr 需要注册的拦截器列表
     */
    public static void simpleWrite(List<String> head, List dataList, OutputStream outputStream, boolean autoCloseStream, WriteHandler... writeHandlerArr) {
        simpleWrite(head, dataList, outputStream, 0, "Sheet1", autoCloseStream, writeHandlerArr);
    }

    /**
     * 简单写一个sheet数据
     *
     * @param head            表头
     * @param dataList        数据 可为List<List<String>>或List<DemoData> DemoData为一个java对象
     * @param outputStream    输出流
     * @param sheetNo         sheet序号
     * @param sheetName       sheet名字
     * @param autoCloseStream 是否自动关闭输出流
     * @param writeHandlerArr 需要注册的拦截器列表
     */
    public static void simpleWrite(List<String> head, List dataList, OutputStream outputStream, Integer sheetNo, String sheetName, boolean autoCloseStream, WriteHandler... writeHandlerArr) {
        ExcelWriterBuilder builder = EasyExcelFactory.write();
        defaultExcelStyle(builder);
        for (WriteHandler writeHandler : writeHandlerArr) {
            builder.registerWriteHandler(writeHandler);
        }
        ExcelWriter excelWriter = builder.excelType(ExcelTypeEnum.XLSX).file(outputStream).autoCloseStream(autoCloseStream).build();
        WriteSheet sheet = new WriteSheet();
        sheet.setSheetNo(sheetNo);
        sheet.setSheetName(sheetName);
        List<List<String>> headList = head(head);
        sheet.setHead(headList);
        excelWriter.write(dataList, sheet);
        excelWriter.finish();
    }

    public static class ExcelListener extends AnalysisEventListener<Map<Integer, String>> {

        //Excel数据缓存结构
        private final List<Map<Integer, String>> dataList;

        //Excel表头（列名）数据缓存结构
        private Map<Integer, String> headTitleMap = new HashMap<>();

        public ExcelListener() {
            dataList = new ArrayList<>();
        }

        /**
         * 解析表头外的所有行数据
         **/
        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            dataList.add(data);
        }

        /**
         * 解析完的后置操作
         **/
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("所有数据解析完成");
        }

        /**
         * 解析表头数据
         **/
        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            headTitleMap = headMap;
        }

        public List<Map<Integer, String>> getDataList() {
            return dataList;
        }

        public Map<Integer, String> getHeadTitleMap() {
            return headTitleMap;
        }
    }
}
