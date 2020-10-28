package com.linkwechat.web.controller.wecom;


import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
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
    @PreAuthorize("@ss.hasPermi('customerManage:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTagGroup weTagGroup)
    {
        startPage();
        return getDataTable(
                weTagGroupService.selectWeTagGroupList(weTagGroup)
        );
    }



    /**
     * 新增标签组
     */
    @PreAuthorize("@ss.hasPermi('customerManage:tag:add')")
    @Log(title = "标签组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeTagGroup weTagGroup)
    {
        weTagGroupService.insertWeTagGroup(weTagGroup);

        return AjaxResult.success();
    }

    /**
     * 修改标签组
     */
    @PreAuthorize("@ss.hasPermi('customerManage:tag:edit')")
    @Log(title = "标签组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeTagGroup weTagGroup)
    {
        weTagGroupService.updateWeTagGroup(weTagGroup);
        return AjaxResult.success();
    }

    /**
     * 删除标签组
     */
    @PreAuthorize("@ss.hasPermi('customerManage:tag:remove')")
    @Log(title = "标签组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(weTagGroupService.deleteWeTagGroupByIds(ids));
    }


    /**
     * 同步标签
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage:tag:sync')")
    @GetMapping("/synchWeTags")
    public AjaxResult synchWeTags(){

        weTagGroupService.synchWeTags();

        return  AjaxResult.success(WeConstans.SYNCH_TIP);
    }
}
