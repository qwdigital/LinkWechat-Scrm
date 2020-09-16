package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResult list()
    {
        return AjaxResult.success(
                weTagService.selectWeTagList(new WeTag())
        );
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
