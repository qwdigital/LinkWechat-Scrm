package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.*;

import java.util.List;

public interface IWeLeaveUserService extends IService<SysLeaveUser> {
    /**
     * 离职未分配员工
     * @param weLeaveUser
     * @return
     */
    List<WeLeaveUser> leaveNoAllocateUserList(WeLeaveUser weLeaveUser);


    /**
     * 离职已分配员工
     * @param weLeaveUser
     * @return
     */
    List<WeLeaveUser> leaveAllocateUserList(WeLeaveUser weLeaveUser);


    /**
     * 获取历史分配记录的成员
     * @param weAllocateCustomers
     * @return
     */
    List<WeAllocateCustomer> getAllocateCustomers(WeAllocateCustomer weAllocateCustomers);


    /**
     * 获取历史分配群
     * @param weAllocateGroups
     * @return
     */
    List<WeAllocateGroups>  getAllocateGroups(WeAllocateGroups weAllocateGroups);



    /**
     * 离职分配
     * @param weLeaveUserInfoAllocate
     */
    void allocateLeaveUserAboutData(WeLeaveUserInfoAllocate weLeaveUserInfoAllocate);


    /**
     * 构建等待分配的
     * @param weUserIds
     */
    void createWaitAllocateCustomerAndGroup(List<String> weUserIds);


    /**
     * 同步企业微信客户
     * @return
     */
    void synchLeaveSysUser();



    /**
     * 同步企业微信客户业务
     * @param msg
     */
    void synchLeaveSysUserHandler(String msg);









}
