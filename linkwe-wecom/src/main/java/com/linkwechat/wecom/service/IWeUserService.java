package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WeLeaveUserVo;

import java.util.List;

/**
 * 通讯录相关客户Service接口
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
public interface IWeUserService 
{
    /**
     * 查询通讯录相关客户
     * 
     * @param id 通讯录相关客户ID
     * @return 通讯录相关客户
     */
    public WeUser selectWeUserById(Long id);

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
    public int insertWeUser(WeUser weUser);

    /**
     * 修改通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    public int updateWeUser(WeUser weUser);

    /**
     * 批量删除通讯录相关客户
     * 
     * @param ids 需要删除的通讯录相关客户ID
     * @return 结果
     */
    public int deleteWeUserByIds(Long[] ids);

    /**
     * 删除通讯录相关客户信息
     * 
     * @param id 通讯录相关客户ID
     * @return 结果
     */
    public int deleteWeUserById(Long id);


    /**
     *  启用或禁用用户
     * @param id
     * @param enable
     * @return
     */
    public int startOrStop(Long id,Boolean enable);


    /**
     * 离职员工
     * @param weLeaveUserVo
     * @return
     */
    List<WeLeaveUserVo> leaveUserList(WeLeaveUserVo weLeaveUserVo);

}
