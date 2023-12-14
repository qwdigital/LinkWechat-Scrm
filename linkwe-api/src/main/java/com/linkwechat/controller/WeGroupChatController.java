package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeCustomerTrajectory;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.groupchat.query.WeGroupChatQuery;
import com.linkwechat.domain.groupchat.query.WeMakeGroupTagQuery;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.groupchat.vo.WeCustomerDeduplicationVo;
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


    @Autowired
    private IWeCustomerService iWeCustomerService;

    /**
     * 客户群列表
     * @param query
     * @return
     */
    @GetMapping("/page/list")
    public TableDataInfo<LinkGroupChatListVo> getPageList(WeGroupChatQuery query) {
        List<LinkGroupChatListVo> pageList = weGroupService.getPageList(query, TableSupport.buildPageRequest());
        TableDataInfo dataTable = getDataTable(pageList);

        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_CUSTOMER_GROUP)
        );//最近同步时间

        dataTable.setTotal(
                weGroupService.countWeGroupListIds(query)
        );

        return dataTable;
    }


    /**
     * 应用客户群列表
     * @param query
     * @return
     */
    @GetMapping("/page/listByApp")
    public TableDataInfo<LinkGroupChatListVo> getPageListByApp(WeGroupChatQuery query) {
        startPage();

        SysUser sysUser
                = iWeCustomerService.findCurrentSysUserInfo(SecurityUtils.getUserId());

        if(!query.isDataScope()){//个人数据
            query.setUserIds(sysUser.getWeUserId());
            return getDataTable(weGroupService.selectWeGroupListByApp(query));
        }

        //全部数据(根据设定角色的数据权限范围来)
        return getDataTable(weGroupService.getPageList(query));
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


    /**
     * 客群去重列表
     * @param query
     * @return
     */
    @GetMapping("/findDeduplications")
    public TableDataInfo<WeCustomerDeduplicationVo> findDeduplications(WeGroupChatQuery query){
        startPage();
        return getDataTable(
                weGroupMemberService.findWeCustomerDeduplication(query.getMemberName())
        );
    }


    /**
     * 客户加入或移除黑名单
     * @return
     */
    @PostMapping("/joinOrRemoveBlackList")
    public AjaxResult joinOrRemoveBlackList(@RequestBody WeCustomersQuery query){
        String customerIds = query.getExternalUserids();
        if(StringUtils.isNotEmpty(customerIds)){
            List<WeCustomer> weCustomers = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                    .in(WeCustomer::getExternalUserid, ListUtil.toList(customerIds.split(","))));
            if(CollectionUtil.isNotEmpty(weCustomers)){
                weCustomers.stream().forEach(k->{
                    k.setIsJoinBlacklist(query.getIsJoinBlacklist());
                });

                iWeCustomerService.updateBatchById(weCustomers);
            }

        }
        return AjaxResult.success();
    }


    /**
     * 群去重提醒
     * @param query
     * @return
     */
   @PostMapping("/remindDuplicateMembers")
   public AjaxResult remindDuplicateMembers(@RequestBody WeCustomersQuery query){

        weGroupMemberService.remindDuplicateMembers(query.getExternalUserids());

        return AjaxResult.success();
   }
}
