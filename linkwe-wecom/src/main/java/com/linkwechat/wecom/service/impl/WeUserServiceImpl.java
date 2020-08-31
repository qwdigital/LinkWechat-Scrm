package com.linkwechat.wecom.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.service.IWeUserService;

/**
 * 通讯录相关客户Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@Service
public class WeUserServiceImpl implements IWeUserService 
{
    @Autowired
    private WeUserMapper weUserMapper;

    /**
     * 查询通讯录相关客户
     * 
     * @param id 通讯录相关客户ID
     * @return 通讯录相关客户
     */
    @Override
    public WeUser selectWeUserById(Long id)
    {
        return weUserMapper.selectWeUserById(id);
    }

    /**
     * 查询通讯录相关客户列表
     * 
     * @param weUser 通讯录相关客户
     * @return 通讯录相关客户
     */
    @Override
    public List<WeUser> selectWeUserList(WeUser weUser)
    {
        return weUserMapper.selectWeUserList(weUser);
    }

    /**
     * 新增通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    @Override
    public int insertWeUser(WeUser weUser)
    {
        return weUserMapper.insertWeUser(weUser);
    }

    /**
     * 修改通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    @Override
    public int updateWeUser(WeUser weUser)
    {
        return weUserMapper.updateWeUser(weUser);
    }

    /**
     * 批量删除通讯录相关客户
     * 
     * @param ids 需要删除的通讯录相关客户ID
     * @return 结果
     */
    @Override
    public int deleteWeUserByIds(Long[] ids)
    {
        return weUserMapper.deleteWeUserByIds(ids);
    }

    /**
     * 删除通讯录相关客户信息
     * 
     * @param id 通讯录相关客户ID
     * @return 结果
     */
    @Override
    public int deleteWeUserById(Long id)
    {
        return weUserMapper.deleteWeUserById(id);
    }
}
