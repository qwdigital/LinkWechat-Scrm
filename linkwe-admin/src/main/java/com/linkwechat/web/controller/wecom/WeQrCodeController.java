package com.linkwechat.web.controller.wecom;

import com.github.pagehelper.PageInfo;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.query.qr.WeQrAddQuery;
import com.linkwechat.wecom.domain.query.qr.WeQrCodeListQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeScanCountVo;
import com.linkwechat.wecom.service.IWeQrCodeService;
import com.linkwechat.wecom.service.event.WeEventPublisherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author danmo
 * @description 活码管理
 * @date 2021/11/12 18:22
 **/

@RestController
@RequestMapping(value = "wecom/qr/")
@Api(tags = "活码管理")
public class WeQrCodeController extends BaseController {

    @Autowired
    private IWeQrCodeService weQrCodeService;

    @Autowired
    private WeEventPublisherService weEventPublisherService;

    @ApiOperation(value = "新增活码", httpMethod = "POST")
    @Log(title = "活码管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addQrCode(@RequestBody @Validated WeQrAddQuery weQrAddQuery) {
        weQrCodeService.addQrCode(weQrAddQuery);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改活码", httpMethod = "POST")
    @Log(title = "活码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateQrCode(@RequestBody @Validated WeQrAddQuery weQrAddQuery) {
        weQrCodeService.updateQrCode(weQrAddQuery);
        return AjaxResult.success();
    }


    @ApiOperation(value = "获取活码详情", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @GetMapping("/get/{id}")
    public AjaxResult<WeQrCodeDetailVo> getQrDetail(@PathVariable("id") Long Id) {
        WeQrCodeDetailVo qrDetail = weQrCodeService.getQrDetail(Id);
        return AjaxResult.success(qrDetail);
    }

    @ApiOperation(value = "获取活码列表", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<WeQrCodeDetailVo> getQrCodeList(WeQrCodeListQuery qrCodeListQuery) {
        startPage();
        PageInfo<WeQrCodeDetailVo> qrCodeList = weQrCodeService.getQrCodeList(qrCodeListQuery);
        return getDataTable(qrCodeList);
    }

    @ApiOperation(value = "删除活码", httpMethod = "DELETE")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @DeleteMapping("/del/{ids}")
    public AjaxResult<WeQrCodeDetailVo> delQrCode(@PathVariable("ids") List<Long> ids) {
        weQrCodeService.delQrCode(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取活码统计", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @GetMapping("/scan/count")
    public AjaxResult<WeQrCodeScanCountVo> getWeQrCodeScanCount(WeQrCodeListQuery qrCodeListQuery) {
        WeQrCodeScanCountVo weQrCodeScanCount = weQrCodeService.getWeQrCodeScanCount(qrCodeListQuery);
        return AjaxResult.success(weQrCodeScanCount);
    }

    @GetMapping("/test")
    public AjaxResult test() {
        weEventPublisherService.register("123","213","213","123");
        return AjaxResult.success();
    }
}
