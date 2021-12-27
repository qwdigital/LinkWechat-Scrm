package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.*;
import com.linkwechat.wecom.domain.dto.customer.AllocateGroupDto;
import com.linkwechat.wecom.domain.dto.customer.JobExtendsCustomer;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeCommonAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 企业微信通讯录成员
 * @author: HaoN
 * @create: 2020-08-27 16:42
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeCommonAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeUserClient {

    /**
     * 创建用户
     * @param weUserDto
     * @return
     */
    @Request(url="/user/create", type = "POST")
    WeResultDto createUser(@JSONBody WeUserDto weUserDto);


    /**
     * 根据用户账号,获取用户详情信息
     * @param userid
     * @return
     */
    @Request(url = "/user/get")
    WeUserDto getUserByUserId(@Query("userid") String userid);


    /**
     * 更新通讯录用户
     * @param weUserDto
     * @return
     */
    @Request(url="/user/update", type = "POST")
    WeResultDto updateUser(@JSONBody WeUserDto weUserDto);


    /**
     * 根据账号删除指定用户
     * @param userid
     * @return
     */
    @Request(url = "/user/delete")
    WeResultDto deleteUserByUserId(@Query("userid") String userid);


    /**
     *  获取部门成员
     * @param departmentId
     * @param fetchChild
     * @return
     */
    @Request(url="/user/simplelist")
    WeUserListDto  simpleList(@Query("department_id") Long departmentId,@Query("fetch_child") Integer fetchChild);

    /**
     *  获取部门成员
     * @param departmentId
     * @param fetchChild
     * @return
     */
    @Request(url="/user/list")
    WeUserListDto  list(@Query("department_id") Long departmentId,@Query("fetch_child") Integer fetchChild);


    /**
     * 离职继承分配客户
     * @return
     */
    @Request(url="/externalcontact/resigned/transfer_customer", type = "POST",interceptor = WeAccessTokenInterceptor.class)
    WeResultDto allocateCustomer(@JSONBody AllocateWeCustomerDto allocateWeCustomerDto);



    /**
     * 在职继承分配客户(在职继承)
     * @param allocateWeCustomerDto
     * @return
     */
    @Request(url = "/externalcontact/transfer_customer", type = "POST",interceptor = WeAccessTokenInterceptor.class)
    JobExtendsCustomer transferCustomer(@JSONBody AllocateWeCustomerDto allocateWeCustomerDto);


    /**
     * 查询客户接替状态
     * @param jobExtendsParam
     * @return
     */
    @Post(url = "externalcontact/transfer_result",interceptor = WeAccessTokenInterceptor.class)
    JobExtendsCustomer transferResult(@JSONBody JobExtendsCustomer.JobExtendsParam jobExtendsParam);




    /**
     * 分配成员群
     * @return
     */
    @Request(url="/externalcontact/groupchat/transfer", type = "POST",interceptor = WeAccessTokenInterceptor.class)
    AllocateGroupDto allocateGroup(@JSONBody AllocateWeGroupDto allocateWeGroupDto);


    /**
     * 获取离职员工列表
     * @return
     */
    @Request(url = "/externalcontact/get_unassigned_list",type = "POST")
    LeaveWeUserListsDto  leaveWeUsers();


    /**
     * 获取访问用户身份(内部应用)
     * @param code
     * @param agentId 应用的id,请求头中
     * @return
     */
    @Request(url = "/user/getuserinfo",interceptor = WeAppAccessTokenInterceptor.class)
    WeUserInfoDto  getUserInfo(@Query("code")String code,@Header("agentId")String agentId);


    /**
     * 获取离职成员待分配客户
     * @param checkParm
     * @return
     */
    @Post(url = "/externalcontact/get_unassigned_list",interceptor = WeAccessTokenInterceptor.class)
    AllocateWeCustomerDto getUnassignedList(@JSONBody AllocateWeCustomerDto.CheckParm checkParm);
}
