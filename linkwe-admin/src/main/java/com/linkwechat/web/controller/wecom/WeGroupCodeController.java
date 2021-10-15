package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.framework.web.domain.server.SysFile;
import com.linkwechat.framework.web.service.FileService;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.service.IWeGroupCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 客户群活码Controller
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@Api(tags = "群活码")
@RestController
@RequestMapping("/wecom/groupCode")
public class WeGroupCodeController extends BaseController {

    @Autowired
    IWeGroupCodeService groupCodeService;

    @Autowired
    FileService fileService;

    @Value("${H5.domainPrefix}")
    private String h5DomainPrefix;

    /**
     * 查询客户群活码列表
     */
    @ApiOperation(value = "查询客户群活码列表", httpMethod = "GET")
//     @PreAuthorize("@ss.hasPermi('drainageCode:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeGroupCode weGroupCode) {
        startPage();
        List<WeGroupCode> list = groupCodeService.selectWeGroupCodeList(weGroupCode);
        return getDataTable(list);
    }

    /**
     * 批量下载群活码
     */
    @ApiOperation(value = "批量下载群活码", httpMethod = "GET")
    //  @PreAuthorize("@ss.hasPermi('drainageCode:group:downloadBatch')")
    @Log(title = "群活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(String ids, HttpServletRequest request, HttpServletResponse response) {
        // 构建文件信息列表
        List<Map<String, String>> fileList = Arrays
                .stream(Optional.ofNullable(ids).orElse("").split(","))
                .filter(StringUtils::isNotEmpty)
                .map(id -> {
                    WeGroupCode code = groupCodeService.getById(id);
                    Map<String, String> fileMap = new HashMap<>();
                    fileMap.put("fileName", code.getActivityName() + ".png");
                    fileMap.put("url", code.getCodeUrl());
                    return fileMap;
                })
                .collect(Collectors.toList());
        try {
            FileUtils.batchDownloadFile(fileList, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载群活码
     */
    @ApiOperation(value = "群活码下载", httpMethod = "GET")
    //    @PreAuthorize("@ss.hasPermi('drainageCode:group:download')")
    @Log(title = "群活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletResponse response) {
        WeGroupCode weGroupCode = groupCodeService.getById(Long.valueOf(id));
        try {
            FileUtils.downloadFile(weGroupCode.getCodeUrl(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取客户群活码详细信息
     */
    @ApiOperation(value = "获取客户群活码详细信息", httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WeGroupCode weGroupCode = groupCodeService.getById(id);
        if (StringUtils.isNull(weGroupCode)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        List<WeGroupCodeActual> weGroupCodeActualList = groupCodeService.selectActualList(weGroupCode.getId());
        weGroupCode.setActualList(weGroupCodeActualList);
        return AjaxResult.success(weGroupCode);
    }

    /**
     * 新增客户群活码
     */
    @ApiOperation(value = "新增客户群活码", httpMethod = "POST")
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:add')")
    @Log(title = "客户群活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WeGroupCode weGroupCode) {
        // 活码名唯一性检查
        if (groupCodeService.isNameOccupied(weGroupCode)) {
            return AjaxResult.error("添加群活码失败，活码名称 " + weGroupCode.getActivityName() + " 已存在");
        }
        AjaxResult ajax = AjaxResult.success();
        weGroupCode.setCreateBy(SecurityUtils.getUsername());
        weGroupCode.setCreateTime(new Date());
        try {
            // 二维码内容，即该二维码扫码后跳转的页面URL
            String content = h5DomainPrefix + "/mobile/#/groupCode?id=" + weGroupCode.getId();
            SysFile sysFile = fileService.upload(QREncode.getQRCodeMultipartFile(content, weGroupCode.getAvatarUrl()));
            weGroupCode.setCodeUrl(sysFile.getImgUrlPrefix() + sysFile.getFileName());
        } catch (Exception e) {
            return AjaxResult.error("创建群活码失败，请稍后再试");
        }
        groupCodeService.insertWeGroupCode(weGroupCode);
        ajax.put("id", weGroupCode.getId());
        return ajax;
    }

    /**
     * 修改客户群活码
     */
    @ApiOperation(value = "修改客户群活码", httpMethod = "PUT")
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:edit')")
    @Log(title = "客户群活码", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody WeGroupCode weGroupCode) {
        // 活码名唯一性检查
        weGroupCode.setId(id);
        if (groupCodeService.isNameOccupied(weGroupCode)) {
            return AjaxResult.error("添加群活码失败，活码名称 " + weGroupCode.getActivityName() + " 已存在");
        }
        weGroupCode.setUpdateBy(SecurityUtils.getUsername());
        weGroupCode.setUpdateTime(new Date());
        try {
            // 二维码内容，即该二维码扫码后跳转的页面URL TODO
            String content = h5DomainPrefix + "/mobile/#/groupCode?id=" + weGroupCode.getId();
            SysFile sysFile = fileService.upload(QREncode.getQRCodeMultipartFile(content, weGroupCode.getAvatarUrl()));
            weGroupCode.setCodeUrl(sysFile.getImgUrlPrefix() + sysFile.getFileName());
        } catch (Exception e) {
            return AjaxResult.error("创建群活码失败，请稍后再试");
        }
        return toAjax(groupCodeService.updateWeGroupCode(weGroupCode));
    }

    /**
     * 删除客户群活码
     */
    @ApiOperation(value = "删除客户群活码", httpMethod = "DELETE")
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:remove')")
    @Log(title = "客户群活码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids) {
        return toAjax(groupCodeService.deleteWeGroupCodeByIds(ids));
    }

    /**
     * 从群活码获取第一个可用的实际码(实际埋点记录扫码次数)
     */
    @ApiOperation(value = "从群活码获取第一个可用的实际码", httpMethod = "GET")
    @GetMapping("/getActualCode/{groupCodeId}")
    public AjaxResult getActual(@PathVariable("groupCodeId") String id) {
        WeGroupCode groupCode = groupCodeService.getById(id);

        //统计扫码次数
        groupCodeService.countScanTimes(groupCode);

        List<WeGroupCodeActual> actualCodeList = groupCodeService.selectActualList(groupCode.getId());
        WeGroupCodeActual groupCodeActual = null;
        for (WeGroupCodeActual item : actualCodeList) {
            // 获取第一个可用的实际码
            if (item.getStatus().intValue() == WeConstans.WE_GROUP_CODE_ENABLE) {
                groupCodeActual = item;
                break;
            }
        }
        if (StringUtils.isNotNull(groupCodeActual)) {
            AjaxResult ajax = AjaxResult.success();

            HashMap<String, String> data = new HashMap<>();
            data.put("activityName", groupCode.getActivityName());
            data.put("tipMsg", groupCode.getTipMsg());
            data.put("guide", groupCode.getGuide());
            data.put("actualQRCode", groupCodeActual.getActualGroupQrCode());
            data.put("isOpenTip", groupCode.getShowTip().toString());
            data.put("serviceQrCode", groupCode.getCustomerServerQrCode());
            data.put("groupName", groupCodeActual.getChatGroupName());
            ajax.put("data", data);
            return ajax;
        } else {
            // 找不到可用的实际群活码也不要抛出错误，否则前端H5页面不好处理。
            return AjaxResult.success("没有可用的实际群活码!");
        }
    }


}
