package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.enums.GroupUpdateDetailEnum;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.enums.message.MessageTypeEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.customer.vo.WeCustomerAddGroupVo;
import com.linkwechat.domain.customer.vo.WeCustomerChannelCountVo;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.groupchat.query.WeGroupChatQuery;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.groupchat.vo.WeGroupChannelCountVo;
import com.linkwechat.domain.wecom.entity.customer.groupChat.WeGroupMemberEntity;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatDetailQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatListQuery;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatDetailVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatListVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeGroupMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 企业微信群(WeGroup)
 *
 * @author danmo
 * @since 2022-04-02 13:35:13
 */
@Slf4j
@Service
public class WeGroupServiceImpl extends ServiceImpl<WeGroupMapper, WeGroup> implements IWeGroupService {

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;



    @Autowired
    private IWeCustomerTrajectoryService weCustomerTrajectoryService;

    @Autowired
    private IWeMessagePushService iWeMessagePushService;

    @Autowired
    private IWeFissionService iWeFissionService;
    @Resource
    private IWeMessageNotificationService weMessageNotificationService;

    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;


    @Override
    public List<LinkGroupChatListVo> getPageList(WeGroupChatQuery query, PageDomain pageDomain) {
        List<LinkGroupChatListVo> groupChatListVos = new ArrayList<>();
        List<String> weGroupListIds = this.baseMapper.findWeGroupListIds(query, pageDomain);

        if (CollectionUtil.isNotEmpty(weGroupListIds)) {
            groupChatListVos = this.baseMapper.selectWeGroupListByIds(weGroupListIds);
        }

        return groupChatListVos;
    }

    @Override
    public long countWeGroupListIds(WeGroupChatQuery query) {
        return this.baseMapper.countWeGroupListIds(query);
    }

    @Override
    public List<LinkGroupChatListVo> getPageList(WeGroupChatQuery query) {

        List<LinkGroupChatListVo> linkGroupChatListVos = this.baseMapper.selectWeGroupList(query);
        return linkGroupChatListVos;
    }

    @Override
    public List<LinkGroupChatListVo> selectWeGroupListByApp(WeGroupChatQuery query){


        return this.baseMapper.selectWeGroupListByApp(query);
    }

    @Override
    public LinkGroupChatListVo getInfo(Long id) {
        WeGroupChatQuery query = new WeGroupChatQuery();
        query.setId(id);
        List<LinkGroupChatListVo> groupChat = this.baseMapper.selectWeGroupList(query);
        if (CollectionUtil.isNotEmpty(groupChat)) {
            return groupChat.get(0);
        }
        return null;
    }

