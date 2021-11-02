package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeCustomerGroupClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.WeAllocateGroup;
import com.linkwechat.wecom.domain.WeCustomerAddGroup;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.domain.dto.AllocateWeGroupDto;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupList;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupMember;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.mapper.WeGroupMapper;
import com.linkwechat.wecom.service.IWeAllocateGroupService;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import com.linkwechat.wecom.service.IWeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:03
 */
@Service
public class WeGroupServiceImpl extends ServiceImpl<WeGroupMapper, WeGroup> implements IWeGroupService {


    @Autowired
    private WeCustomerGroupClient weCustomerGroupClient;


    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;


    @Autowired
    private IWeAllocateGroupService iWeAllocateGroupService;


    @Autowired
    private WeUserClient weUserClient;


    @Autowired
    private WeCustomerClient weCustomerClient;


    @Override
    public List<WeGroup> selectWeGroupList(WeGroup weGroup) {
        return this.baseMapper.selectWeGroupList(weGroup);
    }

    /**
     * 离职员工群分配
     *
     * @param weLeaveUserInfoAllocateVo
     * @return
     */
    @Override
    @Transactional
    public void allocateWeGroup(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        List<WeGroup> weGroups
                = this.list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getOwner, weLeaveUserInfoAllocateVo.getHandoverUserid()));
        if (CollectionUtil.isNotEmpty(weGroups)) {
            List<WeAllocateGroup> weAllocateGroups = new ArrayList<>();
            //更改本地群主
            weGroups.stream().forEach(k -> {
                k.setOwner(weLeaveUserInfoAllocateVo.getTakeoverUserid());
                weAllocateGroups.add(WeAllocateGroup.builder()
                        .allocateTime(new Date())
                        .chatId(k.getChatId())
                        .newOwner(weLeaveUserInfoAllocateVo.getTakeoverUserid())
                        .oldOwner(weLeaveUserInfoAllocateVo.getHandoverUserid())
                        .build());
            });
            this.saveOrUpdateBatch(weGroups);
            //分配记录保存
            if (CollectionUtil.isNotEmpty(weAllocateGroups)) {
                if (iWeAllocateGroupService.saveOrUpdateBatch(weAllocateGroups)) {

//                    //同步企业微信端
                    weUserClient.allocateGroup(
                            AllocateWeGroupDto.builder()
                                    .chat_id_list(ArrayUtil.toArray(weAllocateGroups.stream().map(WeAllocateGroup::getChatId).collect(Collectors.toList()), String.class))
                                    .new_owner(weLeaveUserInfoAllocateVo.getTakeoverUserid())
                                    .build()
                    );

                }
            }

        }
    }


    /**
     * 客户群同步
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void synchWeGroup() {

        CustomerGroupList customerGroupList =
                weCustomerGroupClient.groupChatLists(new CustomerGroupList().new Params());
        if (customerGroupList.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)
                && CollectionUtil.isNotEmpty(customerGroupList.getGroupChatList())) {

            List<WeGroup> weGroups = new ArrayList<>();
            List<WeGroupMember> weGroupMembers = new ArrayList<>();
            customerGroupList.getGroupChatList().stream().forEach(k -> {
                CustomerGroupDetail customerGroupDetail = weCustomerGroupClient.groupChatDetail(
                        new CustomerGroupDetail().new Params(k.getChatId(), 1)
                );

                if (customerGroupDetail.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)
                        && CollectionUtil.isNotEmpty(customerGroupDetail.getGroupChat())) {

                    customerGroupDetail.getGroupChat().stream().forEach(kk -> {
                        weGroups.add(
                                WeGroup.builder()
                                        .chatId(kk.getChatId())
                                        .groupName(StringUtils.isNotEmpty(kk.getName())? kk.getName() : "@微信群")
                                        .notice(kk.getNotice())
                                        .owner(kk.getOwner())
                                        .addTime(new Date(kk.getCreateTime() * 1000L))
                                        .status(k.getStatus())
                                        .delFlag(new Integer(0))
                                        .adminUserId(Optional.ofNullable(kk.getAdminList()).orElseGet(ArrayList::new).stream().map(admin -> admin.getString("userid")).collect(Collectors.joining(",")))
                                        .build()
                        );

                        List<CustomerGroupMember> memberLists = kk.getMemberList();
                        if (CollectionUtil.isNotEmpty(memberLists)) {
                            memberLists.stream().forEach(member -> {
                                weGroupMembers.add(
                                        WeGroupMember.builder()
                                                .chatId(kk.getChatId())
                                                .userId(member.getUserId())
                                                .joinTime(new Date(member.getJoinTime() * 1000L))
                                                .joinScene(member.getJoinScene())
                                                .type(member.getType())
                                                .unionId(member.getUnionId())
                                                .groupNickName(member.getGroupNickName())
                                                .name(member.getName())
                                                .delFlag(new Integer(0))
                                                .invitorUserId(member.getInvitor() == null ? null : member.getInvitor().getString("userid"))
                                                .build()
                                );
                            });
                        }


                    });

                }
            });
            insertBatchGroupAndMember(weGroups, weGroupMembers);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWeGroup(String chatId) {
        List<WeGroup> weGroups = new ArrayList<>();
        List<WeGroupMember> weGroupMembers = new ArrayList<>();
        CustomerGroupDetail customerGroupDetail = weCustomerGroupClient.groupChatDetail(
                new CustomerGroupDetail().new Params(chatId, 1)
        );
        if (CollectionUtil.isNotEmpty(customerGroupDetail.getGroupChat())) {
            customerGroupDetail.getGroupChat().stream().forEach(kk -> {
                weGroups.add(
                        WeGroup.builder()
                                .chatId(kk.getChatId())
                                .groupName(kk.getName())
                                .notice(kk.getNotice())
                                .owner(kk.getOwner())
                                .addTime(new Date(kk.getCreateTime() * 1000L))
                                .adminUserId(Optional.ofNullable(kk.getAdminList()).orElseGet(ArrayList::new).stream().map(admin -> admin.getString("userid")).collect(Collectors.joining(",")))
                                .build()
                );
                List<CustomerGroupMember> memberLists = kk.getMemberList();
                if (CollectionUtil.isNotEmpty(memberLists)) {
                    memberLists.stream().forEach(member -> {
                        weGroupMembers.add(
                                WeGroupMember.builder()
                                        .chatId(kk.getChatId())
                                        .userId(member.getUserId())
                                        .joinTime(new Date(member.getJoinTime() * 1000L))
                                        .joinScene(member.getJoinScene())
                                        .type(member.getType())
                                        .unionId(member.getUnionId())
                                        .groupNickName(member.getGroupNickName())
                                        .name(member.getName())
                                        .invitorUserId(member.getInvitor() == null ? null : member.getInvitor().getString("userid"))
                                        .build()
                        );
                    });
                }
            });
            insertBatchGroupAndMember(weGroups, weGroupMembers);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWeGroup(String chatId) {
        List<WeGroup> weGroups = new ArrayList<>();
        List<WeGroupMember> weGroupMembers = new ArrayList<>();
        CustomerGroupDetail customerGroupDetail = weCustomerGroupClient.groupChatDetail(
                new CustomerGroupDetail().new Params(chatId, 1)
        );
        if (CollectionUtil.isNotEmpty(customerGroupDetail.getGroupChat())) {
            customerGroupDetail.getGroupChat().stream().forEach(kk -> {
                weGroups.add(
                        WeGroup.builder()
                                .chatId(kk.getChatId())
                                .groupName(kk.getName())
                                .notice(kk.getNotice())
                                .owner(kk.getOwner())
                                .addTime(new Date(kk.getCreateTime() * 1000L))
                                .adminUserId(Optional.ofNullable(kk.getAdminList()).orElseGet(ArrayList::new).stream().map(admin -> admin.getString("userid")).collect(Collectors.joining(",")))
                                .build()
                );
                List<CustomerGroupMember> memberLists = kk.getMemberList();
                if (CollectionUtil.isNotEmpty(memberLists)) {
                    memberLists.stream().forEach(member -> {
                        weGroupMembers.add(
                                WeGroupMember.builder()
                                        .chatId(kk.getChatId())
                                        .userId(member.getUserId())
                                        .joinTime(new Date(member.getJoinTime() * 1000L))
                                        .joinScene(member.getJoinScene())
                                        .type(member.getType())
                                        .unionId(member.getUnionId())
                                        .groupNickName(member.getGroupNickName())
                                        .name(member.getName())
                                        .invitorUserId(member.getInvitor() == null ? null : member.getInvitor().getString("userid"))
                                        .build()
                        );
                    });
                }
            });
            insertBatchGroupAndMember(weGroups, weGroupMembers);
        }
    }

    /**
     * 批量添加
     *
     * @param weGroups       客户群
     * @param weGroupMembers 群成员
     */
    private void insertBatchGroupAndMember(List<WeGroup> weGroups, List<WeGroupMember> weGroupMembers) {

        if (CollectionUtil.isNotEmpty(weGroups)) {
            List<List<WeGroup>> lists = Lists.partition(weGroups, 500);
            for (List<WeGroup> groupList : lists) {
                this.baseMapper.insertBatch(groupList);
                this.remove(new LambdaQueryWrapper<WeGroup>()
                        .notIn(WeGroup::getChatId, groupList.stream().map(WeGroup::getChatId).collect(Collectors.toList())));
            }
        }else{
            List<WeGroup> oldWeGroups = this.list();
            if(CollectionUtil.isNotEmpty(oldWeGroups)){
                this.removeByIds(
                        oldWeGroups.stream().map(WeGroup::getChatId).collect(Collectors.toList())
                );
            }

            //无群无人
            List<WeGroupMember> oldWeGroupMembers = iWeGroupMemberService.list();
            if(CollectionUtil.isNotEmpty(oldWeGroupMembers)){
                iWeGroupMemberService.removeByIds(
                        oldWeGroupMembers.stream().map(WeGroupMember::getId).collect(Collectors.toList())
                );
            }

        }

        if (CollectionUtil.isNotEmpty(weGroupMembers)) {

            List<List<WeGroupMember>> lists = Lists.partition(weGroupMembers, 500);
            for (List<WeGroupMember> groupMemberList : lists) {
                iWeGroupMemberService.remove(
                       new LambdaQueryWrapper<WeGroupMember>()
                               .notIn(WeGroupMember::getUserId,groupMemberList.stream().map(WeGroupMember::getUserId).collect(Collectors.toList()))

                );
                iWeGroupMemberService.insertBatch(groupMemberList);

            }


        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWeGroup(String chatId) {
        this.baseMapper.delete(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getChatId, chatId));
        iWeGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>().eq(WeGroupMember::getChatId, chatId));
    }

    @Override
    public List<WeCustomerAddGroup> findWeGroupByCustomer(String userId, String externalUserid) {
        return this.baseMapper.findWeGroupByCustomer(userId, externalUserid);
    }

}
