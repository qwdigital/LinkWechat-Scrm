package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.*;
import com.linkwechat.wecom.service.IWeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 通讯录相关客户Controller
 *
 * @author ruoyi
 * @date 2020-08-31
 */
@RestController
@RequestMapping("/wecom/user")
@Api(tags = "通讯录人员接口")
@Slf4j
public class WeUserController extends BaseController {

    @Autowired
    private IWeUserService weUserService;

    /**
     * 查询通讯录相关客户列表
     */
    @Log(title = "查询通讯录相关客户列表", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    @ApiOperation(value = "获取通讯录人员列表", httpMethod = "GET")
    public TableDataInfo<List<WeUser>> list(WeUser weUser) {
        startPage();
        return getDataTable(weUserService.getList(weUser));
    }


    /**
     * 获取所有员工接口(不分页)
     * @param weUser
     * @return
     */
    @GetMapping("/findAllWeUser")
    public AjaxResult findAllWeUser(WeUser weUser){

        return AjaxResult.success(
                weUserService.getList(weUser)
        );
    }




    /**
     * 获取通讯录相关客户详细信息
     *
     * @return
     */
    @Log(title = "获取通讯录相关客户详细信息", businessType = BusinessType.SELECT)
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取通讯录相关客户详细信息", httpMethod = "GET")
    public AjaxResult<WeUser> getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weUserService.getById(id));
    }

    /**
     * 新增通讯录相关客户
     */
    @Log(title = "通讯录相关客户", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增通讯录客户")
    public AjaxResult add(@Validated @RequestBody WeUser weUser) {
        weUserService.insert(weUser);
        return AjaxResult.success();
    }

    /**
     * 修改通讯录相关客户
     */
    @Log(title = "更新通讯录客户", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新通讯录客户")
    public AjaxResult edit(@RequestBody WeUser weUser) {
        weUserService.update(weUser);
        return AjaxResult.success();
    }


    /**
     * 启用或者禁止
     *
     * @param weUser
     * @return
     */
    @Log(title = "启用禁用用户", businessType = BusinessType.UPDATE)
    @PutMapping("/startOrStop")
    @ApiOperation("是否启用(1表示启用成员，0表示禁用成员)")
    public AjaxResult startOrStop(@RequestBody WeUser weUser) {
        weUserService.startOrStop(weUser);
        return AjaxResult.success();
    }


    /**
     * 离职已分配
     *
     * @param weLeaveUserVo
     * @return
     */
    @ApiOperation("离职已分配")
    @Log(title = "离职已分配", businessType = BusinessType.SELECT)
    @GetMapping({"/leaveUserAllocateList"})
    public TableDataInfo<List<WeLeaveUserVo>> leaveUserAllocateList(WeLeaveUserVo weLeaveUserVo) {
        startPage();
        weLeaveUserVo.setIsActivate(WeConstans.corpUserEnum.ACTIVE_STATE_FIVE.getKey());
        weLeaveUserVo.setIsAllocate(WeConstans.LEAVE_ALLOCATE_STATE);
        List<WeLeaveUserVo> list = this.weUserService.leaveAllocateUserList(weLeaveUserVo);
        return getDataTable(list);
    }


    /**
     * 离职未分配
     *
     * @param weLeaveUserVo
     * @return
     */
    @ApiOperation("离职未分配")
    @Log(title = "离职未分配", businessType = BusinessType.SELECT)
    @GetMapping({"/leaveUserNoAllocateList"})
    public TableDataInfo<List<WeLeaveUserVo>> leaveUserNoAllocateList(WeLeaveUserVo weLeaveUserVo) {
        startPage();
        weLeaveUserVo.setIsActivate(WeConstans.corpUserEnum.ACTIVE_STATE_FIVE.getKey());
        weLeaveUserVo.setIsAllocate(WeConstans.LEAVE_NO_ALLOCATE_STATE);
        List<WeLeaveUserVo> list = weUserService.leaveNoAllocateUserList(weLeaveUserVo);
        return getDataTable(list);
    }


    /**
     * 离职分配
     *
     * @param weLeaveUserInfoAllocateVo
     * @return
     */
    @ApiOperation("离职分配")
    @Log(title = "离职分配", businessType = BusinessType.UPDATE)
    @PutMapping({"/allocateLeaveUserAboutData"})
    public AjaxResult allocateLeaveUserAboutData(@RequestBody WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        weUserService.allocateLeaveUserAboutData(weLeaveUserInfoAllocateVo);

        return AjaxResult.success("离职分配成功");
    }


    /**
     * 同步成员
     *
     * @return
     */
    @ApiOperation("同步成员")
    @Log(title = "同步成员", businessType = BusinessType.OTHER)
    @GetMapping({"/synchWeUser"})
    public AjaxResult synchWeUser() {
        try {
            weUserService.synchWeUser();
        }catch (CustomException e){
            return AjaxResult.error(e.getMessage());
        }

        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }

    /**
     * 删除用户
     *
     * @return
     */
    @ApiOperation("删除用户")
    @Log(title = "删除用户", businessType = BusinessType.DELETE)
    @DeleteMapping({"/{ids}"})
    public AjaxResult deleteUser(@PathVariable String[] ids) {
        weUserService.deleteUser(ids);
        return AjaxResult.success();
    }


    /**
     * 获取历史分配记录的成员
     *
     * @param weAllocateCustomersVo
     * @return
     */
    //   @PreAuthorize("@ss.hasPermi('wecom:user:getAllocateCustomers')")
    @ApiOperation("获取历史分配记录的成员")
    @Log(title = "获取历史分配记录的成员", businessType = BusinessType.SELECT)
    @GetMapping({"/getAllocateCustomers"})
    public TableDataInfo<List<WeAllocateCustomersVo>> getAllocateCustomers(WeAllocateCustomersVo weAllocateCustomersVo) {
        startPage();
        List<WeAllocateCustomersVo> list = weUserService.getAllocateCustomers(weAllocateCustomersVo);
        return getDataTable(list);
    }


    /**
     * 获取历史分配记录的群
     *
     * @param weAllocateGroupsVo
     * @return
     */
    //    @PreAuthorize("@ss.hasPermi('wecom:user:getAllocateGroups')")
    @ApiOperation("获取历史分配记录的群")
    @Log(title = "获取历史分配记录的群", businessType = BusinessType.SELECT)
    @GetMapping({"/getAllocateGroups"})
    public TableDataInfo<List<WeAllocateGroupsVo>> getAllocateGroups(WeAllocateGroupsVo weAllocateGroupsVo) {
        startPage();
        List<WeAllocateGroupsVo> list = weUserService.getAllocateGroups(weAllocateGroupsVo);
        return getDataTable(list);
    }


    /**
     * 内部应用获取用户userId
     *
     * @param code
     * @return
     */
    @Log(title = "内部应用获取用户userId", businessType = BusinessType.SELECT)
    @ApiOperation("内部应用获取用户userId")
    @GetMapping("/getUserInfo")
    public AjaxResult<WeUserInfoVo> getUserInfo(String code, String agentId) {
        WeUserInfoVo userInfo = weUserService.getUserInfo(code, agentId);
        return AjaxResult.success(userInfo);
    }
}
