package com.linkwechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCustomerTrajectory;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.groupchat.query.WeGroupChatQuery;
import com.linkwechat.domain.groupchat.query.WeMakeGroupTagQuery;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author danmo
 * @description 客户群管理
 * @date 2021/11/12 18:22
 **/

@RestController
@RequestMapping("groupchat")
@Api(tags = "客户群管理")
public class WeGroupChatController extends BaseController {

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWeGroupTagRelService weGroupTagRelService;

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;


    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;




    /**
     * 客户群列表
     * @param query
     * @return
     */
    @GetMapping("/page/list")
    public TableDataInfo<LinkGroupChatListVo> getPageList(WeGroupChatQuery query) {
        startPage();
        TableDataInfo dataTable = getDataTable(weGroupService.getPageList(query));

        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_CUSTOMER_GROUP)
        );//最近同步时间

        return dataTable;
    }



    /**
     * 客户群详情
     * @param chatId
     * @return
     */
    @GetMapping("/get/{chatId}")
    public AjaxResult<LinkGroupChatListVo> getInfo(@PathVariable("chatId") String chatId) {
        return AjaxResult.success(weGroupService.getInfo(chatId));
    }


    /**
     * 客户群成员列表
     * @param weGroupMember
     * @return
     */
    @GetMapping({"/member/page/list"})
    public TableDataInfo<List<WeGroupMember>> pageList(WeGroupMember weGroupMember) {
        startPage();
        List<WeGroupMember> list = this.weGroupMemberService.getPageList(weGroupMember);
        return getDataTable(list);
    }

    /**
     * 同步客户群
     * @return
     */
    @GetMapping("/synch")
    public AjaxResult synchWeGroup() {
        weGroupService.synchWeGroup();
        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }

    /**
     * 编辑群标签
     * @return
     */
    @PostMapping("/makeGroupTag")
    public AjaxResult makeGroupTag(@RequestBody WeMakeGroupTagQuery query){
        weGroupTagRelService.makeGroupTag(query);
        return AjaxResult.success();
    }

    /**
     * 获取指定群相关轨迹
     * @param chatId
     * @return
     */
    @GetMapping("/findGroupTrajectory/{chatId}")
    public TableDataInfo<WeCustomerTrajectory> findGroupTrajectory(@PathVariable String chatId){
        startPage();
        return getDataTable(
                iWeCustomerTrajectoryService.list(new LambdaQueryWrapper<WeCustomerTrajectory>()
                        .eq(WeCustomerTrajectory::getExternalUseridOrChatid,chatId))
        );
    }
}
