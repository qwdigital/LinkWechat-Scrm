package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.CorpUserEnum;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
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
import com.linkwechat.mapper.SysLeaveUserMapper;
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
public class WeLeaveUserServiceImpl extends ServiceImpl<SysLeaveUserMapper,SysLeaveUser> implements IWeLeaveUserService {

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
                .isNull(WeAllocateCustomer::getTakeoverUserid)
                .eq(WeAllocateCustomer::getHandoverUserid, weLeaveUserInfoAllocate.getHandoverUserid()));

        if(CollectionUtil.isNotEmpty(weAllocateCustomers)){
            weAllocateCustomers.stream().forEach(k->{
                k.setTakeoverUserid(weLeaveUserInfoAllocate.getTakeoverUserid());
                k.setAllocateTime(new Date());
            });

            if(iWeAllocateCustomerService.updateBatchById(
                    weAllocateCustomers)){


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
                        throw new WeComException( WeErrorCodeEnum.parseEnum(weTransferCustomer.getErrCode()).getErrorMsg());

                    }

                }
            }

        }


        //获取所需分配的群
        List<WeAllocateGroup> weAllocateGroups = iWeAllocateGroupService.list(new LambdaQueryWrapper<WeAllocateGroup>()
                        .isNull(WeAllocateGroup::getNewOwner)
                .eq(WeAllocateGroup::getOldOwner, weLeaveUserInfoAllocate.getHandoverUserid()));
        if(CollectionUtil.isNotEmpty(weAllocateGroups)){
            weAllocateGroups.stream().forEach(k->{
               k.setNewOwner(weLeaveUserInfoAllocate.getTakeoverUserid());
               k.setAllocateTime(new Date());
            });

            if(iWeAllocateGroupService.updateBatchById(weAllocateGroups)){

                AjaxResult<WeTransferCustomerVo> ajaxResult = qwCustomerClient.transferGroupChat(
                        WeTransferGroupChatQuery.builder()
                                .chat_id_list(weAllocateGroups.stream().map(WeAllocateGroup::getChatId).collect(Collectors.toList()))
                                .new_owner(weLeaveUserInfoAllocate.getTakeoverUserid())
                                .build()
                );

                if(ajaxResult != null){
                    WeTransferCustomerVo weTransferCustomerVo = ajaxResult.getData();
                                        if(weTransferCustomerVo != null && !weTransferCustomerVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){

                        throw new WeComException( WeErrorCodeEnum.parseEnum(weTransferCustomerVo.getErrCode()).getErrorMsg());
                    }

                }

            }
        }

        this.update(SysLeaveUser.builder()
                        .isAllocate(CorpUserEnum.YES_IS_ALLOCATE.getKey())
                .build(), new LambdaQueryWrapper<SysLeaveUser>()
                .eq(SysLeaveUser::getWeUserId,weLeaveUserInfoAllocate.getHandoverUserid()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWaitAllocateCustomerAndGroup(List<String> weUserIds) {
        List<WeAllocateCustomer> allocateCustomers=new ArrayList<>();
        List<WeAllocateGroup> weAllocateGroups=new ArrayList<>();
        weUserIds.forEach(weUserId->{
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
//    @SynchRecord(synchType = SynchRecordConstants.SYNCH_LEAVE_USER)
    public void synchLeaveSysUser() {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeLeaveAllocateUserRk(), JSONObject.toJSONString(loginUser));




    }

    @Override
    @Transactional
    public void synchLeaveSysUserHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        if(Objects.isNull(loginUser)){
            return;
        }
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




//            //离职相关更近人数据更新(后去如果企业微信开放离职员工数据接口可拓展)
//            iWeCustomerService.synchWeCustomerByAddIds(
//                    infoList.stream().map(WeLeaveUserVo.Info::getHandover_userid).collect(Collectors.toList())
//            );

             weLeaveUserVoMap.forEach((k,v)->{

                 List<WeCustomer> weCustomers = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                         .eq(WeCustomer::getAddUserId, k)
                         .in(WeCustomer::getExternalUserid, v.stream().map(WeLeaveUserVo.Info::getExternal_userid).collect(Collectors.toList())));

                 if(CollectionUtil.isNotEmpty(weCustomers)){

                     //分配客户
                     weCustomers.stream().forEach(vv->{
                         allocateCustomers.add(
                                 WeAllocateCustomer.builder()
                                         .id(SnowFlakeUtil.nextId())
                                         .allocateTime(new Date())
                                         .extentType(0)
                                         .externalUserid(vv.getExternalUserid())
                                         .handoverUserid(k)
                                         .status(1)
                                         .failReason("离职继承")
                                         .build()
                         );

                     });


                 }



                 //从企业微信拉取当前离职员工等待分配的群(后期开放拉取离职客户群可拓展)
                 List<WeGroupChatListVo.GroupChat> weGroups=iWeGroupService.synchWeGroup(WeGroupChatListQuery.builder()
                                        .owner_filter(WeOwnerFilterEntity.builder()
                                                .userid_list(ListUtil.toList(k))
                                                .build())
                                        .status_filter(1)
                                .build());
//                 List<WeGroup> weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
//                         .eq(WeGroup::getOwner, k));
                 if(CollectionUtil.isNotEmpty(weGroups)){
                     weGroups.stream().forEach(chat->{
                                allocateGroups.add(
                                        WeAllocateGroup.builder()
                                                .id(SnowFlakeUtil.nextId())
                                                .chatId(chat.getChatId())
                                                .oldOwner(k)
                                                .status(1)
                                                .build()
                                );
                            });
                        }


                 //离职员工入库(后期开放离职员工数据可拓展)
//                 AjaxResult<SysUser> ajaxResult = qwSysUserClient.findOrSynchSysUser(k);
                 AjaxResult<List<SysUser>> ajaxResult = qwSysUserClient.findAllSysUser(k, null, null);

                 if(ajaxResult != null && CollectionUtil.isNotEmpty(ajaxResult.getData())){
                     SysUser sysUser = ajaxResult.getData().stream().findFirst().get();
                     sysUser.setIsUserLeave(1);

                     //构建离职员工数据
                     this.baseMapper.batchAddOrUpdate(
                             ListUtil.toList(
                                     SysLeaveUser.builder().userName(sysUser.getUserName())
                                             .deptNames(sysUser.getDeptName())
                                             .weUserId(sysUser.getWeUserId())
                                             .allocateCustomerNum(v.size())
                                             .dimissionTime(new Date(v.stream().findFirst().get().getDimission_time() * 1000L))
                                             .allocateGroupNum(weGroups.size()).build()
                             )
                     );


                     //更新
                     SysUserQuery userQuery=new SysUserQuery();
                     userQuery.setIsUserLeave(1);
                     userQuery.setWeUserId(sysUser.getWeUserId());
                     qwSysUserClient.edit(userQuery);
                 }

             });

            //待分配的客户，群等信息入库
            if(CollectionUtil.isNotEmpty(allocateGroups)){
                iWeGroupService.update(WeGroup.builder()
                                .ownerLeave(1)
                        .build(), new LambdaQueryWrapper<WeGroup>()
                        .in(WeGroup::getChatId,allocateGroups.stream().map(WeAllocateGroup::getChatId).collect(Collectors.toList())));
                iWeAllocateGroupService.batchAddOrUpdate(allocateGroups);
            }


            //离职待分配员工客户数据处理
            if(CollectionUtil.isNotEmpty(allocateCustomers)){
                allocateCustomers.stream().forEach(k->{
                    iWeCustomerService.saveOrUpdate(WeCustomer.builder()
                            .addUserLeave(1)
                            .build(),new LambdaQueryWrapper<WeCustomer>()
                            .eq(WeCustomer::getExternalUserid,k.getExternalUserid())
                            .eq(WeCustomer::getAddUserId,k.getHandoverUserid()));
                });


                iWeAllocateCustomerService.batchAddOrUpdate(
                        allocateCustomers
                );
            }


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
