package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.domain.vo.WeMakeGroupTagVo;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import com.linkwechat.wecom.service.IWeGroupService;
import com.linkwechat.wecom.service.IWeGroupTagRelService;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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


    @Autowired
    private IWeGroupTagRelService iWeGroupTagRelService;

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;

    //  @PreAuthorize("@ss.hasPermi('customerManage:group:list')")
    @GetMapping({"/list"})
    public TableDataInfo list(WeGroup weGroup) {
        startPage();
        List<WeGroup> list = this.weGroupService.selectWeGroupList(weGroup);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_CUSTOMER_GROUP)
        );//最近同步时间
        return dataTable;
    }


    /**
     * 群详情
     * @param chatId
     * @return
     */
    @GetMapping("/chatDetail/{chatId}")
    public AjaxResult chatDetail(@PathVariable String chatId){
        List<WeGroup> weGroups = this.weGroupService.selectWeGroupList(WeGroup.builder()
                        .chatId(chatId)
                .build());
        if(CollectionUtil.isNotEmpty(weGroups)){
            return AjaxResult.success(weGroups.stream().findFirst().get());
        }
        return AjaxResult.success();
    }

    /**
     * 获取所有群接口(不分页)
     * @param weGroup
     * @return
     */
    @GetMapping({"/allList"})
    public AjaxResult allList(WeGroup weGroup) {

        return AjaxResult.success(
                this.weGroupService.selectWeGroupList(weGroup)
        );
    }


    //   @PreAuthorize("@ss.hasPermi('customerManage:group:view')")
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
    //   @PreAuthorize("@ss.hasPermi('customerManage:group:sync')")
    @GetMapping({"/synchWeGroup"})
    public AjaxResult synchWeGroup(){

        try {
            weGroupService.synchWeGroup();
        }catch (CustomException e){
            return AjaxResult.error(e.getMessage());
        }
        return  AjaxResult.success(WeConstans.SYNCH_TIP);
    }


    /**
     * 根据员工id获取员工相关群
     * @param userId
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('customerManage:group:sync')")
    @GetMapping({"/getGroupsByUserId/{userId}"})
    public AjaxResult  getGroupsByUserId(@PathVariable String userId){


        return AjaxResult.success(weGroupService
                .list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getOwner,userId)));
    }


    /**
     * 编辑群标签
     * @return
     */
    @PostMapping("/makeGroupTag")
    @ApiOperation("编辑群标签")
    public AjaxResult makeGroupTag(@RequestBody WeMakeGroupTagVo weMakeGroupTagVo){

        iWeGroupTagRelService.makeGroupTag(weMakeGroupTagVo);

        return AjaxResult.success();
    }

}
