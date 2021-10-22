package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Lists;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 通讯录相关客户Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-31
 */
@Service
public class WeUserServiceImpl extends ServiceImpl<WeUserMapper, WeUser> implements IWeUserService {
    @Autowired
    private WeUserMapper weUserMapper;

    @Autowired
    private WeUserClient weUserClient;

    @Autowired
    private WeMsgAuditClient weMsgAuditClient;


    @Autowired
    private RuoYiConfig  ruoYiConfig;

    @Override
    public List<WeUser> getListByIds(List<Long> idList) {
        return this.list(new LambdaQueryWrapper<WeUser>().in(WeUser::getUserId,idList));
    }

    @Override
    public WeUser getById(Long id) {
        return super.getById(id);
    }

    /**
     * 查询通讯录相关客户
     *
     * @param userId 通讯录相关客户ID
     * @return 通讯录相关客户
     */
    @Override
    public WeUser getByUserId(String userId) {
        return this.getOne(new LambdaQueryWrapper<WeUser>()
                .eq(WeUser::getUserId, userId));
    }

    /**
     * 查询通讯录相关客户列表
     *
     * @param weUser 通讯录相关客户
     * @return 通讯录相关客户
     */
    @Override
    public List<WeUser> getList(WeUser weUser) {
        return weUserMapper.getList(weUser);
    }

    /**
     * 新增通讯录相关客户
     *
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(WeUser weUser) {
        weUser.setIsActivate(4);
        if (insert2Data(weUser)) {
            weUserClient.createUser(
                    weUser.transformWeUserDto()
            );
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert2Data(WeUser weUser) {
        List<WeUser> list = new ArrayList<>(16);
        list.add(weUser);
        this.weUserMapper.insertBatch(list);
        return true;
    }

    /**
     * 修改通讯录相关客户
     *
     * @param weUser 通讯录相关客户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WeUser weUser) {
        if (update2Data(weUser)) {
            weUserClient.updateUser(weUser.transformWeUserDto());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update2Data(WeUser weUser) {
        return this.updateById(weUser);
    }


    /**
     * 启用或禁用用户
     *
     * @param weUser
     * @return
     */
    @Override
    public void startOrStop(WeUser weUser) {
        this.update(weUser);
    }


    /**
     * 离职未分配员工
     *
     * @param weLeaveUserVo
     * @return
     */
    @Override
    public List<WeLeaveUserVo> leaveNoAllocateUserList(WeLeaveUserVo weLeaveUserVo) {
        return this.weUserMapper.leaveNoAllocateUserList(weLeaveUserVo);
    }


    /**
     * 离职已分配员工
     *
     * @param weLeaveUserVo
     * @return
     */
    @Override
    public List<WeLeaveUserVo> leaveAllocateUserList(WeLeaveUserVo weLeaveUserVo) {
        return this.baseMapper.leaveAllocateUserList(weLeaveUserVo);
    }


    /**
     * 分配离职员工相关数据
     *
     * @param weLeaveUserInfoAllocateVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateLeaveUserAboutData(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {
        try {
            //分配客户
            SpringUtils.getBean(IWeCustomerService.class).allocateWeCustomer(weLeaveUserInfoAllocateVo);
            //分配群组
            SpringUtils.getBean(IWeGroupService.class).allocateWeGroup(weLeaveUserInfoAllocateVo);
            //更新员工状态为已分配
            this.update2Data(WeUser.builder()
                    .userId(weLeaveUserInfoAllocateVo.getHandoverUserid())
                    .isAllocate(WeConstans.LEAVE_ALLOCATE_STATE)
                    .build());
        } catch (Exception e) {
            throw new WeComException(e.getMessage());
        }

    }


    /**
     * 同步成员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void synchWeUser() {
        //同步部门
        SpringUtils.getBean(IWeDepartmentService.class).synchWeDepartment();
        //获取通讯录成员列表
        List<WeUser> weUsers = weUserClient.list(WeConstans.WE_ROOT_DEPARMENT_ID,
                WeConstans.DEPARTMENT_SUB_WEUSER).getWeUsers();
        if (CollectionUtil.isNotEmpty(weUsers)) {
            //不存在的客户设置为离职未分配状态
            this.update(WeUser.builder()
                            .isActivate(WeConstans.WE_USER_IS_LEAVE)
                            .isAllocate(WeConstans.LEAVE_NO_ALLOCATE_STATE)
                            .dimissionTime(new Date())
                    .build(),new LambdaQueryWrapper<WeUser>()
                    .notIn(WeUser::getUserId,weUsers.stream().map(WeUser::getUserId).collect(Collectors.toList())));


            String[] noSyncWeUser = ruoYiConfig.getNoSyncWeUser();
            if(ArrayUtil.isNotEmpty(noSyncWeUser)){
                weUsers=weUsers.stream().filter(o -> !ListUtil.toList(noSyncWeUser).contains(o.getUserId())).collect(Collectors.toList());
            }

            List<List<WeUser>> lists = Lists.partition(weUsers, 500);
            for(List<WeUser> list : lists){
                this.weUserMapper.insertBatch(list);
            }
        }

    }


    /**
     * 删除用户
     *
     * @param userIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String[] userIds) {
        List<WeUser> weUsers=new ArrayList<>();
        CollectionUtil.newArrayList(userIds).forEach(userId-> weUsers.add(
                WeUser.builder()
                        .userId(userId)
                        .isActivate(WeConstans.WE_USER_IS_LEAVE)
                        .dimissionTime(new Date())
                        .build()
        ));
        if(this.updateBatchById(weUsers)){
            weUsers.forEach(k-> weUserClient.deleteUserByUserId(k.getUserId()));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserNoToWeCom(String userId) {
        WeUser weUser = WeUser.builder()
                .userId(userId)
                .isActivate(WeConstans.WE_USER_IS_LEAVE)
                .dimissionTime(new Date())
                .build();
        return this.updateById(weUser);
    }


    /**
     * 获取历史分配记录的成员
     *
     * @param weAllocateCustomersVo
     * @return
     */
    @Override
    public List<WeAllocateCustomersVo> getAllocateCustomers(WeAllocateCustomersVo weAllocateCustomersVo) {
        return this.baseMapper.getAllocateCustomers(weAllocateCustomersVo);
    }


    /**
     * 获取历史分配群
     *
     * @param weAllocateGroupsVo
     * @return
     */
    @Override
    public List<WeAllocateGroupsVo> getAllocateGroups(WeAllocateGroupsVo weAllocateGroupsVo) {
        return this.baseMapper.getAllocateGroups(weAllocateGroupsVo);
    }

    @Override
    public List<WeUser> getPermitUserList(WeMsgAuditDto msgAuditDto) {
        WeMsgAuditDto permitUserList = weMsgAuditClient.getPermitUserList(msgAuditDto);
        return this.list(new LambdaQueryWrapper<WeUser>()
                .in(WeUser::getUserId,permitUserList.getIds()));
    }

    @Override
    public WeUserInfoVo getUserInfo(String code, String agentId) {
        WeUserInfoDto userInfo = weUserClient.getUserInfo(code, agentId);
        return WeUserInfoVo.builder()
                .userId(userInfo.getUserId())
                .deviceId(userInfo.getDeviceId())
                .externalUserId(userInfo.getExternal_userid())
                .openId(userInfo.getOpenId())
                .build();

    }

    @Override
    public List<WeCustomerAddUser> findWeUserByCustomerId(String externalUserid) {
        return this.baseMapper.findWeUserByCutomerId(externalUserid);
    }


}
