package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业id相关配置Controller
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
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
    @PreAuthorize("@ss.hasPermi('wechat:corp:add')")
    @Log(title = "新增企业id相关配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WeCorpAccount weCorpAccount)
    {
        return toAjax(weCorpAccountService.insertWeCorpAccount(weCorpAccount));
    }

    /**
     * 修改企业id相关配置
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:edit')")
    @Log(title = "修改企业id相关配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody WeCorpAccount weCorpAccount)
    {
        return toAjax(weCorpAccountService.updateWeCorpAccount(weCorpAccount));
    }

    /**
     * 启用有效企业微信账号
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:startVailWeCorpAccount')")
    @Log(title = "启用有效企业微信账号", businessType = BusinessType.DELETE)
	@PutMapping("/startVailWeCorpAccount/{corpId}")
    public AjaxResult startWeCorpAccount(@PathVariable String corpId)
    {
        return toAjax(weCorpAccountService.startVailWeCorpAccount(corpId));
    }

    /**
     * 客户流失通知开关
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:startCustomerChurnNoticeSwitch')")
    @Log(title = "客户流失通知开关", businessType = BusinessType.UPDATE)
    @PutMapping("/startCustomerChurnNoticeSwitch/{status}")
    public AjaxResult startCustomerChurnNoticeSwitch(@PathVariable String status)
    {
        return toAjax(weCorpAccountService.startCustomerChurnNoticeSwitch(status));
    }

    /**
     * 客户流失通知开关查询
     */
    @PreAuthorize("@ss.hasPermi('wechat:corp:getCustomerChurnNoticeSwitch')")
    @Log(title = "客户流失通知开关查询", businessType = BusinessType.OTHER)
    @GetMapping("/getCustomerChurnNoticeSwitch")
    public AjaxResult getCustomerChurnNoticeSwitch()
    {
        return AjaxResult.success("操作成功",weCorpAccountService.getCustomerChurnNoticeSwitch());
    }

}
