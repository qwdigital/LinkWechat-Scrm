package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerAddUser;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.msgaudit.WeMsgAuditDto;
import com.linkwechat.wecom.domain.vo.*;

import java.util.List;

/**
 * 通讯录相关客户Service接口
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
public interface IWeUserService extends IService<WeUser>
{



    /**
     * 根据id查询通讯录相关客户
     * @param id
     * @return
     */
    public WeUser getById(Long id);


    /**
     * 查询通讯录相关客户列表
     * 
     * @param weUser 通讯录相关客户
     * @return 通讯录相关客户集合
     */
    public List<WeUser> getList(WeUser weUser);

    /**
     * 新增通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    public void insert(WeUser weUser);

    /**
     * 新增通讯录相关客户(不同步企微)
     * @param weUser 通讯录相关客户
     * @return
     */
    public boolean insert2Data(WeUser weUser);

    /**
     * 修改通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    public void update(WeUser weUser);

    /**
     * 修改通讯录相关客户(不同步企微)
     * @param weUser 通讯录相关客户
     * @return
     */
    public boolean update2Data(WeUser weUser);


    /**
     *  启用或禁用用户
     * @return
     */
    public void startOrStop(WeUser weUser);


    /**
     * 离职未分配员工
     * @param weLeaveUserVo
     * @return
     */
    List<WeLeaveUserVo> leaveNoAllocateUserList(WeLeaveUserVo weLeaveUserVo);


    /**
     * 离职已分配员工
     * @param weLeaveUserVo
     * @return
     */
    List<WeLeaveUserVo> leaveAllocateUserList(WeLeaveUserVo weLeaveUserVo);


    /**
     * 离职分配
     * @param weLeaveUserInfoAllocateVo
     */
    void allocateLeaveUserAboutData(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo);


    /**
     * 同步成员
     */
    void synchWeUser();


    /**
     * 删除用户
     * @param userIds
     */
    void deleteUser(String[]  userIds);

    /**
     * 删除成员
     * @param userId 成员id
     * @return
     */
    boolean deleteUserNoToWeCom(String userId);


    /**
     * 获取历史分配记录的成员
     * @param weAllocateCustomersVo
     * @return
     */
     List<WeAllocateCustomersVo> getAllocateCustomers(WeAllocateCustomersVo weAllocateCustomersVo);


    /**
     * 获取历史分配群
     * @param weAllocateGroupsVo
     * @return
     */
     List<WeAllocateGroupsVo>  getAllocateGroups(WeAllocateGroupsVo weAllocateGroupsVo);




    /**
     * 获取访问用户身份(内部应用)
     * @param code
     * @return
     */
    WeUserInfoVo getUserInfo(String code,String agentId);


    /**
     * 根据客户id获取客户添加人
     * @param externalUserid
     * @return
     */
    List<WeCustomerAddUser> findWeUserByCustomerId(String externalUserid);



    void synchLeaveUserData();


}
