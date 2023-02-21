package com.linkwechat.controller;


import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.CorpUserEnum;
import com.linkwechat.domain.WeAllocateCustomer;
import com.linkwechat.domain.WeAllocateGroups;
import com.linkwechat.domain.WeLeaveUser;
import com.linkwechat.domain.WeLeaveUserInfoAllocate;
import com.linkwechat.service.IWeLeaveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 离职继承相关员工
 *
 * @author ruoyi
 * @date 2020-08-31
 */
@RestController
@RequestMapping("/leaveUser")
public class WeLeaveUserController extends BaseController {

    @Autowired
    private IWeLeaveUserService iWeLeaveUserService;

    /**
     * 离职未分配列表
     *
     * @param weLeaveUser
     * @return
     */
    @GetMapping({"/leaveUserNoAllocateList"})
    public TableDataInfo<List<WeLeaveUser>> leaveUserNoAllocateList(WeLeaveUser weLeaveUser) {
        startPage();
        weLeaveUser.setIsAllocate(CorpUserEnum.NO_IS_ALLOCATE.getKey());
        List<WeLeaveUser> list = iWeLeaveUserService.leaveNoAllocateUserList(weLeaveUser);
        return getDataTable(list);
    }



    /**
     * 离职已分配列表
     *
     * @param weLeaveUserVo
     * @return
     */
    @GetMapping({"/leaveUserAllocateList"})
    public TableDataInfo<List<WeLeaveUser>> leaveUserAllocateList(WeLeaveUser weLeaveUserVo) {
        startPage();
        weLeaveUserVo.setIsAllocate(CorpUserEnum.YES_IS_ALLOCATE.getKey());
        List<WeLeaveUser> list = iWeLeaveUserService.leaveAllocateUserList(weLeaveUserVo);
        return getDataTable(list);
    }

    /**
     * 离职分配
     *
     * @param weLeaveUserInfoAllocate
     * @return
     */
    @PutMapping({"/allocateLeaveUserAboutData"})
    public AjaxResult allocateLeaveUserAboutData(@RequestBody WeLeaveUserInfoAllocate weLeaveUserInfoAllocate) {

        iWeLeaveUserService.allocateLeaveUserAboutData(weLeaveUserInfoAllocate);

        return AjaxResult.success("离职分配成功");
    }


    /**
     * 获取历史分配记录的成员
     *
     * @param weAllocateCustomers
     * @return
     */
    @GetMapping({"/getAllocateCustomers"})
    public TableDataInfo<List<WeAllocateCustomer>> getAllocateCustomers(WeAllocateCustomer weAllocateCustomers) {
        startPage();
        List<WeAllocateCustomer> list = iWeLeaveUserService.getAllocateCustomers(weAllocateCustomers);
        return getDataTable(list);
    }


    /**
     * 获取历史分配记录的群
     *
     * @param weAllocateGroups
     * @return
     */
    @GetMapping({"/getAllocateGroups"})
    public TableDataInfo<List<WeAllocateGroups>> getAllocateGroups(WeAllocateGroups weAllocateGroups) {
        startPage();
        List<WeAllocateGroups> list = iWeLeaveUserService.getAllocateGroups(weAllocateGroups);
        return getDataTable(list);
    }


    /**
     * 离职员工相关同步
     * @return
     */
    @GetMapping("/synchLeaveUser")
    public AjaxResult synchLeaveUser(){

        iWeLeaveUserService.synchLeaveSysUser();

        return AjaxResult.success();
    }



}
