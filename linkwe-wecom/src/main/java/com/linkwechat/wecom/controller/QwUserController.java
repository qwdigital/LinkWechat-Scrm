package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.domain.wecom.query.user.WeAddUserQuery;
import com.linkwechat.domain.wecom.query.user.WeLeaveUserQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeCorpQrVo;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.user.WeLeaveUserVo;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.wecom.service.IQwUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author danmo
 * @description 企微员工接口
 * @date 2022/3/13 21:01
 **/
@Slf4j
@RestController
@RequestMapping("user")
public class QwUserController {

    @Autowired
    private IQwUserService qwUserService;

    /**
     * 获取自建访问用户身份
     *
     * @param query code和corpid
     * @return
     */
    @PostMapping("/getLoginUser")
    public AjaxResult<WeLoginUserVo> getLoginUser(@RequestBody WeUserQuery query) {
        WeLoginUserVo weLoginUserVo = qwUserService.getLoginUser(query);
        return AjaxResult.success(weLoginUserVo);
    }

    /**
     * 创建成员
     *
     * @param query
     * @return
     */
    @PostMapping("/addUser")
    public AjaxResult<WeResultVo> addUser(@RequestBody WeAddUserQuery query) {
        WeResultVo result = qwUserService.addUser(query);
        return AjaxResult.success(result);
    }

    /**
     * 更新成员
     *
     * @param query
     * @return
     */
    @PostMapping("/updateUser")
    public AjaxResult<WeResultVo> updateUser(@RequestBody WeAddUserQuery query) {
        WeResultVo result = qwUserService.updateUser(query);
        return AjaxResult.success(result);
    }

    /**
     * 读取成员信息
     *
     * @param query
     * @return
     */
    @PostMapping("/getUserInfo")
    public AjaxResult<WeUserDetailVo> getUserInfo(@RequestBody WeUserQuery query) {
        WeUserDetailVo weUserDetail = qwUserService.getUserInfo(query);
        return AjaxResult.success(weUserDetail);
    }

    /**
     * 删除成员
     *
     * @param query
     * @return
     */
    @PostMapping("/delUser")
    public AjaxResult<WeResultVo> delUser(@RequestBody WeUserQuery query) {
        WeResultVo result = qwUserService.delUser(query);
        return AjaxResult.success(result);
    }

    /**
     * 批量删除成员
     *
     * @param query
     * @return
     */
    @PostMapping("/batch/delUser")
    public AjaxResult<WeResultVo> batchDelUser(@RequestBody WeUserQuery query) {
        WeResultVo result = qwUserService.batchDelUser(query);
        return AjaxResult.success(result);
    }

    /**
     * 获取部门成员
     *
     * @param query
     * @return
     */
    @PostMapping("/getSimpleList")
    public AjaxResult<WeUserListVo> getSimpleList(@RequestBody WeUserListQuery query) {
        WeUserListVo weUserList = qwUserService.getSimpleList(query);
        return AjaxResult.success(weUserList);
    }

    /**
     * 获取部门成员详情
     *
     * @param query
     * @return
     */
    @PostMapping("/getList")
    public AjaxResult<WeUserListVo> getList(@RequestBody WeUserListQuery query) {
        WeUserListVo weUserList = qwUserService.getList(query);
        return AjaxResult.success(weUserList);
    }


    /**
     * 获取访问用户敏感信息
     * @param query
     * @return
     */
    @PostMapping("/getUserSensitiveInfo")
    public AjaxResult<WeUserDetailVo> getUserSensitiveInfo(@RequestBody WeUserQuery query) {
        WeUserDetailVo detail3rd = qwUserService.getUserSensitiveInfo(query);
        return AjaxResult.success(detail3rd);
    }

    /**
     * 获取员工加入企业码
     * @param query
     * @return
     */
    @PostMapping("/getJoinQrcode")
    public AjaxResult<WeCorpQrVo> getJoinQrcode(@RequestBody WeCorpQrQuery query){

        return AjaxResult.success(
                qwUserService.getJoinQrcode(query)
        );
    }


    /**
     * 获取待分配的离职成员列表
     * @param query
     * @return
     */
    @PostMapping("/getUnassignedList")
    public AjaxResult<WeLeaveUserVo>  getUnassignedList(@RequestBody WeLeaveUserQuery query){


        return AjaxResult.success(
                qwUserService.getUnassignedList(query)
        );

    }
}
