package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.wecom.client.WeCustomerGroupClient;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupList;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupMember;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.mapper.WeGroupMapper;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import com.linkwechat.wecom.service.IWeGroupService;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:03
 */
@Service
public class WeGroupServiceImpl extends ServiceImpl<WeGroupMapper,WeGroup> implements IWeGroupService {



    @Autowired
    private WeCustomerGroupClient weCustomerGroupClient;


    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;

    @Autowired
    private IWeUserService weUserService;

    @Autowired
    private IWeCustomerService weCustomerService;


    public List<WeGroup> selectWeGroupList(WeGroup weGroup) {
        return this.baseMapper.selectWeGroupList(weGroup);
    }

    /**
     * 离职员工群分配
     * @param weLeaveUserInfoAllocateVo
     * @return
     */
    @Override
    @Transactional
    public void allocateWeGroup(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {
//        //分配群
//        List<WeGroup> weGroups = this.selectWeGroupList(WeGroup.builder()
//                .groupLeaderUserId(weLeaveUserInfoAllocateVo.getHandoverUserid())
//                .build());
//        if(CollectionUtil.isNotEmpty(weGroups)){
//
//            this.batchLogicDeleteByIds(
//                    weGroups.stream().map(WeGroup::getId).collect(Collectors.toList())
//            );
//
//            weGroups.stream().forEach(k->{
//                k.setId(SnowFlakeUtil.nextId());
//                k.setGroupLeaderUserId(weLeaveUserInfoAllocateVo.getTakeoverUserid());
//            });
//
//            this.batchInsetWeGroup(weGroups);
//        }
    }


    /**
     * 客户群同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchWeGroup() {

        CustomerGroupList customerGroupList =
                weCustomerGroupClient.groupChatLists(new CustomerGroupList().new Params());
        if(customerGroupList.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)
        && CollectionUtil.isNotEmpty(customerGroupList.getGroup_chat_list())){

            List<WeGroup> weGroups=new ArrayList<>();
            List<WeGroupMember> weGroupMembers=new ArrayList<>();
             customerGroupList.getGroup_chat_list().stream().forEach(k->{


                CustomerGroupDetail customerGroupDetail = weCustomerGroupClient.groupChatDetail(
                        new CustomerGroupDetail().new Params(k.getChat_id())
                );

                if(customerGroupDetail.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)
                 && CollectionUtil.isNotEmpty(customerGroupDetail.getGroup_chat())){

                    customerGroupDetail.getGroup_chat().stream().forEach(kk->{
                        weGroups.add(
                                WeGroup.builder()
                                        .chatId(kk.getChat_id())
                                        .groupName(kk.getName())
                                        .notice(kk.getNotice())
                                        .owner(kk.getOwner())
                                        .createTime(kk.getCreate_time())
                                        .status(k.getStatus())
                                        .build()
                        );
                        List<CustomerGroupMember> memberLists = kk.getMember_list();
                        if(CollectionUtil.isNotEmpty(memberLists)){
                            memberLists.stream().forEach(member->{

                                weGroupMembers.add(
                                        WeGroupMember.builder()
                                                .chatId(kk.getChat_id())
                                                .userId(member.getUserid())
                                                .joinTime(member.getJoin_time())
                                                .joinScene(member.getJoin_scene())
                                                .joinType(member.getType())
                                                .unionId(member.getUnionid())
                                                .build()
                                );
                            });
                        }


                    });

                }
            });

              //同步组织架构成员
               weUserService.synchWeUser();
              //同步客户
              weCustomerService.synchWeCustomer();

             this.saveOrUpdateBatch(weGroups);
            iWeGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>().in(WeGroupMember::getChatId,
                    weGroups.stream().map(WeGroup::getChatId).collect(Collectors.toList())));
            iWeGroupMemberService.saveOrUpdateBatch(weGroupMembers);


        }


    }

}
