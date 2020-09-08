package com.linkwechat.wecom.mapper;

import java.util.List;
import com.linkwechat.wecom.domain.WeUser;

/**
 * 通讯录相关客户Mapper接口
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
public interface WeUserMapper 
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
     * 删除通讯录相关客户
     * 
     * @param id 通讯录相关客户ID
     * @return 结果
     */
    public int deleteWeUserById(Long id);

    /**
     * 批量删除通讯录相关客户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeUserByIds(Long[] ids);


    /**
     * 根据部门id获取员工
     * @param departmentId
     * @return
     */
    public List<WeUser> findWeUsersByDepartmentId(Long departmentId);
}
