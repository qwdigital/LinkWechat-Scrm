package com.linkwechat.web.controller.wecom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import com.linkwechat.wecom.service.IWeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 群组相关
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:53
 */

@RestController
@RequestMapping("/wecom/group/chat")
public class WeGroupController extends BaseController {
    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @PreAuthorize("@ss.hasPermi('customerManage:group:list')")
    @GetMapping({"/list"})
    public TableDataInfo list(WeGroup weGroup) {
        startPage();
        List<WeGroup> list = this.weGroupService.selectWeGroupList(weGroup);
        return getDataTable(list);
    }



    @PreAuthorize("@ss.hasPermi('customerManage:group:view')")
    @GetMapping({"/members"})
    public TableDataInfo list(WeGroupMember weGroupMember) {
        startPage();
        List<WeGroupMember> list = this.weGroupMemberService.selectWeGroupMemberList(weGroupMember);
        return getDataTable(list);
    }

    /**
     *  同步客户群
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage:group:sync')")
    @GetMapping({"/synchWeGroup"})
    public AjaxResult synchWeGroup(){
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
            SecurityContextHolder.setContext(context);
            weGroupService.synchWeGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  AjaxResult.success(WeConstans.SYNCH_TIP);
    }


    /**
     * 根据员工id获取员工相关群
     * @param userId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage:group:sync')")
    @GetMapping({"/getGroupsByUserId/{userId}"})
    public AjaxResult  getGroupsByUserId(@PathVariable String userId){


        return AjaxResult.success(weGroupService
                .list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getOwner,userId)));
    }
}
