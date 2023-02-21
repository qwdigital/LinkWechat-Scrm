package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.CorpUserEnum;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.wecom.entity.customer.groupChat.WeOwnerFilterEntity;
import com.linkwechat.domain.wecom.query.customer.WeBatchCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatListQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferGroupChatQuery;
import com.linkwechat.domain.wecom.query.user.WeLeaveUserQuery;
import com.linkwechat.domain.wecom.vo.customer.WeBatchCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatListVo;
import com.linkwechat.domain.wecom.vo.customer.transfer.WeTransferCustomerVo;
import com.linkwechat.domain.wecom.vo.user.WeLeaveUserVo;
import com.linkwechat.fegin.QwAuthClient;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.mapper.WeLeaveUserMapper;
import com.linkwechat.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.acl.Group;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.collection.IterUtil.forEach;

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

    @Autowired
    RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    QwUserClient qwUserClient;

    @Autowired
    QwSysUserClient qwSysUserClient;








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
                AjaxResult<WeTransferCustomerVo> weTransferCustomerVo = qwCustomerClient.resignedTransferCustomer(
                        WeTransferCustomerQuery.builder()
                                .external_userid(
                                        weAllocateCustomers.stream().map(WeAllocateCustomer::getExternalUserid).collect(Collectors.toList())
                                )
                                .handover_userid(weLeaveUserInfoAllocate.getHandoverUserid())
                                .takeover_userid(weLeaveUserInfoAllocate.getTakeoverUserid())
                                .build()
                );


                if(null != weTransferCustomerVo){
                    WeTransferCustomerVo weTransferCustomer = weTransferCustomerVo.getData();

                    if(weTransferCustomer != null && !weTransferCustomer.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                        throw new WeComException(weTransferCustomer.getErrMsg());
                    }

                }


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

    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_LEAVE_USER)
    public void synchLeaveSysUser() {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeLeaveAllocateUserRk(), JSONObject.toJSONString(loginUser));




    }

    @Override
    @Transactional
    public void synchLeaveSysUserHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
        SecurityContextHolder.setUserType(loginUser.getUserType());

        List<WeLeaveUserVo.Info> infoList=new ArrayList<>();
        this.getWeLeaveUserVo(infoList,null);

        if(CollectionUtil.isNotEmpty(infoList)){

            //待分配的离职客户
            List<WeAllocateCustomer> allocateCustomers=new ArrayList<>();
            //等待分配的客群
            List<WeAllocateGroup> allocateGroups=new ArrayList<>();
            Map<String, List<WeLeaveUserVo.Info>> weLeaveUserVoMap = infoList.stream()
                    .collect(Collectors.groupingBy(WeLeaveUserVo.Info::getHandover_userid));

             weLeaveUserVoMap.forEach((k,v)->{
                        v.stream().forEach(vv->{
                            //分配客户
                            allocateCustomers.add(
                                    WeAllocateCustomer.builder()
                                            .allocateTime(new Date())
                                            .extentType(new Integer(0))
                                            .externalUserid(k)
                                            .handoverUserid(vv.getExternal_userid())
                                            .status(new Integer(1))
                                            .failReason("离职继承")
                                            .build()
                            );

                        });

                        //分配群
                        List<WeGroupChatListVo.GroupChat> groupChatList=new ArrayList<>();

                        //从企业微信拉取当前离职员工等待分配的群
                        iWeGroupService.getGroupChatList(groupChatList, WeGroupChatListQuery.builder()
                                        .owner_filter(WeOwnerFilterEntity.builder()
                                                .userid_list(ListUtil.toList(k))
                                                .build())
                                        .status_filter(1)
                                .build());
                        if(CollectionUtil.isNotEmpty(groupChatList)){
                            groupChatList.stream().forEach(chat->{
                                allocateGroups.add(
                                        WeAllocateGroup.builder()
                                                .chatId(chat.getChatId())
                                                .oldOwner(k)
                                                .status(new Integer(1))
                                                .build()
                                );
                            });
                        }



            });

             //修改员工表的状态为待分配
            List<SysUser> allSysUsers = qwSysUserClient.findAllSysUser(
                    infoList.stream().map(WeLeaveUserVo.Info::getHandover_userid).collect(Collectors.joining(",")),
                    null, null
            ).getData();
            if(CollectionUtil.isNotEmpty(allSysUsers)){
                allSysUsers.stream().forEach(sysUser -> {

                    sysUser.setIsAllocate(CorpUserEnum.NO_IS_ALLOCATE.getKey());

                    WeLeaveUserVo.Info info
                            = weLeaveUserVoMap.get(sysUser.getWeUserId()).stream().findFirst().get();

                    sysUser.setDimissionTime(info!=null?new Date(info.getDimission_time() * 1000L):new Date());
                });
                qwSysUserClient.batchUpdateSysUser(allSysUsers);
            }

            //待分配的客户，群等信息入库
            iWeAllocateGroupService.batchAddOrUpdate(allocateGroups);
            iWeAllocateCustomerService.batchAddOrUpdate(
                    allocateCustomers
            );

        }


    }

    private void getWeLeaveUserVo(List<WeLeaveUserVo.Info> infoList, String nextCursor){

        WeLeaveUserVo leaveUserVos = qwUserClient.getUnassignedList(WeLeaveUserQuery.builder()
                .cursor(nextCursor)
                .build()).getData();


        if(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode().equals(leaveUserVos.getErrCode())){
            infoList.addAll(leaveUserVos.getInfo());

            if (StringUtils.isNotEmpty(leaveUserVos.getNext_cursor())) {
                getWeLeaveUserVo(infoList, nextCursor);
            }


        }



    }


}
