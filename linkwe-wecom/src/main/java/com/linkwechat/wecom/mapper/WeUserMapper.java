package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerAddUser;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WeAllocateCustomersVo;
import com.linkwechat.wecom.domain.vo.WeAllocateGroupsVo;
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
     * 查询通讯录相关客户列表
     * 
     * @param weUser 通讯录相关客户
     * @return 通讯录相关客户集合
     */
    public List<WeUser> getList(WeUser weUser);

    /**
     * 批量新增或更新
     * @param weUserList 成员数据
     * @return
     */
    public void insertBatch(@Param("weUserList") List<WeUser> weUserList);
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
     * 获取历史分配记录的成员
     * @param weAllocateCustomersVo
     * @return
     */
    public List<WeAllocateCustomersVo> getAllocateCustomers(WeAllocateCustomersVo weAllocateCustomersVo);



    /**
     * 获取历史分配群
     * @param weAllocateGroupsVo
     * @return
     */
    public List<WeAllocateGroupsVo>  getAllocateGroups(WeAllocateGroupsVo weAllocateGroupsVo);


    /**
     * 根据客户id获取客户添加人
     * @param externalUserid
     * @return
     */
    List<WeCustomerAddUser> findWeUserByCutomerId(@Param("externalUserid") String externalUserid);
}
