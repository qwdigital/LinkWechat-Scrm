package com.linkwechat.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.kf.query.WeKfQualityStatQuery;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeKfInfoService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @date 2022年11月29日 14:45
 */
public class WeQualityWriteHandler implements SheetWriteHandler {

    private WeKfQualityStatQuery query;

    public WeQualityWriteHandler() {
    }

    public WeQualityWriteHandler(WeKfQualityStatQuery query) {
        this.query = query;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        IWeKfInfoService weKfInfoService = SpringUtils.getBean(IWeKfInfoService.class);
        QwSysUserClient qwSysUserClient = SpringUtils.getBean(QwSysUserClient.class);

        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        // 设置0行0列，这里可以设置一些自定义的样式，颜色，文本，背景等等
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("日期范围： " + query.getBeginTime() + "——" + query.getEndTime());

        // 设置1行0列
        Row row1 = sheet.createRow(1);
        Cell cell1 = row1.createCell(0);
        String openKfNames = "无";
        if (CollectionUtil.isNotEmpty(query.getOpenKfIds())) {
            List<WeKfInfo> weKfInfoList = weKfInfoService.list(new LambdaQueryWrapper<WeKfInfo>().in(WeKfInfo::getOpenKfId, query.getOpenKfIds()).eq(WeKfInfo::getDelFlag, 0));
            if(CollectionUtil.isNotEmpty(weKfInfoList)){
                openKfNames = weKfInfoList.stream().map(WeKfInfo::getName).collect(Collectors.joining("；"));
            }
        }
        cell1.setCellValue("客服：" + openKfNames);

        // 设置2行0列
        Row row2 = sheet.createRow(2);
        Cell cell2 = row2.createCell(0);
        String userNames = "无";
        if (CollectionUtil.isNotEmpty(query.getUserIds())) {
            SysUserQuery userQuery = new SysUserQuery();
            userQuery.setWeUserIds(query.getUserIds());
            List<SysUserVo> userList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
            if(CollectionUtil.isNotEmpty(userList)){
                userNames = userList.stream().map(SysUserVo::getUserName).distinct().collect(Collectors.joining("；"));
            }
        }
        cell2.setCellValue("接待员工：" + userNames);
    }
}
