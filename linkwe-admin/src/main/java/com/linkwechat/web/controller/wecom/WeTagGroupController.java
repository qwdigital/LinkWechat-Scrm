package com.linkwechat.web.controller.wecom;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.util.List;

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

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;

    /**
     * 查询标签组列表(分页)
     */
    //  @PreAuthorize("@ss.hasPermi('customerManage:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTagGroup weTagGroup)
    {
        startPage();

        TableDataInfo dataTable = getDataTable(weTagGroupService.selectWeTagGroupList(weTagGroup));
        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_CUSTOMER)
        );//最近同步时间

        return dataTable;
    }


    /**
     *  查询标签组列表(不分页)
     * @param weTagGroup
     * @return
     */
    @GetMapping("/allList")
    public AjaxResult allList(WeTagGroup weTagGroup)
    {
        return AjaxResult.success(
                weTagGroupService.selectWeTagGroupList(weTagGroup)
        );
    }


    /**
     * 新增标签组
     */
    //  @PreAuthorize("@ss.hasPermi('customerManage:tag:add')")
    @Log(title = "标签组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeTagGroup weTagGroup)
    {

        //校验标签组名称与标签名称是否相同
        if(StrUtil.isNotBlank(weTagGroup.getGourpName())){
            List<WeTag> weTags = weTagGroup.getWeTags();
            if(CollectionUtil.isNotEmpty(weTags)){
                if(weTags.stream().filter(m -> m.getName().equals(weTagGroup.getGourpName())).findAny().isPresent()){
                    return   AjaxResult.error("标签组名称与标签名不可重复");
                }

            }
        }
        weTagGroupService.insertWeTagGroup(weTagGroup);

        return AjaxResult.success();
    }

    /**
     * 修改标签组
     */
    //    @PreAuthorize("@ss.hasPermi('customerManage:tag:edit')")
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
    //   @PreAuthorize("@ss.hasPermi('customerManage:tag:remove')")
    @Log(title = "标签组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        weTagGroupService.deleteWeTagGroupByIds(ids);
        return AjaxResult.success();
    }


    /**
     * 同步标签
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('customerManage:tag:sync')")
    @GetMapping("/synchWeTags")
    public AjaxResult synchWeTags(){

        try {
            weTagGroupService.synchWeTags();
        }catch (CustomException e){
            return AjaxResult.error(e.getMessage());
        }


        return  AjaxResult.success(WeConstans.SYNCH_TIP);
    }
}
