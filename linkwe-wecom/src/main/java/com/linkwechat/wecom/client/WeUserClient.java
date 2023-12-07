package com.linkwechat.wecom.client;

import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.domain.wecom.query.user.*;
import com.linkwechat.domain.wecom.vo.user.*;
import com.linkwechat.domain.wecom.vo.WeCorpQrVo;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAddressBookAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;
import com.dtflys.forest.annotation.*;

/**
 * @author danmo
 * @Description 成员管理接口
 * @date 2021/12/7 15:28
 **/
@BaseRequest(baseURL = "${weComServerUrl}")
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeUserClient {

    /**
     * 创建成员
     *
     * @param query
     * @return WeResultVo
     */
    @Post(url = "/user/create", interceptor = WeAppAccessTokenInterceptor.class)
    WeResultVo addUser(@JSONBody WeAddUserQuery query);

    /**
     * 更新成员
     *
     * @param query
     * @return WeResultVo
     */
    @Post(url = "/user/update", interceptor = WeAppAccessTokenInterceptor.class)
    WeResultVo updateUser(@JSONBody WeAddUserQuery query);

    /**
     * 读取成员
     *
     * @param query
     * @return WeUserDetailVo
     */
    @Get(url = "/user/get?userid=${query.userid}", interceptor = WeAppAccessTokenInterceptor.class)
    WeUserDetailVo getUserInfo(@Var("query") WeUserQuery query);

    /**
     * 删除成员
     *
     * @param query
     * @return WeResultVo
     */
    @Get(url = "/user/delete?userid=${query.userid}", interceptor = WeAppAccessTokenInterceptor.class)
    WeResultVo delUser(@Var("query") WeUserQuery query);

    /**
     * 批量删除成员
     *
     * @param query
     * @return WeResultVo
     */
    @Post(url = "/user/batchdelete", interceptor = WeAppAccessTokenInterceptor.class)
    WeResultVo batchDelUser(@JSONBody WeUserQuery query);

    /**
     * 获取部门成员
     *
     * @param query
     * @return WeUserListVo
     */
    @Get(url = "/user/simplelist?department_id=${query.department_id}&fetch_child=${query.fetch_child}", interceptor = WeAppAccessTokenInterceptor.class)
    WeUserListVo getSimpleList(@Var("query") WeUserListQuery query);

    /**
     * 获取部门成员详情
     *
     * @param query
     * @return WeUserListVo
     */
    @Get(url = "/user/list?department_id=${query.department_id}&fetch_child=${query.fetch_child}", interceptor = WeAppAccessTokenInterceptor.class)
    WeUserListVo getUserList(@Var("query") WeUserListQuery query);

    /**
     * userid转openid
     *
     * @param query
     * @return WeUserConvertVo
     */
    @Post(url = "/user/convert_to_openid", interceptor = WeAccessTokenInterceptor.class)
    WeUserConvertVo convertToOpenId(@JSONBody WeUserConvertQuery query);

    /**
     * openid转userid
     *
     * @param query
     * @return WeUserConvertVo
     */
    @Post(url = "/user/convert_to_userid", interceptor = WeAccessTokenInterceptor.class)
    WeUserConvertVo convertToUserId(@JSONBody WeUserConvertQuery query);

    /**
     * 获取企业活跃成员数
     *
     * @param query
     * @return WeUserActiveVo
     */
    @Post(url = "/user/get_active_stat", interceptor = WeAccessTokenInterceptor.class)
    WeUserActiveVo getActiveStat(@JSONBody WeUserActiveQuery query);

    /**
     * 二次验证
     *
     * @param query
     * @return WeResultVo
     */
    @Get(url = "/user/authsucc?userid=${query.userid}", interceptor = WeAppAccessTokenInterceptor.class)
    WeResultVo authSucc(@Var("query") WeUserQuery query);

    /**
     * 邀请成员
     *
     * @param query
     * @return WeUserInviteVo
     */
    @Post(url = "/batch/invite", interceptor = WeAppAccessTokenInterceptor.class)
    WeUserInviteVo batchUserInvite(@JSONBody WeUserInviteQuery query);

    /**
     * 获取加入企业二维码
     *
     * @param query
     * @return WeCorpQrVo
     */
    @Get(url = "/corp/get_join_qrcode?size_type=${query.size_type}", interceptor = WeAddressBookAccessTokenInterceptor.class)
    WeCorpQrVo getJoinQrcode(@Var("query") WeCorpQrQuery query);

    /**
     * 获取访问用户身份
     *
     * @param query
     * @return WeLoginUserVo
     */
    @Get(url = "/auth/getuserinfo?code=${query.code}", interceptor = WeAccessTokenInterceptor.class)
    WeLoginUserVo getLoginUser(@Var("query") WeUserQuery query);

    /**
     * 获取访问用户敏感信息
     * @param query
     * @return
     */
    @Post(url = "/auth/getuserdetail", interceptor = WeAccessTokenInterceptor.class)
    WeUserDetailVo getUserSensitiveInfo(@JSONBody WeUserQuery query);


    /**
     * 获取待分配的离职成员列表
     * @param query
     * @return
     */
    @Post(url = "/externalcontact/get_unassigned_list", interceptor = WeAccessTokenInterceptor.class)
    WeLeaveUserVo getUnassignedList(@JSONBody WeLeaveUserQuery query);






}
