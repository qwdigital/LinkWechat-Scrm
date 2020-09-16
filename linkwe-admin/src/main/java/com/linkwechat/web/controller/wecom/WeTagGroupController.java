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
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.service.IWeTagGroupService;

/**
 * 标签组Controller
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@RestController
@RequestMapping("/wecom/group")
public class WeTagGroupController extends BaseController
{
    @Autowired
    private IWeTagGroupService weTagGroupService;

    /**
     * 查询标签组列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:group:list')")
    @GetMapping("/list")
    public AjaxResult list()
    {

        return AjaxResult.success(
                weTagGroupService.selectWeTagGroupList(new WeTagGroup())
        );
    }

    /**
     * 导出标签组列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:group:export')")
    @Log(title = "标签组", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTagGroup weTagGroup)
    {
        List<WeTagGroup> list = weTagGroupService.selectWeTagGroupList(weTagGroup);
        ExcelUtil<WeTagGroup> util = new ExcelUtil<WeTagGroup>(WeTagGroup.class);
        return util.exportExcel(list, "group");
    }

    /**
     * 获取标签组详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weTagGroupService.selectWeTagGroupById(id));
    }

    /**
     * 新增标签组
     */
    @PreAuthorize("@ss.hasPermi('wecom:group:add')")
    @Log(title = "标签组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeTagGroup weTagGroup)
    {
        return toAjax(weTagGroupService.insertWeTagGroup(weTagGroup));
    }

    /**
     * 修改标签组
     */
    @PreAuthorize("@ss.hasPermi('wecom:group:edit')")
    @Log(title = "标签组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeTagGroup weTagGroup)
    {
        return toAjax(weTagGroupService.updateWeTagGroup(weTagGroup));
    }

    /**
     * 删除标签组
     */
    @PreAuthorize("@ss.hasPermi('wecom:group:remove')")
    @Log(title = "标签组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weTagGroupService.deleteWeTagGroupByIds(ids));
    }
}