    @Override
    public LinkGroupChatListVo getInfo(String chatId) {
//        WeGroupChatQuery query = new WeGroupChatQuery();
//        query.setChatId(chatId);
//        List<LinkGroupChatListVo> groupChat = this.baseMapper.selectWeGroupList(query);
//        if (CollectionUtil.isNotEmpty(groupChat)) {
//            return groupChat.get(0);
//        }
        return this.baseMapper.selectWeGroupDetail(chatId);
    }

    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_GROUP)
    public void synchWeGroup() {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        List<WeGroupChatListVo.GroupChat> groupChatList=new ArrayList<>();
        this.getGroupChatList(groupChatList,WeGroupChatListQuery.builder().build());
        //当前群先入库
        if (CollectionUtil.isNotEmpty(groupChatList)){

            List<List<WeGroupChatListVo.GroupChat>> partition = Lists.partition(groupChatList, 5);


            for (List<WeGroupChatListVo.GroupChat> groupChat : partition) {
                loginUser.setChatIds(groupChat.stream().map(WeGroupChatListVo.GroupChat::getChatId)
                        .collect(Collectors.toList()));
                //发送通知更新群群详情
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeGroupChatRk(), JSONObject.toJSONString(loginUser));

            }

        }

    }


    @Override
    public void synchWeGroupHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserType(loginUser.getUserType());


        List<String> chatIds = loginUser.getChatIds();
        if(CollectionUtil.isNotEmpty(chatIds)){
            chatIds.stream().forEach(chatId->{
                WeGroupChatDetailQuery groupChatDetailQuery = new WeGroupChatDetailQuery(chatId, 1);
                WeGroupChatDetailVo weGroupChatDetailVo = qwCustomerClient.getGroupChatDetail(groupChatDetailQuery).getData();
                List<WeGroup> weGroups = new LinkedList<>();
                List<WeGroupMember> weGroupMembers = new LinkedList<>();
                if (weGroupChatDetailVo.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())
                        && weGroupChatDetailVo.getGroupChat() != null) {

                    WeGroupChatDetailVo.GroupChatDetail detail = weGroupChatDetailVo.getGroupChat();
                    WeGroup weGroup = new WeGroup();
                    weGroup.transformQwParams(detail);
                    weGroup.setDelFlag(Constants.COMMON_STATE);
                    weGroup.setCreateBy(SecurityUtils.getUserName());
                    weGroup.setCreateById(SecurityUtils.getUserId());
                    weGroup.setUpdateBy(SecurityUtils.getUserName());
                    weGroup.setUpdateById(SecurityUtils.getUserId());
                    weGroup.setCreateTime(new Date());
                    weGroup.setUpdateTime(new Date());
                    weGroups.add(weGroup);

                    List<WeGroupMemberEntity> memberLists = detail.getMemberList();
                    if (CollectionUtil.isNotEmpty(memberLists)) {
                        memberLists.forEach(groupMember -> {
                            WeGroupMember weGroupMember = new WeGroupMember();
                            weGroupMember.setChatId(chatId);
                            weGroupMember.transformQwParams(groupMember);
                            weGroupMember.setCreateTime(new Date());
                            weGroupMember.setUpdateTime(new Date());
                            weGroupMember.setDelFlag(Constants.COMMON_STATE);
                            weGroupMember.setCreateBy(SecurityUtils.getUserName());
                            weGroupMember.setCreateById(SecurityUtils.getUserId());
                            weGroupMember.setCreateTime(new Date());
                            weGroupMember.setUpdateBy(SecurityUtils.getUserName());
                            weGroupMember.setUpdateById(SecurityUtils.getUserId());
                            weGroupMember.setUpdateTime(new Date());
                            weGroupMembers.add(weGroupMember);
                        });
                    }


                    if(CollectionUtil.isNotEmpty(weGroups)){
                        //删除不包含当前的群以及成员
                        this.baseMapper.insertBatch(weGroups);
                    }


                    if(CollectionUtil.isNotEmpty(weGroupMembers)){
                        iWeGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>()
                                .notIn(WeGroupMember::getUserId,weGroupMembers.stream().map(WeGroupMember::getUserId)
                                        .collect(Collectors.toList()))
                                .eq(WeGroupMember::getChatId,chatId));
                        iWeGroupMemberService.insertBatch(weGroupMembers);
                    }


                }




            });

        }


    }



    @Override
    public List<WeGroup> findGroupInfoFromWechat(WeGroupChatListQuery chatListQuery){


        List<WeGroup> weGroups = new LinkedList<>();

        List<WeGroupChatListVo.GroupChat> groupChatList=new ArrayList<>();

        this.getGroupChatList(groupChatList,chatListQuery);

        if (CollectionUtil.isNotEmpty(groupChatList)) {

            //获取相关详情
            for (WeGroupChatListVo.GroupChat groupChat : groupChatList) {
                WeGroupChatDetailQuery groupChatDetailQuery = new WeGroupChatDetailQuery(groupChat.getChatId(), 1);
                AjaxResult<WeGroupChatDetailVo> result
                        = qwCustomerClient.getGroupChatDetail(groupChatDetailQuery);
                if(null != result){
                    WeGroupChatDetailVo data = result.getData();
                    if(null != data && data.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                        WeGroupChatDetailVo.GroupChatDetail groupChatDetail = data.getGroupChat();
                        if(null != groupChatDetail){

                            WeGroup weGroup = new WeGroup();
                            weGroup.transformQwParams(groupChatDetail);
                            weGroup.setStatus(groupChat.getStatus());
                            weGroup.setDelFlag(Constants.COMMON_STATE);
                            weGroup.setCreateBy(SecurityUtils.getUserName());
                            weGroup.setCreateById(SecurityUtils.getUserId());
                            weGroup.setUpdateBy(SecurityUtils.getUserName());
                            weGroup.setUpdateById(SecurityUtils.getUserId());
                            weGroup.setCreateTime(new Date());
                            weGroup.setUpdateTime(new Date());
                            weGroups.add(weGroup);

                        }


                    }



                }


            }


//
//            List<WeGroup> weGroupList = this.list(new LambdaQueryWrapper<WeGroup>()
//                    .in(WeGroup::getChatId, groupChatList.stream().map(WeGroupChatListVo.GroupChat::getChatId).collect(Collectors.toList())));
//
//
//
//
//
//            groupChatList.stream().forEach(k->{
//
//                WeGroup weGroup = new WeGroup();
//                weGroup.setChatId(k.getChatId());
//                weGroup.setGroupName("@企微群");
//                weGroup.setDelFlag(Constants.COMMON_STATE);
//                weGroup.setCreateBy(SecurityUtils.getUserName());
//                weGroup.setCreateById(SecurityUtils.getUserId());
//                weGroup.setUpdateBy(SecurityUtils.getUserName());
//                weGroup.setUpdateById(SecurityUtils.getUserId());
//                weGroup.setCreateTime(new Date());
//                weGroup.setUpdateTime(new Date());
//
//
//                if(CollectionUtil.isNotEmpty(weGroupList)) {
//                    WeGroup oldWeGroup
//                            = weGroupList.stream().filter(item -> item.getChatId().equals(k.getChatId())).findFirst().get();
//                    if(null != oldWeGroup){
//                        weGroup.setGroupName(oldWeGroup.getGroupName());
//                    }
//
//                }
//
//
//                weGroups.add(weGroup);
//
//            });







//            for (WeGroupChatListVo.GroupChat groupChat : groupChatList) {
//                WeGroupChatDetailQuery groupChatDetailQuery = new WeGroupChatDetailQuery(groupChat.getChatId(), 1);
//                WeGroupChatDetailVo weGroupChatDetailVo = qwCustomerClient.getGroupChatDetail(groupChatDetailQuery).getData();
//
//                if (weGroupChatDetailVo.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode()) && weGroupChatDetailVo.getGroupChat() != null) {
//                    WeGroupChatDetailVo.GroupChatDetail detail = weGroupChatDetailVo.getGroupChat();
//                    WeGroup weGroup = new WeGroup();
//                    weGroup.transformQwParams(detail);
//                    weGroup.setStatus(groupChat.getStatus());
//                    weGroup.setDelFlag(Constants.COMMON_STATE);
//                    weGroup.setCreateBy(SecurityUtils.getUserName());
//                    weGroup.setCreateById(SecurityUtils.getUserId());
//                    weGroup.setUpdateBy(SecurityUtils.getUserName());
//                    weGroup.setUpdateById(SecurityUtils.getUserId());
//                    weGroup.setCreateTime(new Date());
//                    weGroup.setUpdateTime(new Date());
//                    weGroups.add(weGroup);
//                }
//
//            }



        }

        return weGroups;
    }


    @Override
    public void getGroupChatList( List<WeGroupChatListVo.GroupChat> groupChatList,WeGroupChatListQuery chatListQuery){
        chatListQuery.setLimit(500);
        WeGroupChatListVo groupChatListVo = qwCustomerClient.getGroupChatList(chatListQuery).getData();
        if (groupChatListVo.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())
                && CollectionUtil.isNotEmpty(groupChatListVo.getGroupChatList())) {
            groupChatList.addAll(groupChatListVo.getGroupChatList());

            if(StringUtils.isNotEmpty(groupChatListVo.getNextCursor())){
                chatListQuery.setCursor(groupChatListVo.getNextCursor());
                getGroupChatList(groupChatList,chatListQuery);
            }

        }



    }




    @Override
    @Transactional
    public void createWeGroup(String chatId, String corpId,boolean isCallBack) {
        List<WeGroup> weGroups = new ArrayList<>();
        List<WeGroupMember> weGroupMembers = new ArrayList<>();
        WeGroupChatDetailQuery query = new WeGroupChatDetailQuery(chatId, 1);
        WeGroupChatDetailVo chatDetailVo = qwCustomerClient.getGroupChatDetail(query).getData();
        if (chatDetailVo != null && chatDetailVo.getGroupChat() != null) {
            WeGroupChatDetailVo.GroupChatDetail detail = chatDetailVo.getGroupChat();
            WeGroup weGroup = new WeGroup();
            weGroup.transformQwParams(detail);
            weGroup.setCreateBy(SecurityUtils.getUserName());
            weGroup.setCreateById(SecurityUtils.getUserId());
            weGroup.setCreateTime(new Date());
            weGroup.setUpdateBy(SecurityUtils.getUserName());
            weGroup.setUpdateById(SecurityUtils.getUserId());
            weGroup.setUpdateTime(new Date());
            weGroup.setDelFlag(0);
            weGroup.setStatus(0);
            weGroups.add(weGroup);

            List<WeGroupMemberEntity> memberLists = detail.getMemberList();
            if (CollectionUtil.isNotEmpty(memberLists)) {
                memberLists.forEach(groupMember -> {
                    WeGroupMember weGroupMember = new WeGroupMember();
                    weGroupMember.setChatId(chatId);
                    weGroupMember.transformQwParams(groupMember);
                    weGroupMember.setDelFlag(0);
                    weGroupMember.setCreateBy(SecurityUtils.getUserName());
                    weGroupMember.setCreateById(SecurityUtils.getUserId());
                    weGroupMember.setCreateTime(new Date());
                    weGroupMember.setUpdateBy(SecurityUtils.getUserName());
                    weGroupMember.setUpdateById(SecurityUtils.getUserId());
                    weGroupMember.setUpdateTime(new Date());
                    weGroupMembers.add(weGroupMember);
                });
            }
            insertBatchGroupAndMember(weGroups, weGroupMembers,isCallBack);
            weCustomerTrajectoryService.createBuildOrDissGroupTrajectory(weGroups,true);


            if(isCallBack){
                //通知新群sop满足条件则加入执行计划
                LinkGroupChatListVo linkGroupChatListVo=new LinkGroupChatListVo();
                linkGroupChatListVo.setGroupName(weGroup.getGroupName());
                linkGroupChatListVo.setChatId(weGroup.getChatId());
                linkGroupChatListVo.setOwner(weGroup.getOwner());
                linkGroupChatListVo.setAddTime(weGroup.getAddTime());
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getSopEx(), rabbitMQSettingConfig.getNewWeGroupSopRk(), JSONObject.toJSONString(linkGroupChatListVo));
            }

        }
    }

    @Override
    @Transactional
    public void deleteWeGroup(String chatId) {

        List<WeGroup> weGroups = this.list(new LambdaQueryWrapper<WeGroup>()
                .eq(WeGroup::getChatId, chatId));

        if(CollectionUtil.isNotEmpty(weGroups)){
            this.remove(new LambdaQueryWrapper<WeGroup>()
                    .eq(WeGroup::getChatId,chatId));
            weGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>()
                    .eq(WeGroupMember::getChatId,chatId));


            weCustomerTrajectoryService.createBuildOrDissGroupTrajectory(weGroups,false);
        }


    }


    /**
     * 批量添加
     *
     * @param weGroups       客户群
     * @param weGroupMembers 群成员
     * @param isCallBack  是否回掉 true回掉调用该方法,false非回掉触发改方法
     */
    private void insertBatchGroupAndMember(List<WeGroup> weGroups, List<WeGroupMember> weGroupMembers,boolean isCallBack) {

        if (CollectionUtil.isNotEmpty(weGroups)) {
            if (!isCallBack) {
                //删除已经不存在的群
                this.remove(new LambdaQueryWrapper<WeGroup>()
                        .notIn(WeGroup::getChatId,weGroups.stream().map(WeGroup::getChatId).collect(Collectors.toList())));

                //群下不包含的客户
                weGroups.stream().forEach(weGroup -> {

                    if(CollectionUtil.isNotEmpty(weGroupMembers)){
                        List<WeGroupMember> weGroupMemberss = weGroupMembers.stream()
                                .filter(weGroupMember -> weGroupMember.getChatId().equals(weGroup.getChatId()))
                                .collect(Collectors.toList());

                        weGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>()
                                .eq(WeGroupMember::getChatId,weGroup.getChatId())
                                .notIn(CollectionUtil.isNotEmpty(weGroupMemberss),WeGroupMember::getUserId,
                                        weGroupMemberss.stream().map(WeGroupMember::getUserId).collect(Collectors.toList())));
                    }else{
                        weGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>()
                                .eq(WeGroupMember::getChatId,weGroup.getChatId()));
                    }

                });



            }

            List<List<WeGroup>> lists = Lists.partition(weGroups, 500);
            for (List<WeGroup> groupList : lists) {

                this.baseMapper.insertBatch(groupList);
            }
        }

        if (CollectionUtil.isNotEmpty(weGroupMembers)) {
            List<List<WeGroupMember>> lists = Lists.partition(weGroupMembers, 500);
            for (List<WeGroupMember> groupMemberList : lists) {

                //物理删除已有的del_flag为非0的数据
                groupMemberList.stream().forEach(kk->{
                    weGroupMemberService.physicalDelete(kk.getChatId(),kk.getUserId());
                    iWeFissionService.handleGroupFissionRecord(kk.getState(),kk);
                });

                weGroupMemberService.saveBatch(groupMemberList);


            }
        }
    }

    @Override
    public List<WeCustomerAddGroupVo> findWeGroupByCustomer(String userId, String externalUserid) {
        return this.baseMapper.findWeGroupByCustomer(userId, externalUserid);
    }

    @Override
    public void addMember(String chatId, Integer joinScene, Integer memChangeCnt,List<String> memeberList) {
        WeGroupChatDetailQuery query = new WeGroupChatDetailQuery(chatId, 1);
        WeGroupChatDetailVo groupChatDetail = qwCustomerClient.getGroupChatDetail(query).getData();
        if (groupChatDetail != null && groupChatDetail.getGroupChat() != null) {
            WeGroupChatDetailVo.GroupChatDetail groupChat = groupChatDetail.getGroupChat();
            List<WeGroupMemberEntity> addMemberList = groupChat.getMemberList();
            //需要新增的群成员信息列表
            List<WeGroupMemberEntity> needAddMemberList = addMemberList.stream().filter(item->memeberList.contains(item.getUserId())).collect(Collectors.toList());
            log.info("成员入群 chatId：{},joinScene:{},memChangeCnt:{},needAddMemberList:{}", chatId, joinScene, memChangeCnt, needAddMemberList.size());
            if (CollectionUtil.isNotEmpty(needAddMemberList)) {
                List<WeGroupMember> members = needAddMemberList.stream().map(groupMember -> {
                    WeGroupMember weGroupMember = new WeGroupMember();
                    weGroupMember.setCreateBy(SecurityUtils.getUserName());
                    weGroupMember.setCreateById(SecurityUtils.getUserId());
                    weGroupMember.setCreateTime(new Date());
                    weGroupMember.setUpdateBy(SecurityUtils.getUserName());
                    weGroupMember.setUpdateById(SecurityUtils.getUserId());
                    weGroupMember.setUpdateTime(new Date());
                    weGroupMember.setChatId(chatId);
                    weGroupMember.transformQwParams(groupMember);
                    weGroupMember.setDelFlag(Constants.COMMON_STATE);

                    //新增群成员发送通知任务
                    JSONObject noticeObj = new JSONObject();
                    noticeObj.put("chatId",chatId);
                    noticeObj.put("userId",weGroupMember.getUserId());
                    noticeObj.put("status",weGroupMember.getState());
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getGroupAddUserEx(),rabbitMQSettingConfig.getGroupAddUserCodeRk(),noticeObj.toJSONString());

                    //任务宝处理逻辑
                    iWeFissionService.handleGroupFissionRecord(weGroupMember.getState(),weGroupMember);
                    return weGroupMember;
                }).collect(Collectors.toList());
                //删除原有的
                weGroupMemberService.remove(new LambdaQueryWrapper<WeGroupMember>()
                        .eq(WeGroupMember::getChatId,chatId)
                        .in(WeGroupMember::getUserId,members.stream().map(WeGroupMember::getUserId).collect(Collectors.toList())));
                weGroupMemberService.saveBatch(members);
                weCustomerTrajectoryService.createJoinOrExitGroupTrajectory(members,groupChat.getName(),true);
                if(members.size()==1){
                    //为被添加员工发送一条消息提醒
                    iWeMessagePushService.pushMessageSelfH5( ListUtil.toList(
                                    groupChat.getOwner()
                            )
                            , "【客群动态】<br/><br/> 客户@"+members.stream().findFirst().get().getName()+" 刚刚进入群聊"+groupChat.getName(), MessageNoticeType.CUSTOMERADDCHAT.getType(),false);

                }else if(members.size()>=2){
                    WeGroupMember groupMember = members.stream().max(Comparator.comparing(WeGroupMember::getJoinTime)).get();
                    if(null != groupMember){
                        iWeMessagePushService.pushMessageSelfH5( ListUtil.toList(
                                        groupChat.getOwner()
                                )
                                , "【客群动态】<br/><br/> 客户@"+groupMember.getName()+" 刚刚进入群聊"+groupChat.getName(), MessageNoticeType.CUSTOMERADDCHAT.getType(),false);

                        //添加消息通知
                       weMessageNotificationService.save(MessageTypeEnum.GROUP.getType(),groupChat.getOwner(), MessageConstants.GROUP_ADD,  groupMember.getName(), groupChat.getName());
                    }
                }

            }
        }
    }

    @Override
    public void delMember(String chatId, Integer quitScene, Integer memChangeCnt,List<String> memeberList) {
        log.info("删除群成员 chatId:{}, quitScene:{},memChangeCnt:{}",chatId,quitScene,memChangeCnt);
        WeGroupChatDetailQuery query = new WeGroupChatDetailQuery(chatId, 1);
        WeGroupChatDetailVo groupChatDetail = qwCustomerClient.getGroupChatDetail(query).getData();
        if (groupChatDetail != null && groupChatDetail.getGroupChat() != null) {
            WeGroupChatDetailVo.GroupChatDetail groupChat = groupChatDetail.getGroupChat();
            //需要新增的群成员信息列表
            List<WeGroupMember> needQuitMemberList = weGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>().eq(WeGroupMember::getChatId, chatId)
                    .in(WeGroupMember::getUserId, memeberList));
            log.info("成员退群 chatId：{},joinScene:{},memChangeCnt:{},needQuitMemberList:{}", chatId, quitScene, memChangeCnt, needQuitMemberList.size());
            if (CollectionUtil.isNotEmpty(needQuitMemberList)) {
                needQuitMemberList.forEach(memeber -> {
                    memeber.setQuitScene(quitScene);
                    weGroupMemberService.quitGroup(quitScene,memeber.getUserId(),memeber.getChatId());
                    if(new Integer(0).equals(quitScene)){
                        //为被添加员工发送一条消息提醒
                        iWeMessagePushService.pushMessageSelfH5(ListUtil.toList(groupChat.getOwner()), "【客群动态】<br/><br/> 客户@" + memeber.getName() + " 刚刚退出群聊" + groupChat.getName(), MessageNoticeType.CUSTOMERADDCHAT.getType(), false);
                        //添加消息通知
                        weMessageNotificationService.save(MessageTypeEnum.GROUP.getType(),groupChat.getOwner(),MessageConstants.GROUP_DELETE,memeber.getName(),groupChat.getName());
                    }
                });
                weCustomerTrajectoryService.createJoinOrExitGroupTrajectory(needQuitMemberList, groupChat.getName(), false);
            }
        }
    }

    @Override
    public void changeGroup(String chatId, String updateDetail) {
        WeGroupChatDetailQuery query = new WeGroupChatDetailQuery(chatId, 1);
        WeGroupChatDetailVo groupChatDetail = qwCustomerClient.getGroupChatDetail(query).getData();
        if (groupChatDetail != null && groupChatDetail.getGroupChat() != null) {
            WeGroupChatDetailVo.GroupChatDetail groupChat = groupChatDetail.getGroupChat();
            WeGroup weGroup = getOne(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getChatId, chatId).eq(WeGroup::getDelFlag, 0));
            if (weGroup != null) {
                if (updateDetail.equals(GroupUpdateDetailEnum.CHANGE_OWNER.getType())) {
                    weGroup.setOwner(groupChat.getOwner());
                } else if (updateDetail.equals(GroupUpdateDetailEnum.CHANGE_NAME.getType())) {
                    weGroup.setGroupName(groupChat.getName());
                } else if (updateDetail.equals(GroupUpdateDetailEnum.CHANGE_NOTICE.getType())) {
                    weGroup.setNotice(groupChat.getNotice());
                }
                updateById(weGroup);
            }
        }
    }

    @Override
    public List<WeGroup> findGroupByUserId(String chatUserId, String state) {
        return this.baseMapper.findGroupByUserId(chatUserId,state);
    }

    @Override
    public List<LinkGroupChatListVo> selectChatByMember(WeGroupChatQuery query) {
        return this.baseMapper.selectChatByMember(query);
    }


}
