package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.client.WeMsgAuditClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.WeCustomerAddUser;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeUserInfoDto;
import com.linkwechat.wecom.domain.dto.msgaudit.WeMsgAuditDto;
import com.linkwechat.wecom.domain.vo.*;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeDepartmentService;
import com.linkwechat.wecom.service.IWeGroupService;
import com.linkwechat.wecom.service.IWeUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 通讯录相关客户Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@Service
public class WeUserServiceImpl extends ServiceImpl<WeUserMapper,WeUser> implements IWeUserService
{
    @Autowired
    private WeUserMapper weUserMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WeUserClient weUserClient;

    @Autowired
    private WeMsgAuditClient weMsgAuditClient;


    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeGroupService iWeGroupService;


    @Autowired
    private IWeDepartmentService iWeDepartmentService;

    /**
     * 查询通讯录相关客户
     *
     * @param userId 通讯录相关客户ID
     * @return 通讯录相关客户
     */
    @Override
    public WeUser selectWeUserById(String userId)
    {
        return weUserMapper.selectWeUserById(userId);
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
        String[] department = weUser.getDepartment();
        if(ArrayUtil.isNotEmpty(department)){
            weUser.setDepartmentStr(StringUtils.join(department, ","));
        }
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
    public void insertWeUser(WeUser weUser)
    {


        if(this.save(weUser)){
            weUserClient.createUser(
                    weUser.transformWeUserDto()
            );
        }

    }

    @Override
    @Transactional
    public int insertWeUserNoToWeCom(WeUser weUser)
    {
        WeUser weUserInfo = weUserMapper.selectWeUserById(weUser.getUserId());
        if (weUserInfo != null){
            return weUserMapper.updateWeUser(weUser);
        }
        return weUserMapper.insertWeUser(weUser);

    }

    /**
     * 修改通讯录相关客户
     * 
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    @Override
    @Transactional
    public void updateWeUser(WeUser weUser)
    {

        if(this.updateById(weUser)){
            weUserClient.updateUser(
                    weUser.transformWeUserDto()
            );
        }
    }

    @Override
    @Transactional
    public int updateWeUserNoToWeCom(WeUser weUser)
    {
        return weUserMapper.updateWeUser(weUser);
    }




    /**
     *  启用或禁用用户
     * @param weUser
     * @return
     */
    @Override
    public void startOrStop(WeUser weUser) {
        this.updateWeUser(weUser);
    }


    /**
     * 离职未分配员工
     * @param weLeaveUserVo
     * @return
     */
    @Override
    public List<WeLeaveUserVo> leaveNoAllocateUserList(WeLeaveUserVo weLeaveUserVo) {
        return this.weUserMapper.leaveNoAllocateUserList(weLeaveUserVo);
    }


    /**
     * 离职已分配员工
     * @param weLeaveUserVo
     * @return
     */
    @Override
    public List<WeLeaveUserVo> leaveAllocateUserList(WeLeaveUserVo weLeaveUserVo) {
        return this.baseMapper.leaveAllocateUserList(weLeaveUserVo);
    }


    /**
     * 分配离职员工相关数据
     * @param weLeaveUserInfoAllocateVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateLeaveUserAboutData(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        try {
            //分配客户
            iWeCustomerService.allocateWeCustomer(weLeaveUserInfoAllocateVo);

            //分配群组
            iWeGroupService.allocateWeGroup(weLeaveUserInfoAllocateVo);

            //更新员工状态为已分配
            this.updateById(WeUser.builder()
                    .userId(weLeaveUserInfoAllocateVo.getHandoverUserid())
                    .isAllocate(WeConstans.LEAVE_ALLOCATE_STATE)
                    .build());
        }catch (Exception e){
            throw new WeComException(e.getMessage());
        }

    }


    /**
     * 同步成员
     */
    @Override
    @Transactional
    public void synchWeUser(){

        //同步部门
        iWeDepartmentService.synchWeDepartment();

        List<WeUser> weUsers
                = weUserClient.list(WeConstans.WE_ROOT_DEPARMENT_ID, WeConstans.DEPARTMENT_SUB_WEUSER).getWeUsers();
        if(CollectionUtil.isNotEmpty(weUsers)){
            weUsers.forEach(userInfo ->{
                insertWeUserNoToWeCom(userInfo);
            });
        }

    }


    /**
     * 删除用户
     * @param ids
     */
    @Override
    @Transactional
    public void deleteUser(String[] ids) {

        List<WeUser> weUsers=new ArrayList<>();
        CollectionUtil.newArrayList(ids).stream().forEach(k->{
            weUsers.add(
                    WeUser.builder()
                            .userId(k)
                            .isActivate(WeConstans.WE_USER_IS_LEAVE)
                            .dimissionTime(new Date())
                            .build()
            );
        });

        if(this.updateBatchById(weUsers)){
            weUsers.stream().forEach(k->{
                weUserClient.deleteUserByUserId(k.getUserId());
            });
        }

    }

    @Override
    @Transactional
    public int deleteUserNoToWeCom(String userId) {
        WeUser weUser = WeUser.builder()
                .userId(userId)
                .isActivate(WeConstans.WE_USER_IS_LEAVE)
                .dimissionTime(new Date())
                .build();
        return weUserMapper.updateById(weUser);
    }


    /**
     * 获取历史分配记录的成员
     * @param weAllocateCustomersVo
     * @return
     */
    @Override
    public List<WeAllocateCustomersVo> getAllocateCustomers(WeAllocateCustomersVo weAllocateCustomersVo) {
        return this.baseMapper.getAllocateCustomers(weAllocateCustomersVo);
    }


    /**
     * 获取历史分配群
     * @param weAllocateGroupsVo
     * @return
     */
    @Override
    public List<WeAllocateGroupsVo> getAllocateGroups(WeAllocateGroupsVo weAllocateGroupsVo) {
        return this.baseMapper.getAllocateGroups(weAllocateGroupsVo);
    }

    @Override
    public List<WeUser> getPermitUserList(WeMsgAuditDto msgAuditDto) {
        List<String> userIds = redisCache.getCacheList(WeConstans.weMsgAuditKey);
        if (CollectionUtil.isNotEmpty(userIds)){
            WeMsgAuditDto permitUserList = weMsgAuditClient.getPermitUserList(msgAuditDto);
            userIds = Optional.ofNullable(userIds).orElse(permitUserList.getIds());
            redisCache.setCacheList(WeConstans.weMsgAuditKey,userIds);
            redisCache.expire(WeConstans.weMsgAuditKey,10,TimeUnit.MILLISECONDS);
        }
        return weUserMapper.selectBatchIds(userIds);
    }

    @Override
    public WeUserInfoVo getUserInfo(String code,String agentId) {


        WeUserInfoDto getuserinfo = weUserClient.getuserinfo(code,agentId);

        return WeUserInfoVo.builder()
                .userId(getuserinfo.getUserId())
                .deviceId(getuserinfo.getDeviceId())
                .externalUserId(getuserinfo.getExternal_userid())
                .openId(getuserinfo.getOpenId())
                .build();

    }

    @Override
    public List<WeCustomerAddUser> findWeUserByCutomerId(String externalUserid) {
        return this.baseMapper.findWeUserByCutomerId(externalUserid);
    }


}
