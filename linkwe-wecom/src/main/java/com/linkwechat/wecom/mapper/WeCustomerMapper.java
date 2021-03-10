package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerPortrait;
import com.linkwechat.wecom.domain.WeCustomerSocialConn;
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
     * @param externalUserId 企业微信客户ID
     * @return 企业微信客户
     */
    public WeCustomer selectWeCustomerById(String externalUserId);

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
     * @param externalUserId 企业微信客户ID
     * @return 结果
     */
    public int deleteWeCustomerById(String externalUserId);

    /**
     * 批量删除企业微信客户
     * 
     * @param externalUserIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeCustomerByIds(String[] externalUserIds);


    /**
     * 根据员工ID获取客户
     * @param externalUserid
     * @return
     */
    public List<WeUser> getCustomersByUserId(@Param("externalUserid") String externalUserid);

    /**
     * 通过标签查询客户列表
     * @param ids 标签id
     * @return
     */
    List<WeUser> getCustomerByTag(List<String> ids);


    /**
     * 根据外部联系人ID和企业员工ID获取当前客户信息
     * @param externalUserid
     * @param userid
     * @return
     */
    WeCustomerPortrait findCustomerByOperUseridAndCustomerId(@Param("externalUserid") String externalUserid,@Param("userid") String userid);


    /**
     * 统计客户社交关系
     * @param externalUserid 客户id
     * @param userid 员工id
     * @return
     */
    WeCustomerSocialConn countSocialConn(@Param("externalUserid")String externalUserid,@Param("userid")String userid);


}
