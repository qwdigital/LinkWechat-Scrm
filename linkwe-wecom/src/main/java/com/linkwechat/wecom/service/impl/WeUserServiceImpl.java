package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeLeaveUserVo;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeGroupService;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Autowired
    private WeUserClient weUserClient;



    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeGroupService iWeGroupService;

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
    @Transactional
    public int insertWeUser(WeUser weUser)
    {

        int returnCode = weUserMapper.insertWeUser(weUser);

        if(returnCode>0){
            weUserClient.createUser(
                    weUser.transformWeUserDto()
            );
        }

        return returnCode;
    }

    /**
     * 修改通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWeUser(WeUser weUser)
    {
        int returnCode = weUserMapper.updateWeUser(weUser);

        if(returnCode >0){
            weUserClient.updateUser(
                    weUser.transformWeUserDto()
            );
        }

        return returnCode;

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



    /**
     *  启用或禁用用户
     * @param id
     * @param enable
     * @return
     */
    @Override
    @Transactional
    public int startOrStop(Long id, Boolean enable) {

         int returnCode = -1;

         WeUser weUser = this.selectWeUserById(id);

         if(null != weUser){

             weUser.setEnable(enable?WeConstans.WE_USER_START:WeConstans.WE_USER_STOP);

            returnCode=updateWeUser(weUser);
        }


        return returnCode;
    }


    /**
     * 离职分配员工
     * @param weLeaveUserVo
     * @return
     */
    @Override
    public List<WeLeaveUserVo> leaveUserList(WeLeaveUserVo weLeaveUserVo) {
        return this.weUserMapper.leaveUserList(weLeaveUserVo);
    }


    /**
     * 分配离职员工相关数据
     * @param weLeaveUserInfoAllocateVo
     */
    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
    public void allocateLeaveUserAboutData(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        try {
            //分配客户
            iWeCustomerService.allocateWeCustomer(weLeaveUserInfoAllocateVo);

            //分配群组
            iWeGroupService.allocateWeGroup(weLeaveUserInfoAllocateVo);
        }catch (Exception e){
            throw new WeComException(e.getMessage());
        }

    }


}
