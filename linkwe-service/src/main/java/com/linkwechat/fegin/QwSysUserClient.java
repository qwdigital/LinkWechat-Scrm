package com.linkwechat.fegin;

import com.alibaba.fastjson.JSONObject;
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

    /**
     * 同步组织架构
     * @param msg
     * @return
     */
    @PostMapping("/system/user/syncUserHandler")
    AjaxResult syncUserHandler(@RequestBody JSONObject msg);

    /**
     * 回调删除
     * @param weUserIds 企微员工ID
     * @return
     */
    @DeleteMapping("/system/user/callback/{userIds}")
    AjaxResult delete(@PathVariable("userIds") List<String> weUserIds);

    /**
     * 回调新增
     * @param query
     * @return
     */
    @PostMapping("/system/user/callback/add")
    AjaxResult add(@RequestBody SysUserQuery query);

    /**
     * 回调更新
     * @param query
     * @return
     */
    @PutMapping("/system/user/callback/edit")
    AjaxResult edit(@RequestBody SysUserQuery query);



    @GetMapping("/system/user/info/{id}")
    AjaxResult getUserInfoById(@PathVariable("id") Long userId);

//    @PostMapping("/system/user/listByQuery")
//    AjaxResult<List<SysUser>> list(@RequestBody SysUser sysUser);

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
     * 更新员工开启会话存档状态
     * @param query
     * @return
     */
    @PutMapping("/system/user/update/chat/status")
    AjaxResult updateUserChatStatus(@RequestBody SysUserQuery query);

    /**
     * 通过企微员工ID获取员工信息
     * @param query
     * @return
     */
    @PostMapping("/system/user/getUserListByWeUserIds")
    AjaxResult<List<SysUserVo>> getUserListByWeUserIds(@RequestBody SysUserQuery query);


    /**
     * 根据员工条件查询员工
     * @param query
     * @return
     */
    @PostMapping("/system/user/findSysUser")
    AjaxResult<List<SysUser>> findSysUser(@RequestBody SysUserQuery query);


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


    @PostMapping("/system/user/listByQuery")
    AjaxResult<List<SysUser>> listByQuery(@RequestBody SysUser sysUser);




}
