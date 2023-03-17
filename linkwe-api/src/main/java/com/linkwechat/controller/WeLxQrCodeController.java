package com.linkwechat.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.domain.qr.query.WeLxQrAddQuery;
import com.linkwechat.domain.qr.query.WeLxQrCodeListQuery;
import com.linkwechat.domain.qr.query.WeLxQrCodeQuery;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.service.IWeLxQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author danmo
 * @description 拉新活码管理
 * @date 2023/03/06 18:22
 **/

@RestController
@RequestMapping(value = "lxqr")
@Api(tags = "拉新活码管理")
public class WeLxQrCodeController extends BaseController {

    @Autowired
    private IWeLxQrCodeService weLxQrCodeService;

    @ApiOperation(value = "新增拉新活码", httpMethod = "POST")
    @PostMapping("/add")
    public AjaxResult<WeLxQrAddVo> addQrCode(@RequestBody @Validated WeLxQrAddQuery query) {
        Long id = weLxQrCodeService.addQrCode(query);
        WeLxQrAddVo lxQrCode = weLxQrCodeService.generateQrCode(id,query.getQrType());
        return AjaxResult.success(lxQrCode);
    }

    @ApiOperation(value = "修改活码", httpMethod = "POST")
    @PutMapping("/update/{id}")
    public AjaxResult updateQrCode(@PathVariable("id") Long id, @RequestBody @Validated WeLxQrAddQuery query) {
        query.setQrId(id);
        weLxQrCodeService.updateQrCode(query);
        return AjaxResult.success();
    }


    @ApiOperation(value = "删除拉新活码", httpMethod = "DELETE")
    @DeleteMapping("/del/{ids}")
    public AjaxResult<WeQrCodeDetailVo> delQrCode(@PathVariable("ids") List<Long> ids) {
        weLxQrCodeService.delQrCode(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取拉新活码详情", httpMethod = "GET")
    @GetMapping("/get/{id}")
    public AjaxResult<WeLxQrCodeDetailVo> getQrDetail(@PathVariable("id") Long id) {
        WeLxQrCodeDetailVo qrDetail = weLxQrCodeService.getQrDetail(id);
        return AjaxResult.success(qrDetail);
    }

    @ApiOperation(value = "获取活码列表", httpMethod = "GET")
    @GetMapping("/list")
    public TableDataInfo<WeLxQrCodeListVo> getQrCodeList(WeLxQrCodeListQuery query) {
        startPage();
        PageInfo<WeLxQrCodeListVo> qrCodeList = weLxQrCodeService.getQrCodeList(query);
        return getDataTable(qrCodeList);
    }


    @ApiOperation(value = "获取活码折线统计", httpMethod = "GET")
    @GetMapping("/line/statistics")
    public AjaxResult<WeLxQrCodeLineVo> getWeQrCodeLineStatistics(WeLxQrCodeListQuery query) {
        WeLxQrCodeLineVo weQrCodeScanCount = weLxQrCodeService.getWeQrCodeLineStatistics(query);
        return AjaxResult.success(weQrCodeScanCount);
    }

    @ApiOperation(value = "获取活码列表统计", httpMethod = "GET")
    @GetMapping("/list/statistics")
    public TableDataInfo<WeLxQrCodeSheetVo> getWeQrCodeListStatistics(WeLxQrCodeListQuery query) {
        startPage();
        List<WeLxQrCodeSheetVo> list = weLxQrCodeService.getWeQrCodeListStatistics(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "导出活码列表统计", httpMethod = "GET")
    @GetMapping("/list/statistics/export")
    public void qrCodeListStatisticsExport(WeLxQrCodeListQuery query) throws IOException {
        List<WeLxQrCodeSheetVo> list = weLxQrCodeService.getWeQrCodeListStatistics(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("活码列表统计", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeLxQrCodeSheetVo.class).sheet("活码列表统计").doWrite(list);
    }


    @ApiOperation(value = "领取总数统计", httpMethod = "GET")
    @GetMapping("/receive/statistics")
    public AjaxResult<WeLxQrCodeReceiveVo> receiveStatistics(WeLxQrCodeQuery query) {
        WeLxQrCodeReceiveVo receiveStatistics = weLxQrCodeService.receiveStatistics(query);
        return AjaxResult.success(receiveStatistics);
    }

    @ApiOperation(value = "领取红包个数统计（折线图）", httpMethod = "GET")
    @GetMapping("/receive/line/num")
    public AjaxResult<WeLxQrCodeReceiveLineVo> receiveLineNum(WeLxQrCodeQuery query) {
        WeLxQrCodeReceiveLineVo lineNum = weLxQrCodeService.receiveLineNum(query);
        return AjaxResult.success(lineNum);
    }

    @ApiOperation(value = "领取红包金额统计（折线图）", httpMethod = "GET")
    @GetMapping("/receive/line/amount")
    public AjaxResult<WeLxQrCodeReceiveLineVo> receiveLineAmount(WeLxQrCodeQuery query) {
        WeLxQrCodeReceiveLineVo lineNum = weLxQrCodeService.receiveLineAmount(query);
        return AjaxResult.success(lineNum);
    }

    @ApiOperation(value = "领取红包列表统计（表格）", httpMethod = "GET")
    @GetMapping("/receive/list/statistics")
    public TableDataInfo<List<WeLxQrCodeReceiveListVo>> receiveListStatistics(WeLxQrCodeQuery query) {
        startPage();
        List<WeLxQrCodeReceiveListVo> list = weLxQrCodeService.receiveListStatistics(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "领取红包列表统计（表格）", httpMethod = "GET")
    @GetMapping("/receive/list/statistics/export")
    public void receiveListStatisticsExport(WeLxQrCodeQuery query) throws IOException {
        List<WeLxQrCodeReceiveListVo> list = weLxQrCodeService.receiveListStatistics(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("领取红包列表统计", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeLxQrCodeReceiveListVo.class).sheet("领取红包列表统计").doWrite(list);
    }


}
