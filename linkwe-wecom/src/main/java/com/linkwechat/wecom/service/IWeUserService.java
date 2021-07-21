package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dtflys.forest.annotation.Query;
import com.linkwechat.wecom.domain.WeCustomerAddUser;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeUserInfoDto;
import com.linkwechat.wecom.domain.dto.msgaudit.WeMsgAuditDto;
import com.linkwechat.wecom.domain.vo.*;
import org.apache.ibatis.annotations.Param;

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
     * 查询通讯录相关客户
     *
     * @param userId 通讯录相关客户ID
     * @return 通讯录相关客户
     */
    public WeUser selectWeUserById(String userId);

    /**
     * 查询通讯录相关客户列表
     * 
     * @param weUser 通讯录相关客户
     * @return 通讯录相关客户集合
     */
    public List<WeUser> selectWeUserList(WeUser weUser);

    /**
     * 新增通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    public void insertWeUser(WeUser weUser);

    /**
     * 新增通讯录相关客户(不同步企微)
     * @param weUser 通讯录相关客户
     * @return
     */
    public int insertWeUserNoToWeCom(WeUser weUser);

    /**
     * 修改通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    public void updateWeUser(WeUser weUser);

    /**
     * 修改通讯录相关客户(不同步企微)
     * @param weUser 通讯录相关客户
     * @return
     */
    public int updateWeUserNoToWeCom(WeUser weUser);


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
     * @param ids
     */
    void deleteUser(String[] ids);

    /**
     * 删除成员
     * @param userId 成员id
     * @return
     */
    int deleteUserNoToWeCom(String userId);


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
     * 获取会话内容存档开启成员列表
     * @param msgAuditDto
     * @return
     */
    List<WeUser> getPermitUserList(WeMsgAuditDto msgAuditDto);


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
    List<WeCustomerAddUser> findWeUserByCutomerId(String externalUserid);
}
