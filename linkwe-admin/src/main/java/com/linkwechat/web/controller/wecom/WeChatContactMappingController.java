package com.linkwechat.web.controller.wecom;

import com.github.pagehelper.PageInfo;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeChatContactMapping;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.service.IWeChatContactMappingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 聊天关系映射Controller
 * 
 * @author ruoyi
 * @date 2020-12-27
 */
@RestController
@RequestMapping("/chat/mapping")
public class WeChatContactMappingController extends BaseController
{
    @Autowired
    private IWeChatContactMappingService weChatContactMappingService;

    /**
     * 查询聊天关系映射列表
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeChatContactMapping weChatContactMapping)
    {
        startPage();
        List<WeChatContactMapping> list = weChatContactMappingService.selectWeChatContactMappingList(weChatContactMapping);
        return getDataTable(list);
    }

    /**
     * 按客户查询关系映射列表
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:listByCustomer')")
    @GetMapping("/listByCustomer")
    public TableDataInfo listByCustomer()
    {
        startPage();
        PageInfo<WeCustomer> weCustomerPageInfo = weChatContactMappingService.listByCustomer();
        return getDataTable(weCustomerPageInfo);
    }

    /**
     * 导出聊天关系映射列表
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:export')")
    @Log(title = "聊天关系映射", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeChatContactMapping weChatContactMapping)
    {
        List<WeChatContactMapping> list = weChatContactMappingService.selectWeChatContactMappingList(weChatContactMapping);
        ExcelUtil<WeChatContactMapping> util = new ExcelUtil<WeChatContactMapping>(WeChatContactMapping.class);
        return util.exportExcel(list, "mapping");
    }

    /**
     * 获取聊天关系映射详细信息
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weChatContactMappingService.selectWeChatContactMappingById(id));
    }

    /**
     * 新增聊天关系映射
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:add')")
    @Log(title = "聊天关系映射", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeChatContactMapping weChatContactMapping)
    {
        return toAjax(weChatContactMappingService.insertWeChatContactMapping(weChatContactMapping));
    }

    /**
     * 修改聊天关系映射
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:edit')")
    @Log(title = "聊天关系映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeChatContactMapping weChatContactMapping)
    {
        return toAjax(weChatContactMappingService.updateWeChatContactMapping(weChatContactMapping));
    }

    /**
     * 删除聊天关系映射
     */
//    @PreAuthorize("@ss.hasPermi('chat:mapping:remove')")
    @Log(title = "聊天关系映射", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weChatContactMappingService.deleteWeChatContactMappingByIds(ids));
    }
}
