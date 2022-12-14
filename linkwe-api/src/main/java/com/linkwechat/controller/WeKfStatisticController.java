package com.linkwechat.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.domain.kf.query.WeKfCustomerStatisticQuery;
import com.linkwechat.domain.kf.query.WeKfQualityStatQuery;
import com.linkwechat.domain.kf.vo.*;
import com.linkwechat.handler.WeQualityWriteHandler;
import com.linkwechat.service.IWeKfStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author danmo
 * @description 客服统计管理
 * @date 2022/2/8 23:54
 **/
@Slf4j
@Api(tags = "客服统计管理")
@RestController
@RequestMapping("/kf/statistic/")
public class WeKfStatisticController extends BaseController {

    @Autowired
    private IWeKfStatisticService weKfStatisticService;

    @ApiOperation(value = "场景客户数据分析", httpMethod = "GET")
    @GetMapping("/scene/getAnalysis")
    public AjaxResult<WeKfSceneAnalysisVo> getSceneCustomerAnalysis() {
        return AjaxResult.success(weKfStatisticService.getSceneCustomerAnalysis());
    }

    @ApiOperation(value = "场景客户数据-实时数据", httpMethod = "GET")
    @GetMapping("/scene/getRealCnt")
    public AjaxResult<List<WeKfSceneRealCntVo>> getSceneCustomerRealCnt(WeKfCustomerStatisticQuery query) {
        return AjaxResult.success(weKfStatisticService.getSceneCustomerRealCnt(query));
    }

    @ApiOperation(value = "场景客户数据-场景值排行", httpMethod = "GET")
    @GetMapping("/scene/getRankCnt")
    public AjaxResult<WeKfSceneRankCntListVo> getSceneCustomerRank(WeKfCustomerStatisticQuery query) {
        return AjaxResult.success(weKfStatisticService.getSceneCustomerRank(query));
    }

    @ApiOperation(value = "场景客户数据-实时数据（分页）", httpMethod = "GET")
    @GetMapping("/scene/getRealPageCnt")
    public TableDataInfo<WeKfSceneRealCntVo> getSceneCustomerRealPageCnt(WeKfCustomerStatisticQuery query) {
        startPage();
        return getDataTable(weKfStatisticService.getSceneCustomerRealPageCnt(query));
    }

    @ApiOperation(value = "场景客户数据-实时数据-导出", httpMethod = "GET")
    @GetMapping("/scene/real/export")
    public void sceneCustomerRealExport(WeKfCustomerStatisticQuery query) throws IOException {
        List<WeKfSceneRealCntVo> sceneCustomerRealCnt = weKfStatisticService.getSceneCustomerRealCnt(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("场景数据报表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeKfSceneRealCntVo.class).sheet("场景数据").doWrite(sceneCustomerRealCnt);
    }

    @ApiOperation(value = "咨询客户数据分析", httpMethod = "GET")
    @GetMapping("/consult/getAnalysis")
    public AjaxResult<WeKfConsultAnalysisVo> getConsultCustomerAnalysis() {
        return AjaxResult.success(weKfStatisticService.getConsultCustomerAnalysis());
    }

    @ApiOperation(value = "咨询客户数据-实时数据", httpMethod = "GET")
    @GetMapping("/consult/getRealCnt")
    public AjaxResult<List<WeKfConsultRealCntVo>> getConsultCustomerRealCnt(WeKfCustomerStatisticQuery query) {
        return AjaxResult.success(weKfStatisticService.getConsultCustomerRealCnt(query));
    }

    @ApiOperation(value = "咨询客户数据-接待人员排行", httpMethod = "GET")
    @GetMapping("/consult/getRankCnt")
    public AjaxResult<WeKfConsultRankCntListVo> getConsultUserRank(WeKfCustomerStatisticQuery query) {
        return AjaxResult.success(weKfStatisticService.getConsultUserRank(query));
    }

    @ApiOperation(value = "咨询客户数据-实时数据（分页）", httpMethod = "GET")
    @GetMapping("/consult/getRealPageCnt")
    public TableDataInfo<WeKfConsultRealCntVo> getConsultCustomerRealPageCnt(WeKfCustomerStatisticQuery query) {
        startPage();
        return getDataTable(weKfStatisticService.getConsultCustomerRealPageCnt(query));
    }

    @ApiOperation(value = "咨询客户数据-实时数据-导出", httpMethod = "GET")
    @GetMapping("/consult/real/export")
    public void consultCustomerRealExport(WeKfCustomerStatisticQuery query) throws IOException {
        List<WeKfConsultRealCntVo> consultCustomerRealCnt = weKfStatisticService.getConsultCustomerRealPageCnt(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("咨询分析数据报表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeKfConsultRealCntVo.class).sheet("咨询分析数据").doWrite(consultCustomerRealCnt);
    }

    @ApiOperation(value = "质量分析数据", httpMethod = "GET")
    @GetMapping("/quality/getAnalysis")
    public AjaxResult<WeKfQualityAnalysisVo> getQualityAnalysis() {
        WeKfQualityAnalysisVo weKfQualityAnalysisVo =  weKfStatisticService.getQualityAnalysis();
        return AjaxResult.success(weKfQualityAnalysisVo);
    }

    @ApiOperation(value = "质量分析数据-折线图", httpMethod = "GET")
    @GetMapping("/quality/getBrokenLine")
    public AjaxResult<List<WeKfQualityBrokenLineVo>> getQualityBrokenLine(WeKfQualityStatQuery query) {
        return AjaxResult.success(weKfStatisticService.getQualityBrokenLine(query));
    }

    @ApiOperation(value = "质量分析数据-柱状图", httpMethod = "GET")
    @GetMapping("/quality/getHistogram")
    public AjaxResult<List<WeKfQualityHistogramVo>> getQualityHistogram(WeKfQualityStatQuery query) {
        return AjaxResult.success(weKfStatisticService.getQualityHistogram(query));
    }

    @ApiOperation(value = "质量分析数据-图表", httpMethod = "GET")
    @GetMapping("/quality/getChart")
    public TableDataInfo<WeKfQualityChatVo> getQualityChart(WeKfQualityStatQuery query) {
        super.startPage();
        PageInfo<WeKfQualityChatVo> list = weKfStatisticService.getQualityChart(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "质量分析数据-图表-导出", httpMethod = "GET")
    @GetMapping("/quality/getChart/export")
    public void getQualityChartExport(WeKfQualityStatQuery query) throws IOException {
        PageInfo<WeKfQualityChatVo> list = weKfStatisticService.getQualityChart(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("质量分析数据报表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ExcelWriterBuilder write = EasyExcel.write(response.getOutputStream(), WeKfQualityChatVo.class);
        write.relativeHeadRowIndex(3);
        write.registerWriteHandler(new WeQualityWriteHandler(query));
        write.sheet("质量分析数据").doWrite(list.getList());
    }

}
