package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeLeaveUserVo;
import com.linkwechat.wecom.service.IWeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api("通讯录人员接口")
public class WeUserController extends BaseController {
    @Autowired
    private IWeUserService weUserService;

    /**
     * 查询通讯录相关客户列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:list')")
    @GetMapping("/list")
    @ApiOperation("获取通讯录人员列表")
    public TableDataInfo list(WeUser weUser)
    {
        startPage();
        List<WeUser> list = weUserService.selectWeUserList(weUser);
        return getDataTable(list);
    }



    /**
     * 获取通讯录相关客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weUserService.selectWeUserById(id));
    }

    /**
     * 新增通讯录相关客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:add')")
    @Log(title = "通讯录相关客户", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增通讯录客户")
    public AjaxResult add(@RequestBody WeUser weUser)
    {
        return toAjax(weUserService.insertWeUser(weUser));
    }

    /**
     * 修改通讯录相关客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:edit')")
    @Log(title = "更新通讯录客户", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新通讯录客户")
    public AjaxResult edit(@RequestBody WeUser weUser)
    {
        return toAjax(weUserService.updateWeUser(weUser));
    }


    /**
     * 启用或者禁止
     * @param id
     * @param enable
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:startOrStop')")
    @Log(title = "启用禁用用户", businessType = BusinessType.UPDATE)
    @PutMapping("/startOrStop")
    @ApiOperation("启用禁用用户 true：启用 false：禁用")
    public AjaxResult startOrStop(Long id,Boolean enable){


        return toAjax(weUserService.startOrStop(id,enable));
    }


    /**
     * 离职等待分配
     * @param weLeaveUserVo
     * @return
     */
     @PreAuthorize("@ss.hasPermi('wecom:user:leaveUserAllocateList')")
     @GetMapping({"/leaveUserAllocateList"})
     public TableDataInfo leaveUserAllocateList(WeLeaveUserVo weLeaveUserVo) {
       startPage();
       weLeaveUserVo.setIsActivate(WeConstans.LEAVE_ALLOCATE_STATE);
       List<WeLeaveUserVo> list = this.weUserService.leaveUserList(weLeaveUserVo);
       return getDataTable(list);
     }


    /**
     * 离职已分配
     * @param weLeaveUserVo
     * @return
     */
     @PreAuthorize("@ss.hasPermi('wecom:user:leaveUserNoAllocateList')")
     @GetMapping({"/leaveUserNoAllocateList"})
     public TableDataInfo leaveUserNoAllocateList(WeLeaveUserVo weLeaveUserVo) {
        startPage();
        weLeaveUserVo.setIsActivate(WeConstans.LEAVE_NO_ALLOCATE_STATE);
        List<WeLeaveUserVo> list = this.weUserService.leaveUserList(weLeaveUserVo);
        return getDataTable(list);
     }


    /**
     * 离职分配
     * @param weLeaveUserInfoAllocateVo
     * @return
     */
     @PreAuthorize("@ss.hasPermi('wecom:user:allocateLeaveUserAboutData')")
     @PutMapping({"/allocateLeaveUserAboutData"})
     public AjaxResult allocateLeaveUserAboutData(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {



            return AjaxResult.success("离职分配成功");
    }




}
