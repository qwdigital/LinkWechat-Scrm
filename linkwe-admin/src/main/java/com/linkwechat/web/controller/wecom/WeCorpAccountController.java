package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业id相关配置Controller
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Api("企业id配置")
@RestController
@RequestMapping("/wecom/corp")
public class WeCorpAccountController extends BaseController
{
    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    /**
     * 查询企业id相关配置列表
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeCorpAccount weCorpAccount)
    {
        startPage();
        List<WeCorpAccount> list = weCorpAccountService.selectWeCorpAccountList(weCorpAccount);
        return getDataTable(list);
    }

//    /**
//     * 导出企业id相关配置列表
//     */
//    @PreAuthorize("@ss.hasPermi('wechat:corp:export')")
//    @Log(title = "企业id相关配置", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(WxCorpAccount wxCorpAccount)
//    {
//        List<WxCorpAccount> list = wxCorpAccountService.selectWxCorpAccountList(wxCorpAccount);
//        ExcelUtil<WxCorpAccount> util = new ExcelUtil<WxCorpAccount>(WxCorpAccount.class);
//        return util.exportExcel(list, "corp");
//    }

    /**
     * 获取企业id相关配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weCorpAccountService.selectWeCorpAccountById(id));
    }

    /**
     * 新增企业id相关配置
     */
//    @PreAuthorize("@ss.hasPermi('wechat:corp:add')")
//    @Log(title = "企业id相关配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增")
    public AjaxResult add(@RequestBody WeCorpAccount weCorpAccount)
    {
        return toAjax(weCorpAccountService.insertWeCorpAccount(weCorpAccount));
    }

    /**
     * 修改企业id相关配置
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:edit')")
    @Log(title = "企业id相关配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeCorpAccount weCorpAccount)
    {
        return toAjax(weCorpAccountService.updateWeCorpAccount(weCorpAccount));
    }

    /**
     * 删除企业id相关配置
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:remove')")
    @Log(title = "企业id相关配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weCorpAccountService.deleteWeCorpAccountByIds(ids));
    }

}
