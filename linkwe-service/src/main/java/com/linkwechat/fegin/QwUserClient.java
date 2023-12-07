package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.domain.wecom.query.user.WeLeaveUserQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeCorpQrVo;
import com.linkwechat.domain.wecom.vo.user.WeLeaveUserVo;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.fallback.QwUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author leejoker
 * @date 2022/3/29 22:57
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwUserFallbackFactory.class)
public interface QwUserClient {

    /**
     * 获取自建访问用户身份
     *
     * @param query
     * @return
     */
    @PostMapping("/user/getLoginUser")
    AjaxResult<WeLoginUserVo> getLoginUser(@RequestBody WeUserQuery query);

    /**
     * 获取部门成员
     *
     * @param query
     * @return
     */
    @PostMapping("/user/getSimpleList")
    AjaxResult<WeUserListVo> getSimpleList(@RequestBody WeUserListQuery query);

    /**
     * 读取成员信息
     * @param query
     * @return
     */
    @PostMapping("/user/getUserInfo")
    AjaxResult<WeUserDetailVo> getUserInfo(@RequestBody WeUserQuery query);


    /**
     * 获取访问用户敏感信息
     * @param query
     * @return
     */
    @PostMapping("/user/getUserSensitiveInfo")
    AjaxResult<WeUserDetailVo> getUserSensitiveInfo(@RequestBody WeUserQuery query);

    /**
     * 获取部门成员详情
     * @param query
     * @return
     */
    @PostMapping("/user/getList")
    AjaxResult<WeUserListVo> getList(@RequestBody WeUserListQuery query);

    /**
     * 获取员工加入企业码
     * @param query
     * @return
     */
    @PostMapping("/user/getJoinQrcode")
    AjaxResult<WeCorpQrVo> getJoinQrcode(@RequestBody WeCorpQrQuery query);


    /**
     * 获取待分配的离职成员列表
     * @param query
     * @return
     */
    @PostMapping("/user/getUnassignedList")
    AjaxResult<WeLeaveUserVo>  getUnassignedList(@RequestBody WeLeaveUserQuery query);


    @PostMapping("/usr/listByQuery")
    AjaxResult<SysUser> listByQuery(@RequestBody SysUser sysUser);
}
