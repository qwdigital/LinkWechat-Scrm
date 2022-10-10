package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.WeTagGroup;
import com.linkwechat.service.IWeSynchRecordService;
import com.linkwechat.service.IWeTagGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 标签
 */
@RestController
@RequestMapping("/tag")
public class WeTagController extends BaseController {

    @Autowired
    private IWeTagGroupService weTagGroupService;

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;

    /**
     * 查询标签组列表(分页)
     */
    @GetMapping("/list")
    public TableDataInfo list(WeTagGroup weTagGroup)
    {
        startPage();

        TableDataInfo dataTable = getDataTable(weTagGroupService.selectWeTagGroupList(weTagGroup));

        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_CUSTOMER_TAG)
        );//最近同步时间

        return dataTable;
    }



    /**
     *  查询标签组列表(不分页)
     * @param weTagGroup
     * @return
     */
    @GetMapping("/allList")
    public AjaxResult<List<WeTagGroup>> allList(WeTagGroup weTagGroup)
    {
        return AjaxResult.success(
                weTagGroupService.selectWeTagGroupList(weTagGroup)
        );
    }

    /**
     * 新增标签组
     */
    @PostMapping
    public AjaxResult add(@RequestBody WeTagGroup weTagGroup)
    {

        //校验标签组名称与标签名称是否相同
        if(StrUtil.isNotBlank(weTagGroup.getGroupName())){
            //检验当前标签组名称是否重复
            if(weTagGroupService.count(new LambdaQueryWrapper<WeTagGroup>()
                    .eq(WeTagGroup::getGroupName,weTagGroup.getGroupName())
                    .eq(WeTagGroup::getGroupTagType,weTagGroup.getGroupTagType()))>0){
                return AjaxResult.error(
                        "标签组名称不可重复"
                );
            }

            List<WeTag> weTags = weTagGroup.getWeTags();
            if(CollectionUtil.isNotEmpty(weTags)){
                if(weTags.stream().filter(m -> m.getName().equals(weTagGroup.getGroupName())).findAny().isPresent()){
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
    @PutMapping
    public AjaxResult edit(@RequestBody WeTagGroup weTagGroup)
    {
        //校验标签组名称与标签名称是否相同
        if(StrUtil.isNotBlank(weTagGroup.getGroupName())){
            if(!weTagGroupService.getById(weTagGroup.getId()).getGroupName()
                    .equals(weTagGroup.getGroupName())){
                //检验当前标签组名称是否重复
                if(weTagGroupService.count(new LambdaQueryWrapper<WeTagGroup>()
                        .eq(WeTagGroup::getGroupName,weTagGroup.getGroupName())
                        .eq(WeTagGroup::getGroupTagType,weTagGroup.getGroupTagType()))>0){
                    return AjaxResult.error(
                            "标签组名称不可重复"
                    );
                }
            }

            List<WeTag> weTags = weTagGroup.getWeTags();
            if(CollectionUtil.isNotEmpty(weTags)){
                if(weTags.stream().filter(m -> m.getName().equals(weTagGroup.getGroupName())).findAny().isPresent()){
                    return   AjaxResult.error("标签组名称与标签名不可重复");
                }

            }
        }

        weTagGroupService.updateWeTagGroup(weTagGroup);
        return AjaxResult.success();
    }


    /**
     * 删除标签组
     */
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
