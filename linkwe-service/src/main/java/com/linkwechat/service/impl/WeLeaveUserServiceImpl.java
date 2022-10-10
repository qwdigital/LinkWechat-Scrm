package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferGroupChatQuery;
import com.linkwechat.domain.wecom.vo.customer.transfer.WeTransferCustomerVo;
import com.linkwechat.fegin.QwAuthClient;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeLeaveUserMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeLeaveUserServiceImpl implements IWeLeaveUserService {

    @Autowired
    WeLeaveUserMapper weLeaveUserMapper;

    @Autowired
    IWeCustomerService iWeCustomerService;

    @Autowired
    IWeGroupService iWeGroupService;

    @Autowired
    IWeAllocateGroupService iWeAllocateGroupService;

    @Autowired
    IWeAllocateCustomerService iWeAllocateCustomerService;


    @Autowired
    QwCustomerClient qwCustomerClient;





    @Override
    public List<WeLeaveUser> leaveNoAllocateUserList(WeLeaveUser weLeaveUser) {
        return weLeaveUserMapper.leaveNoAllocateUserList(weLeaveUser);
    }

    @Override
    public List<WeLeaveUser> leaveAllocateUserList(WeLeaveUser weLeaveUser) {
        return weLeaveUserMapper.leaveAllocateUserList(weLeaveUser);
    }

    @Override
    public List<WeAllocateCustomer> getAllocateCustomers(WeAllocateCustomer weAllocateCustomers) {
        return weLeaveUserMapper.getAllocateCustomers(weAllocateCustomers);
    }

    @Override
    public List<WeAllocateGroups> getAllocateGroups(WeAllocateGroups weAllocateGroups) {
        return weLeaveUserMapper.getAllocateGroups(weAllocateGroups);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateLeaveUserAboutData(WeLeaveUserInfoAllocate weLeaveUserInfoAllocate) {



        //获取所需分配的客户
        List<WeAllocateCustomer> weAllocateCustomers = iWeAllocateCustomerService.list(new LambdaQueryWrapper<WeAllocateCustomer>()
                .eq(WeAllocateCustomer::getHandoverUserid, weLeaveUserInfoAllocate.getHandoverUserid())
                .eq(WeAllocateCustomer::getStatus, new Integer(1)));
        if(CollectionUtil.isNotEmpty(weAllocateCustomers)){
            weAllocateCustomers.stream().forEach(k->{
                k.setTakeoverUserid(weLeaveUserInfoAllocate.getTakeoverUserid());
                k.setAllocateTime(new Date());
                k.setStatus(new Integer(2));
            });

            if(iWeCustomerService.remove(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getAddUserId,weLeaveUserInfoAllocate.getHandoverUserid())) && iWeAllocateCustomerService.updateBatchById(
                    weAllocateCustomers
            )){
                qwCustomerClient.resignedTransferCustomer(
                        WeTransferCustomerQuery.builder()
                                .external_userid(
                                        weAllocateCustomers.stream().map(WeAllocateCustomer::getExternalUserid).collect(Collectors.toList())
                                )
                                .handover_userid(weLeaveUserInfoAllocate.getHandoverUserid())
                                .takeover_userid(weLeaveUserInfoAllocate.getTakeoverUserid())
                                .build()
                );
            }



        }


        //获取所需分配的群
        List<WeAllocateGroup> weAllocateGroups = iWeAllocateGroupService.list(new LambdaQueryWrapper<WeAllocateGroup>()
                .eq(WeAllocateGroup::getStatus, new Integer(1))
                .eq(WeAllocateGroup::getOldOwner, weLeaveUserInfoAllocate.getHandoverUserid()));
        if(CollectionUtil.isNotEmpty(weAllocateGroups)){
            weAllocateGroups.stream().forEach(k->{
               k.setNewOwner(weLeaveUserInfoAllocate.getTakeoverUserid());
               k.setAllocateTime(new Date());
               k.setStatus(new Integer(2));
            });

            if(iWeGroupService.remove(new LambdaQueryWrapper<WeGroup>()
                    .eq(WeGroup::getOwner,weLeaveUserInfoAllocate.getHandoverUserid()))
            && iWeAllocateGroupService.updateBatchById(weAllocateGroups)){

                qwCustomerClient.transferGroupChat(
                        WeTransferGroupChatQuery.builder()
                                .chat_id_list(weAllocateGroups.stream().map(WeAllocateGroup::getChatId).collect(Collectors.toList()))
                                .new_owner(weLeaveUserInfoAllocate.getTakeoverUserid())
                                .build()
                );

            }

        }


        //修改员工状态为已分配
        this.weLeaveUserMapper.updateWeUserIsAllocate(weLeaveUserInfoAllocate.getHandoverUserid());


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWaitAllocateCustomerAndGroup(String[] weUserIds) {
        List<WeAllocateCustomer> allocateCustomers=new ArrayList<>();
        List<WeAllocateGroup> weAllocateGroups=new ArrayList<>();
        Arrays.asList(weUserIds).forEach(weUserId->{
            //客户分配
            List<WeCustomersVo> weCustomerList = iWeCustomerService.findWeCustomerList(WeCustomersQuery.builder()
                    .delFlag(Constants.COMMON_STATE)
                    .firstUserId(weUserId)
                    .build(), null);

            if(CollectionUtil.isNotEmpty(weCustomerList)){
                weCustomerList.stream().forEach(k->{
                    WeAllocateCustomer allocateCustomer = WeAllocateCustomer.builder()
                            .allocateTime(new Date())
                            .extentType(new Integer(0))
                            .externalUserid(k.getExternalUserid())
                            .handoverUserid(weUserId)
                            .status(new Integer(1))
                            .failReason("离职继承")
                            .build();
                    allocateCustomer.setCreateBy(SecurityUtils.getUserName());
                    allocateCustomer.setCreateById(SecurityUtils.getUserId());
                    allocateCustomer.setCreateTime(new Date());
                    allocateCustomer.setUpdateBy(SecurityUtils.getUserName());
                    allocateCustomer.setUpdateById(SecurityUtils.getUserId());
                    allocateCustomer.setUpdateTime(new Date());
                    allocateCustomers.add(allocateCustomer);
                });




            }


            //群分配记录
            List<WeGroup> weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
                    .eq(WeGroup::getOwner, weUserId));
            if(CollectionUtil.isNotEmpty(weGroups)){
                weGroups.stream().forEach(weGroup -> {

                    WeAllocateGroup weAllocateGroup = WeAllocateGroup.builder()
                            .chatId(weGroup.getChatId())
                            .oldOwner(weUserId)
                            .status(new Integer(1))
                            .build();
                    weAllocateGroup.setCreateBy(SecurityUtils.getUserName());
                    weAllocateGroup.setCreateById(SecurityUtils.getUserId());
                    weAllocateGroup.setCreateTime(new Date());
                    weAllocateGroup.setUpdateBy(SecurityUtils.getUserName());
                    weAllocateGroup.setUpdateById(SecurityUtils.getUserId());
                    weAllocateGroup.setUpdateTime(new Date());
                    weAllocateGroups.add(
                            weAllocateGroup
                    );
                });


            }

        });

        if(CollectionUtil.isNotEmpty(allocateCustomers)){
            iWeAllocateCustomerService.saveOrUpdateBatch(allocateCustomers);
        }


        if(CollectionUtil.isNotEmpty(weAllocateGroups)){
            iWeAllocateGroupService.saveOrUpdateBatch(weAllocateGroups);
        }



    }

}
