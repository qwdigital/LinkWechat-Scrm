package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.service.IWeGroupCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 客户群活码Controller
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@RestController
@RequestMapping("/wecom/groupCode")
public class WeGroupCodeController extends BaseController {
    @Autowired
    private IWeGroupCodeService groupCodeService;
    /**
     * 查询客户群活码列表
     */
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
    //  @PreAuthorize("@ss.hasPermi('drainageCode:group:downloadBatch')")
    @Log(title = "群活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(String ids, HttpServletRequest request, HttpServletResponse response) {
        List<String> idList = Arrays.stream(Optional.ofNullable(ids).orElse("").split(","))
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        try {
            List<WeGroupCode> weGroupCodeList = groupCodeService.selectWeGroupCodeListByIds(idList);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            if (CollectionUtil.isNotEmpty(weGroupCodeList)) {
                for (WeGroupCode weGroupCode : weGroupCodeList) {
                    String codeUrl = weGroupCode.getCodeUrl();
                    if (StringUtils.isEmpty(codeUrl)) {
                        continue;
                    }
                    URL url = new URL(codeUrl);
                    String fileName = weGroupCode.getActivityName() + ".png";
                    zos.putNextEntry(new ZipEntry(fileName));
                    InputStream fis = url.openConnection().getInputStream();
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }
            }
            //关闭zip输出流
            zos.flush();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    @PreAuthorize("@ss.hasPermi('drainageCode:group:download')")
    @Log(title = "群活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        WeGroupCode weGroupCode = groupCodeService.selectWeGroupCodeById(Long.valueOf(id));
        try {
            FileUtils.downloadFile(weGroupCode.getCodeUrl(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取客户群活码详细信息
     */
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WeGroupCode weGroupCode = groupCodeService.selectWeGroupCodeById(id);
        if (StringUtils.isNull(weGroupCode)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        List<WeGroupCodeActual> weGroupCodeActualList = groupCodeService.selectActualListByGroupCodeId(weGroupCode.getId());
        weGroupCode.setActualList(weGroupCodeActualList);
        return AjaxResult.success(weGroupCode);
    }

    /**
     * 新增客户群活码
     */
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:add')")
    @Log(title = "客户群活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WeGroupCode weGroupCode) {
        // 唯一性检查
        if (!groupCodeService.checkActivityNameUnique(weGroupCode)) {
            return AjaxResult.error("添加群活码失败，活码名称 " + weGroupCode.getActivityName() + " 已存在");
        }
        AjaxResult ajax = AjaxResult.success();
        weGroupCode.setCreateBy(SecurityUtils.getUsername());
        groupCodeService.insertWeGroupCode(weGroupCode);
        ajax.put("id", weGroupCode.getId());
        return ajax;
    }

    /**
     * 修改客户群活码
     */
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:edit')")
    @Log(title = "客户群活码", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody WeGroupCode weGroupCode) {
        WeGroupCode originalCode = groupCodeService.selectWeGroupCodeById(id);
        if (StringUtils.isNull(originalCode)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        // 唯一性检查
        if (!originalCode.getActivityName().equals(weGroupCode.getActivityName()) &&
                !groupCodeService.checkActivityNameUnique(weGroupCode)) {
            return AjaxResult.error("修改群活码失败，活码名称 " + weGroupCode.getActivityName() + " 已存在");
        }
        weGroupCode.setId(id);
        weGroupCode.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(groupCodeService.updateWeGroupCode(weGroupCode));
    }

    /**
     * 删除客户群活码
     */
    //   @PreAuthorize("@ss.hasPermi('drainageCode:group:remove')")
    @Log(title = "客户群活码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids) {
        return toAjax(groupCodeService.deleteWeGroupCodeByIds(ids));
    }

    /**
     * 从群活码获取第一个可用的实际码
     */
    @GetMapping("/getActualCode/{groupCodeId}")
    public AjaxResult getActual(@PathVariable("groupCodeId") String groupCodeUuid) {
        WeGroupCode groupCode = groupCodeService.getWeGroupByUuid(groupCodeUuid);
        List<WeGroupCodeActual> actualCodeList = groupCodeService.selectActualListByGroupCodeId(groupCode.getId());
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
            data.put("isOpenTip", groupCode.getJoinGroupIsTip().toString());
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
