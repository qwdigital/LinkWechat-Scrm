package com.linkwechat.wecom.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.domain.dto.WeCustomerDto;
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
    public void synchWeCustomer() {

        WeFollowUserDto weFollowUserDto = weFollowUserClient.getFollowUserList();

        if(WeConstans.WE_SUCCESS_CODE.equals(weFollowUserDto.getErrcode())
        && ArrayUtil.isNotEmpty(weFollowUserDto.getFollow_user())){

            Arrays.asList(weFollowUserDto.getFollow_user())
                    .stream().forEach(k->{

                //获取指定联系人对应的客户
                WeCustomerDto externalUserid
                        = weFollowUserClient.list(k);
                if(WeConstans.WE_SUCCESS_CODE.equals(externalUserid.getErrcode())
                && ArrayUtil.isNotEmpty(externalUserid.getExternal_userid())){

                    Arrays.asList(externalUserid.getExternal_userid()).forEach(v->{

                        //获取指定客户的详情
                        WeCustomerDto externalContact = weFollowUserClient.get(v);

                        if(WeConstans.WE_SUCCESS_CODE.equals(externalContact.getErrcode())
                        && null != externalContact.getExternal_contact()){

                            //分装成需要入库的数据格式




                        }
                    });

                }

            });


        }

    }




}
