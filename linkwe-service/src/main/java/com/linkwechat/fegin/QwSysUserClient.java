package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.user.vo.WeUserScreenConditVo;
import com.linkwechat.fallback.QwSysUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/29 22:58
 */
@FeignClient(value = "${wecom.serve.linkwe-auth}", fallback = QwSysUserFallbackFactory.class, contextId = "linkwe-auth-user")
public interface QwSysUserClient {
    @GetMapping("/system/user/listAll")
    AjaxResult<List<SysUser>> listAll();

    @GetMapping("/system/user/syncUserAndDeptHandler")
    AjaxResult syncUserAndDeptHandler(@RequestParam("msg") String msg);


    @DeleteMapping("/system/user/callBackRemove/{corpId}/{userIds}")
    AjaxResult callBackRemove(@PathVariable("corpId") String corpId, @PathVariable("userIds") String[] userIds);

    @PostMapping("/system/user")
    AjaxResult add(@RequestBody SysUserDTO sysUser);

    @PutMapping("/system/user")
    AjaxResult edit(@RequestBody SysUserDTO sysUser);

    @GetMapping("/system/user/info/{id}")
    AjaxResult getUserInfoById(@PathVariable("id") Long userId);

    @PostMapping("/system/user/listByQuery")
    AjaxResult<List<SysUser>> list(@RequestBody SysUser sysUser);

    @GetMapping("/system/user/getUserInfo/{weUserId}")
    AjaxResult<SysUser> getInfo(@PathVariable("weUserId") String weUserId);

    @GetMapping("/system/user/findAllSysUser")
    AjaxResult<List<SysUser>> findAllSysUser(@RequestParam("weUserIds") String weUserIds,
                                             @RequestParam("positions") String positions,@RequestParam("deptIds") String deptIds);

    /**
     * 根据weuserid获取员工，如果没有则从企业微信端同步
     * @param weuserId
     * @return
     */
    @GetMapping("/system/user/findOrSynchSysUser/{weuserId}")
    AjaxResult<SysUser> findOrSynchSysUser(@PathVariable("weuserId") String weuserId);



    /**
     * 更新员工客服接待状态
     * @param sysUser
     * @return
     */
    @PutMapping("/system/user/update/kf/status")
    AjaxResult updateUserKfStatus(@RequestBody SysUser sysUser);

    /**
     * 通过企微员工ID获取员工信息
     * @param query
     * @return
     */
    @PostMapping("/system/user/getUserListByWeUserIds")
    AjaxResult<List<SysUserVo>> getUserListByWeUserIds(@RequestBody SysUserQuery query);


    /**
     * 根据职位等条件筛选员工
     * @param weUserIds
     * @param deptIds
     * @param positions
     * @return
     */
    @GetMapping("/system/user/screenConditWeUser")
    AjaxResult<List<String>> screenConditWeUser(
            @RequestParam("weUserIds") String weUserIds,@RequestParam("deptIds") String deptIds,
            @RequestParam("positions") String positions
    );


    /**
     * 批量更新sysUser
     * @param sysUsers
     * @return
     */
    @PostMapping("/system/user/builderLeaveSysUser")
    AjaxResult builderLeaveSysUser(@RequestBody SysUserQuery sysUsers);




}
