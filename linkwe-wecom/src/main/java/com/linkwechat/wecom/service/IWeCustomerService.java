package com.linkwechat.wecom.service;

import java.util.List;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;

/**
 * 企业微信客户Service接口
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public interface IWeCustomerService 
{
    /**
     * 查询企业微信客户
     * 
     * @param id 企业微信客户ID
     * @return 企业微信客户
     */
    public WeCustomer selectWeCustomerById(Long id);

    /**
     * 查询企业微信客户列表
     * 
     * @param weCustomer 企业微信客户
     * @return 企业微信客户集合
     */
    public List<WeCustomer> selectWeCustomerList(WeCustomer weCustomer);

    /**
     * 新增企业微信客户
     * 
     * @param weCustomer 企业微信客户
     * @return 结果
     */
    public int insertWeCustomer(WeCustomer weCustomer);

    /**
     * 修改企业微信客户
     * 
     * @param weCustomer 企业微信客户
     * @return 结果
     */
    public int updateWeCustomer(WeCustomer weCustomer);

    /**
     * 批量删除企业微信客户
     * 
     * @param ids 需要删除的企业微信客户ID
     * @return 结果
     */
    public int deleteWeCustomerByIds(Long[] ids);

    /**
     * 删除企业微信客户信息
     * 
     * @param id 企业微信客户ID
     * @return 结果
     */
    public int deleteWeCustomerById(Long id);


    /**
     * 同步客户接口
     * @return
     */
    public void synchWeCustomer();


    /**
     * 分配离职员工客户
     * @param weLeaveUserInfoAllocateVo
     */
    public void allocateWeCustomer(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo);
}
