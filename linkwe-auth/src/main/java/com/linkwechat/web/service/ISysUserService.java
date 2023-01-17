package com.linkwechat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.web.domain.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser
     * @return
     */
    public List<UserVo> selectUserVoList(SysUser sysUser, PageDomain pageDomain);


    /**
     * 员工数统计
     * @param sysUser
     * @return
     */
    public int selectCountUserDeptList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    public String selectUserRoleGroup(String userName);

    /**
     * 根据用户ID查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    public String selectUserPostGroup(String userName);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public void updateUser(SysUserDTO user);

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserStatus(SysUser user);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserProfile(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    public int resetPwd(SysUser user);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String userName, String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);


    /**
     * 同步企业微信成员
     * @param deptId
     * @param corpId
     * @return
     */
    public List<SysUser> syncWeUser(Long deptId, String corpId);


    /**
     *  根据we_user_id和租户 查询系统用户信息
     * @param weUserId
     * @return
     */
    SysUser selectUserByWeUserId(String weUserId);


    /**
     * 员工离职同事设置员工相关分配数据
     *
     * @param weUserIds
     */
    void leaveUser(String[] weUserIds);

    /**
     * 同步员工和部门(发送同步消息到mq)
     */
    void syncUserAndDept();

    /**
     * 同步员工和部门(监听mq业务逻辑)
     *
     * @param msg
     */
    void syncUserAndDeptHandler(String msg);

    SysUser addUser(SysUserDTO sysUser);


    /**
     * 编辑用户角色
     * @param user
     */
    void editUserRole(SysUserDTO user);


    /**
     * 批量更新角色
     * @param roleId
     * @param users
     */
    void batchEditUserRole(Long roleId,List<SysUserDTO> users);

    /**
     * 获取员工敏感数据
     * @param userTicket
     */
    void getUserSensitiveInfo(String userTicket);

    void getUserSensitiveInfo(Long userId, String userTicket);


    List<SysUser> findAllSysUser(String weUserIds,String positions,String deptIds);

    /**
     * 根据we_user_id获取用户，如果没有则从企业微信端同步
     * @param weuserId
     * @return
     */
    SysUser findOrSynchSysUser(String weuserId);



    /**
     * 通过企微员工ID获取员工信息
     * @param query
     * @return
     */
    List<SysUserVo> getUserListByWeUserIds(SysUserQuery query);
}
