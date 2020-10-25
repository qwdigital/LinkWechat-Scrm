package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WeLeaveUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通讯录相关客户Mapper接口
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
public interface WeUserMapper extends BaseMapper<WeUser>
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
     * 离职未分配员工
     * @param weLeaveUserVo
     * @return
     */
   public List<WeLeaveUserVo> leaveNoAllocateUserList(WeLeaveUserVo weLeaveUserVo);


    /**
     * 离职已分配员工
     * @param weLeaveUserVo
     * @return
     */
    public List<WeLeaveUserVo> leaveAllocateUserList(WeLeaveUserVo weLeaveUserVo);



    /**
     * 删除当前表的员工
     * @return
     */
    public int  deleteWeUser();


    /**
     * 批量插入员工
     * @param weUsers
     * @return
     */
    public int batchInsertWeUser(@Param("weUsers") List<WeUser> weUsers);
}
