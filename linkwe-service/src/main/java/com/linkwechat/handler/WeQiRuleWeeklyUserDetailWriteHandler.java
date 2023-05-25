package com.linkwechat.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyDetailListQuery;
import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyListVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeQiRuleManageStatisticsService;
import com.linkwechat.service.IWeQiRuleService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @date 2023年05月22日 14:45
 */
public class WeQiRuleWeeklyUserDetailWriteHandler implements SheetWriteHandler {

    private WeQiRuleWeeklyDetailListQuery query;

    public WeQiRuleWeeklyUserDetailWriteHandler() {
    }

    public WeQiRuleWeeklyUserDetailWriteHandler(WeQiRuleWeeklyDetailListQuery query) {
        this.query = query;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        IWeQiRuleService ruleService = SpringUtils.getBean(IWeQiRuleService.class);
        QwSysUserClient qwSysUserClient = SpringUtils.getBean(QwSysUserClient.class);

        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);

        CellRangeAddress cra =new CellRangeAddress(0, 0, 0, 5); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra);
        // 设置0行0列，这里可以设置一些自定义的样式，颜色，文本，背景等等
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        StringBuilder strBuilder = new StringBuilder(DateUtil.today());

        WeQiRuleWeeklyListQuery listQuery = new WeQiRuleWeeklyListQuery();
        listQuery.setWeeklyId(query.getWeeklyId());
        List<WeQiRuleWeeklyListVo> weeklyList = ruleService.getWeeklyList(listQuery);
        if(CollectionUtil.isNotEmpty(weeklyList)){
            strBuilder.append(" ");
            strBuilder.append(weeklyList.get(0).getUserName());
        }
        strBuilder.append(" ");
        strBuilder.append("质检详情");
        
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
