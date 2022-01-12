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
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.wecom.annotation.SynchRecord;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeCustomerGroupClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.WeAllocateCustomer;
import com.linkwechat.wecom.domain.WeAllocateGroup;
import com.linkwechat.wecom.domain.WeCustomerAddUser;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.AllocateWeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeUserInfoDto;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupList;
import com.linkwechat.wecom.domain.dto.customer.ExternalUserDetail;
import com.linkwechat.wecom.domain.dto.customer.FollowUserList;
import com.linkwechat.wecom.domain.dto.msgaudit.WeMsgAuditDto;
import com.linkwechat.wecom.domain.vo.*;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private RuoYiConfig  ruoYiConfig;


    @Autowired
    private WeCustomerClient weCustomerClient;

    @Autowired
    private IWeAllocateCustomerService iWeAllocateCustomerService;

    @Autowired
    private WeCustomerGroupClient weCustomerGroupClient;

    @Autowired
    private IWeAllocateGroupService iWeAllocateGroupService;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Override
    public WeUser getById(Long id) {
        return super.getById(id);
    }



    /**
     * 查询通讯录相关客户列表
     *
     * @param weUser 通讯录相关客户
     * @return 通讯录相关客户
     */
    @Override
    public List<WeUser> getList(WeUser weUser) {


       return this.list(new LambdaQueryWrapper<WeUser>()
                .like(StringUtils.isNotEmpty(weUser.getName()),WeUser::getName,weUser.getName())
                .eq(weUser.getIsActivate() !=null,WeUser::getIsActivate,weUser.getIsActivate())
                .eq(weUser.getIsConfigCustomerContact() != null,WeUser::getIsConfigCustomerContact,weUser.getIsConfigCustomerContact())
                .apply(StringUtils.isNotEmpty(weUser.getDepartment()),"FIND_IN_SET('"+weUser.getDepartment()+"',department)")
                .apply(StringUtils.isNotBlank( weUser.getBeginTime()),
                        "date_format (create_time,'%Y-%m-%d') >= date_format('" + weUser.getBeginTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isNotBlank(weUser.getEndTime()),
                        "date_format (create_time,'%Y-%m-%d') <= date_format('" + weUser.getEndTime() + "','%Y-%m-%d')")
               .orderByDesc(WeUser::getCreateTime)
        );
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
//            SpringUtils.getBean(IWeCustomerService.class).allocateWeCustomer(weLeaveUserInfoAllocateVo);
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
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_MAIL_LIST)
    public void synchWeUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        //异步同步一下标签库,解决标签不同步问题
        Threads.SINGLE_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                SecurityContextHolder.setContext(securityContext);


                //同步部门
                SpringUtils.getBean(IWeDepartmentService.class).synchWeDepartment();
                //获取通讯录成员列表
                List<WeUser> weUsers = weUserClient.list(WeConstans.WE_ROOT_DEPARMENT_ID,
                        WeConstans.DEPARTMENT_SUB_WEUSER).getWeUsers();
                if (CollectionUtil.isNotEmpty(weUsers)) {
                    //不存在的客户设置为离职未分配状态
                   update(WeUser.builder()
                            .isActivate(WeConstans.WE_USER_IS_LEAVE)
                            .isAllocate(WeConstans.LEAVE_NO_ALLOCATE_STATE)
                            .dimissionTime(new Date())
                            .build(),new LambdaQueryWrapper<WeUser>()
                            .notIn(WeUser::getUserId,weUsers.stream().map(WeUser::getUserId).collect(Collectors.toList())));


                    List<List<WeUser>> lists = Lists.partition(weUsers, 500);
                    for(List<WeUser> list : lists){
                        weUserMapper.insertBatch(list);
                    }


                    //获取外部联系人列表
                    FollowUserList followUserList = weCustomerClient.getFollowUserList();
                    if (WeConstans.WE_SUCCESS_CODE.equals(followUserList.getErrcode())
                            && ArrayUtil.isNotEmpty(followUserList.getFollow_user())) {

                        //设置员工是否开启外部联系人功能
                        update(WeUser.builder()
                                .isConfigCustomerContact(new Integer(1))
                                .build(),new LambdaQueryWrapper<WeUser>()
                                .eq(WeUser::getUserId,ListUtil.toList(followUserList.getFollow_user())));


                    }



                }

            }
        });




    }


    /**
     * 删除用户
     *
     * @param userIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void deleteUser(String[] userIds) {
        List<WeUser> weUsers=new ArrayList<>();
        CollectionUtil.newArrayList(userIds).forEach(userId-> {

            String[] noSyncWeUser = ruoYiConfig.getNoSyncWeUser();

            if(ArrayUtil.isNotEmpty(noSyncWeUser)){
                if(CollectionUtil.toList(noSyncWeUser).contains(userId)){
                    throw new WeComException("当前员工不可删除");
                }
            }

            weUsers.add(
                    WeUser.builder()
                            .userId(userId)
                            .isActivate(WeConstans.corpUserEnum.ACTIVE_STATE_FIVE.getKey())
                            .dimissionTime(new Date())
                            .build()
            );


        }
       );
        if(this.updateBatchById(weUsers)){
            weUsers.forEach(k-> weUserClient.deleteUserByUserId(k.getUserId()));
            //同步离职成员相关信息分配
            this.synchLeaveUserData();
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


    /**
     * 同步离职成员的相关数据(待分配客户，待分配群)。
     */
    @Override
    @Async
    public void synchLeaveUserData() {

        //同步所需要分配的客户
        List<AllocateWeCustomerDto.Info> infos=new ArrayList<>();
        getLeaveUserCustomer(null,infos);
        if(CollectionUtil.isNotEmpty(infos)){
            //过滤掉不存在的外部联系人
            List<WeAllocateCustomer> allocateCustomers=new ArrayList<>();
            infos.stream().forEach(kk->{
                allocateCustomers.add(
                        WeAllocateCustomer.builder()
                                .allocateTime(new Date())
                                .extentType(new Integer(1))
                                .externalUserid(kk.getExternal_userid())
                                .handoverUserid(kk.getHandover_userid())
                                .status(new Integer(2))
                                .failReason("离职继承")
                                .build()
                );

            });
            iWeAllocateCustomerService.batchAddOrUpdate(allocateCustomers);
        }



        //同步所需分配的客户群
        List<CustomerGroupList.GroupChat> groupChats=new ArrayList<>();
        getLeaveUserGroup(null,groupChats);
        if(CollectionUtil.isNotEmpty(groupChats)){
            List<WeAllocateGroup> weAllocateGroups=new ArrayList<>();
            groupChats.stream().forEach(k->{
                CustomerGroupDetail customerGroupDetail = weCustomerGroupClient.groupChatDetail(CustomerGroupDetail.Params.builder()
                        .chat_id(k.getChatId())
                        .need_name(new Integer(0))
                        .build());

                if(WeConstans.WE_SUCCESS_CODE.equals(customerGroupDetail.getErrcode())
                &&ArrayUtil.isNotEmpty(customerGroupDetail.getGroupChat())){
                    weAllocateGroups.add(WeAllocateGroup.builder()
                            .chatId(k.getChatId())
                            .oldOwner(customerGroupDetail.getGroupChat().stream().findFirst().get().getOwner())
                            .status(new Integer(3))
                            .build());
                }


            });
            iWeAllocateGroupService.batchAddOrUpdate(weAllocateGroups);
        }






    }

    private void getLeaveUserCustomer(String nextCursor, List<AllocateWeCustomerDto.Info> list){
        AllocateWeCustomerDto unassignedList = weUserClient.getUnassignedList(AllocateWeCustomerDto.CheckParm.builder()
                        .cursor(nextCursor)
                .build());

        if (WeConstans.WE_SUCCESS_CODE.equals(unassignedList.getErrcode())
                && ArrayUtil.isNotEmpty(unassignedList.getInfo())) {
            list.addAll(unassignedList.getInfo());
            if (StringUtils.isNotEmpty(unassignedList.getNext_cursor())) {
                getLeaveUserCustomer(unassignedList.getNext_cursor(),list);
            }

        }

    }


    private void getLeaveUserGroup(String nextCursor,List<CustomerGroupList.GroupChat> groupChats){
        CustomerGroupList customerGroupList =
                weCustomerGroupClient.groupChatLists(CustomerGroupList.Params.builder()
                        .status_filter(new Integer(1))
                        .cursor(nextCursor)
                                .limit(new Integer(1000))
                        .build());
        if (WeConstans.WE_SUCCESS_CODE.equals(customerGroupList.getErrcode())
                && ArrayUtil.isNotEmpty(customerGroupList.getGroupChatList())) {
            groupChats.addAll(customerGroupList.getGroupChatList());
            if (StringUtils.isNotEmpty(customerGroupList.getNext_cursor())) {
                getLeaveUserGroup(customerGroupList.getNext_cursor(),groupChats);
            }
        }

    }




}
