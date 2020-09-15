package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.domain.dto.WeFollowUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.service.IWeCustomerService;

/**
 * 企业微信客户Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
@Service
public class WeCustomerServiceImpl implements IWeCustomerService 
{
    @Autowired
    private WeCustomerMapper weCustomerMapper;


    @Autowired
    private WeCustomerClient weFollowUserClient;

    /**
     * 查询企业微信客户
     * 
     * @param id 企业微信客户ID
     * @return 企业微信客户
     */
    @Override
    public WeCustomer selectWeCustomerById(Long id)
    {
        return weCustomerMapper.selectWeCustomerById(id);
    }

    /**
     * 查询企业微信客户列表
     * 
     * @param weCustomer 企业微信客户
     * @return 企业微信客户
     */
    @Override
    public List<WeCustomer> selectWeCustomerList(WeCustomer weCustomer)
    {
        return weCustomerMapper.selectWeCustomerList(weCustomer);
    }

    /**
     * 新增企业微信客户
     * 
     * @param weCustomer 企业微信客户
     * @return 结果
     */
    @Override
    public int insertWeCustomer(WeCustomer weCustomer)
    {
        weCustomer.setCreateTime(DateUtils.getNowDate());
        return weCustomerMapper.insertWeCustomer(weCustomer);
    }

    /**
     * 修改企业微信客户
     * 
     * @param weCustomer 企业微信客户
     * @return 结果
     */
    @Override
    public int updateWeCustomer(WeCustomer weCustomer)
    {
        return weCustomerMapper.updateWeCustomer(weCustomer);
    }

    /**
     * 批量删除企业微信客户
     * 
     * @param ids 需要删除的企业微信客户ID
     * @return 结果
     */
    @Override
    public int deleteWeCustomerByIds(Long[] ids)
    {
        return weCustomerMapper.deleteWeCustomerByIds(ids);
    }

    /**
     * 删除企业微信客户信息
     * 
     * @param id 企业微信客户ID
     * @return 结果
     */
    @Override
    public int deleteWeCustomerById(Long id)
    {
        return weCustomerMapper.deleteWeCustomerById(id);
    }


    /**
     * 客户同步接口
     * @return
     */
    @Override
    public int synchWeCustomer() {

        WeFollowUserDto followUserList = weFollowUserClient.getFollowUserList();
        System.out.println(followUserList.getErrmsg());
        return 0;
    }
}
