package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeUser;
import org.apache.ibatis.annotations.Param;

/**
 * 企业微信客户Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public interface WeCustomerMapper  extends BaseMapper<WeCustomer>
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
     * 删除企业微信客户
     * 
     * @param id 企业微信客户ID
     * @return 结果
     */
    public int deleteWeCustomerById(Long id);

    /**
     * 批量删除企业微信客户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeCustomerByIds(Long[] ids);


    /**
     * 根据员工ID获取客户
     * @param userId
     * @return
     */
    public List<WeUser> getCustomersByUserId(@Param("userId") String userId);
}
