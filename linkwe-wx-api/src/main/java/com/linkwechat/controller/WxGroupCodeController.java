package com.linkwechat.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.service.IWeCommunityNewGroupService;
import com.linkwechat.service.IWeGroupCodeService;
import com.linkwechat.service.IWePresTagGroupTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupCode")
public class WxGroupCodeController {


    @Autowired
    private IWeGroupCodeService groupCodeService;

    @Autowired
    private IWeCommunityNewGroupService iWeCommunityNewGroupService;


    @Autowired
    private IWePresTagGroupTaskService taskService;


    /**
     * 获取群活码详情
     * @param id
     * @return
     */
    @GetMapping("/getActualCode/{id}")
    public AjaxResult<WeGroupCode> getWeGroupCode(@PathVariable String id){
        return AjaxResult.success(
                groupCodeService.getById(id)
        );
    }


    /**
     * 获取新客自动拉群详细信息
     */
    @GetMapping(value = "/findWeCommunityNewGroupById/{id}")
    public AjaxResult<WeCommunityNewGroup> findWeCommunityNewGroupById(@PathVariable("id") String id) {
        WeCommunityNewGroup weCommunityNewGroup
                = iWeCommunityNewGroupService.findWeCommunityNewGroupById(id);

        return AjaxResult.success(weCommunityNewGroup);
    }


    /**
     * 老客标签建群详情信息
     * @param id
     * @return
     */
    @GetMapping("/findWePresTagGroupById/{id}")
    public AjaxResult<WePresTagGroupTask> findWePresTagGroupById(@PathVariable("id") Long id){
        WePresTagGroupTask taskVo = taskService.getTaskById(id);

        return AjaxResult.success(taskVo);
    }


}
