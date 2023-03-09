package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.qr.query.WeLxQrAddQuery;
import com.linkwechat.domain.qr.query.WeLxQrCodeListQuery;
import com.linkwechat.domain.qr.query.WeQrCodeListQuery;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.service.IWeLxQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        WeLxQrAddVo lxQrCode = weLxQrCodeService.generateQrCode(id);
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
        List<WeQrCodeDetailVo> qrCodeList = weLxQrCodeService.getQrCodeList(query);
        return getDataTable(qrCodeList);
    }


    @ApiOperation(value = "获取活码统计", httpMethod = "GET")
    @GetMapping("/scan/count")
    public AjaxResult<WeQrCodeScanCountVo> getWeQrCodeScanCount(WeLxQrCodeListQuery query) {
        WeQrCodeScanCountVo weQrCodeScanCount = weLxQrCodeService.getLxQrCodeScanCount(query);
        return AjaxResult.success(weQrCodeScanCount);
    }

}
