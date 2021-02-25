package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
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
    private IWeGroupCodeService weGroupCodeService;

    /**
     * 查询客户群活码列表
     */
    @PreAuthorize("@ss.hasPermi('drainageCode:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeGroupCode weGroupCode) {
        startPage();
        List<WeGroupCode> list = weGroupCodeService.selectWeGroupCodeList(weGroupCode);
        return getDataTable(list);
    }

    /**
     * 批量下载群活码
     */
    @PreAuthorize("@ss.hasPermi('drainageCode:group:downloadBatch')")
    @Log(title = "群活码批量下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadBatch")
    public void downloadBatch(String ids, HttpServletRequest request, HttpServletResponse response) {
        List<String> idList = Arrays.stream(Optional.ofNullable(ids).orElse("").split(","))
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        try {
            List<WeGroupCode> weGroupCodeList = weGroupCodeService.selectWeGroupCodeListByIds(idList);
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

    @PreAuthorize("@ss.hasPermi('drainageCode:group:download')")
    @Log(title = "群活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        WeGroupCode weGroupCode = weGroupCodeService.selectWeGroupCodeById(Long.valueOf(id));
        try {
            FileUtils.downloadFile(weGroupCode.getCodeUrl(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 导出客户群活码列表
//     */
//    @PreAuthorize("@ss.hasPermi('wecom:code:export')")
//    @Log(title = "客户群活码", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(WeGroupCode weGroupCode)
//    {
//        List<WeGroupCode> list = weGroupCodeService.selectWeGroupCodeList(weGroupCode);
//        ExcelUtil<WeGroupCode> util = new ExcelUtil<WeGroupCode>(WeGroupCode.class);
//        return util.exportExcel(list, "code");
//    }

    /**
     * 获取客户群活码详细信息
     */
    @PreAuthorize("@ss.hasPermi('drainageCode:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        WeGroupCode weGroupCode = weGroupCodeService.selectWeGroupCodeById(id);
        if (StringUtils.isNull(weGroupCode)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        List<WeGroupCodeActual> weGroupCodeActualList = weGroupCodeService.selectActualListByGroupCodeId(weGroupCode.getId());
        weGroupCode.setActualList(weGroupCodeActualList);
        return AjaxResult.success(weGroupCode);
    }

    /**
     * 新增客户群活码
     */
    @PreAuthorize("@ss.hasPermi('drainageCode:group:add')")
    @Log(title = "客户群活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WeGroupCode weGroupCode) {
        // 唯一性检查
        if (!weGroupCodeService.checkActivityNameUnique(weGroupCode)) {
            return AjaxResult.error("添加群活码失败，活码名称 " + weGroupCode.getActivityName() + " 已存在");
        }
        AjaxResult ajax = AjaxResult.success();
        weGroupCode.setCreateBy(SecurityUtils.getUsername());
        weGroupCodeService.insertWeGroupCode(weGroupCode);
        ajax.put("id", weGroupCode.getId());
        return ajax;
    }

    /**
     * 修改客户群活码
     */
    @PreAuthorize("@ss.hasPermi('drainageCode:group:edit')")
    @Log(title = "客户群活码", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/{id}")
    public AjaxResult edit(@PathVariable("id") Long id, @RequestBody WeGroupCode weGroupCode) {
        WeGroupCode originalCode = weGroupCodeService.selectWeGroupCodeById(id);
        if (StringUtils.isNull(originalCode)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "数据不存在");
        }
        // 唯一性检查
        if (!originalCode.getActivityName().equals(weGroupCode.getActivityName()) &&
                !weGroupCodeService.checkActivityNameUnique(weGroupCode)) {
            return AjaxResult.error("修改群活码失败，活码名称 " + weGroupCode.getActivityName() + " 已存在");
        }
        weGroupCode.setId(id);
        weGroupCode.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(weGroupCodeService.updateWeGroupCode(weGroupCode));
    }

    /**
     * 删除客户群活码
     */
    @PreAuthorize("@ss.hasPermi('drainageCode:group:remove')")
    @Log(title = "客户群活码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids) {
        return toAjax(weGroupCodeService.deleteWeGroupCodeByIds(ids));
    }

//    /**
//     * 删除客户群活码
//     */
//    @PreAuthorize("@ss.hasPermi('wecom:code:remove')")
//    @Log(title = "客户群活码", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(weGroupCodeService.deleteWeGroupCodeByIds(ids));
//    }


}
