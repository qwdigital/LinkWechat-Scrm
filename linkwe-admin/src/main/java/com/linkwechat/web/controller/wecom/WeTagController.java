package com.linkwechat.web.controller.wecom;

import java.util.List;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
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
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.service.IWeTagService;

/**
 * 企业微信标签Controller
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@RestController
@RequestMapping("/wecom/tag")
public class WeTagController extends BaseController
{
    @Autowired
    private IWeTagService weTagService;

    /**
     * 查询企业微信标签列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTag weTag)
    {
        startPage();
        List<WeTag> list = weTagService.selectWeTagList(weTag);
        return getDataTable(list);
    }

    /**
     * 导出企业微信标签列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:tag:export')")
    @Log(title = "企业微信标签", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTag weTag)
    {
        List<WeTag> list = weTagService.selectWeTagList(weTag);
        ExcelUtil<WeTag> util = new ExcelUtil<WeTag>(WeTag.class);
        return util.exportExcel(list, "tag");
    }

    /**
     * 获取企业微信标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:tag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weTagService.selectWeTagById(id));
    }

    /**
     * 新增企业微信标签
     */
    @PreAuthorize("@ss.hasPermi('wecom:tag:add')")
    @Log(title = "企业微信标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeTag weTag)
    {
        return toAjax(weTagService.insertWeTag(weTag));
    }

    /**
     * 修改企业微信标签
     */
    @PreAuthorize("@ss.hasPermi('wecom:tag:edit')")
    @Log(title = "企业微信标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeTag weTag)
    {
        return toAjax(weTagService.updateWeTag(weTag));
    }

    /**
     * 删除企业微信标签
     */
    @PreAuthorize("@ss.hasPermi('wecom:tag:remove')")
    @Log(title = "企业微信标签", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weTagService.deleteWeTagByIds(ids));
    }
}
