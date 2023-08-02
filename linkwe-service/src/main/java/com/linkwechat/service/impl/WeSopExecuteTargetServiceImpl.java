package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.enums.SopExecuteStatus;
import com.linkwechat.common.enums.SopType;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.WeSopChange;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.groupchat.query.WeGroupChatQuery;
import com.linkwechat.domain.groupchat.query.WeMakeGroupTagQuery;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.sop.WeSopAttachments;
import com.linkwechat.domain.sop.WeSopBase;
import com.linkwechat.domain.sop.WeSopExecuteTarget;
import com.linkwechat.domain.sop.WeSopExecuteTargetAttachments;
import com.linkwechat.domain.sop.vo.WeSopExecuteConditVo;
import com.linkwechat.domain.sop.vo.WeSopExecuteEndVo;
import com.linkwechat.mapper.*;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static io.netty.util.internal.SystemPropertyUtil.contains;
import static java.util.stream.Collectors.toList;

/**
* @author robin
* @description 针对表【we_sop_execute_target(目标执行对象表)】的数据库操作Service实现
* @createDate 2022-09-13 16:26:00
*/
@Service
public class WeSopExecuteTargetServiceImpl extends ServiceImpl<WeSopExecuteTargetMapper, WeSopExecuteTarget>
implements IWeSopExecuteTargetService {


    @Autowired
    @Lazy
    private IWeSopBaseService iWeSopBaseService;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Autowired
    private IWeCustomerService iWeCustomerService;

    @Autowired
    private IWeGroupCodeService iWeGroupCodeService;


    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;


    @Autowired
    private IWeSopExecuteTargetAttachmentsService iWeSopExecuteTargetAttachmentsService;


    @Autowired
    private IWeGroupTagRelService weGroupTagRelService;

    @Autowired
    private IWeSopChangeService iWeSopChangeService;






    @Override
    public void sopExceptionEnd(List<String> executeWeUserIds, List<String> executeWeCustomerIdsOrGroupIds) {

        this.update(WeSopExecuteTarget.builder()
                        .executeState(SopExecuteStatus.SOP_STATUS_EXCEPTION.getType())
                .build(), new LambdaQueryWrapper<WeSopExecuteTarget>()
                .in(CollectionUtil.isNotEmpty(executeWeUserIds),WeSopExecuteTarget::getExecuteWeUserId,executeWeUserIds)
                .in(CollectionUtil.isNotEmpty(executeWeCustomerIdsOrGroupIds),WeSopExecuteTarget::getTargetId,executeWeCustomerIdsOrGroupIds)
                .eq(WeSopExecuteTarget::getExecuteState,SopExecuteStatus.SOP_STATUS_ING.getType()));

    }

    @Override
    public void editSopExceptionEnd(Long sopBaseId, List<String> executeWeCustomerIdsOrGroupIds) {

        //not in执行中的设置为异常结束，删除进行中的
        //上次执行中的客户，但修改后不满足条件，全部设置为异常结束
        this.update(WeSopExecuteTarget.builder()
                .executeState(SopExecuteStatus.SOP_STATUS_EXCEPTION.getType())
                        .executeEndTime(new Date())
                .build(),new LambdaQueryWrapper<WeSopExecuteTarget>()
                .notIn(CollectionUtil.isNotEmpty(executeWeCustomerIdsOrGroupIds),WeSopExecuteTarget::getTargetId,executeWeCustomerIdsOrGroupIds)
                .eq(WeSopExecuteTarget::getSopBaseId,sopBaseId)
                .eq(WeSopExecuteTarget::getExecuteState,SopExecuteStatus.SOP_STATUS_ING.getType()));


        //查询当前sop下数据库中已经存在执行中的任务
        List<WeSopExecuteTarget> weSopExecuteTargets = this.list(new LambdaQueryWrapper<WeSopExecuteTarget>()
                .eq(WeSopExecuteTarget::getSopBaseId, sopBaseId)
                .in(CollectionUtil.isNotEmpty(executeWeCustomerIdsOrGroupIds), WeSopExecuteTarget::getTargetId, executeWeCustomerIdsOrGroupIds)
                .eq(WeSopExecuteTarget::getExecuteState, SopExecuteStatus.SOP_STATUS_ING.getType()));

        if(CollectionUtil.isNotEmpty(weSopExecuteTargets)){
            this.removeByIds(
                    weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toList())
            );

            //删除对应的附件
            iWeSopExecuteTargetAttachmentsService.remove(new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                                        .in(WeSopExecuteTargetAttachments::getExecuteTargetId,weSopExecuteTargets.stream().map(WeSopExecuteTarget::getId).collect(Collectors.toList()))
                                        .eq(WeSopExecuteTargetAttachments::getExecuteState,0));
        }


        this.remove(new LambdaQueryWrapper<WeSopExecuteTarget>()
                .eq(WeSopExecuteTarget::getSopBaseId,sopBaseId)
                .in(CollectionUtil.isNotEmpty(executeWeCustomerIdsOrGroupIds),WeSopExecuteTarget::getTargetId,executeWeCustomerIdsOrGroupIds)
                .eq(WeSopExecuteTarget::getExecuteState,SopExecuteStatus.SOP_STATUS_ING.getType()));

    }

    @Override
    public void earlyEndConditionsSop(){

        List<WeSopBase> weSopBases = iWeSopBaseService.list(
                new LambdaQueryWrapper<WeSopBase>()
                        .eq(WeSopBase::getSopState, 1)
                        .eq(WeSopBase::getDelFlag, Constants.COMMON_STATE)
                        .ne(WeSopBase::getEarlyEnd,0)
        );

        if(CollectionUtil.isNotEmpty(weSopBases)){
            weSopBases.stream().forEach(weSopBase -> {
                if(weSopBase.getBaseType().equals(1)){//客户sop,提前结束处理

                    WeSopExecuteEndVo endContent = (WeSopExecuteEndVo)weSopBase.getEndContent();
                    if(null != endContent){
                        //满足标签的客户
                        List<String> tagCustomerIds=new ArrayList<>();
                        WeSopExecuteEndVo.ExecuteTag executeTag = endContent.getExecuteTag();
                        if(null != executeTag && CollectionUtil.isNotEmpty(executeTag.getTagIds())){
                            List<String> weCustomerListIdsByApp
                                    = ((WeCustomerMapper) iWeCustomerService.getBaseMapper()).findWeCustomerListEuIds(WeCustomersQuery.builder()
                                    .tagIds(StringUtils.join(executeTag.getTagIds(), ","))
                                    .build());
                            if(CollectionUtil.isNotEmpty(weCustomerListIdsByApp)){
                                tagCustomerIds.addAll(weCustomerListIdsByApp);
                            }
                        }

                        //满足群条件的客户
                        List<String> goupCustomerIds=new ArrayList<>();
                        WeSopExecuteEndVo.JoinCustomerGroup
                                joinCustomerGroup = endContent.getJoinCustomerGroup();

                        if(null != joinCustomerGroup &&  StringUtils.isNotEmpty(joinCustomerGroup.getGroupCodeId())){

                            WeGroupCode weGroupCode = iWeGroupCodeService.getById(joinCustomerGroup.getGroupCodeId());
                            if(null != weGroupCode && StringUtils.isNotEmpty(weGroupCode.getChatIdList())){
                                List<WeGroupMember> weGroupMembers = iWeGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>()
                                        .in(WeGroupMember::getChatId, ListUtil.toList((weGroupCode.getChatIdList().split(","))))
                                );
                                if(CollectionUtil.isNotEmpty(weGroupMembers)){
                                    goupCustomerIds.addAll(
                                            weGroupMembers.stream().map(WeGroupMember::getUserId).collect(toList())
                                    );
                                }


                            }

                        }

                        //满足转接sop
                        List<String> sopCustomerIds=new ArrayList<>();
                        WeSopExecuteEndVo.ToChangeIntoOtherSop toChangeIntoOtherSop
                                = endContent.getToChangeIntoOtherSop();
                        if(null != toChangeIntoOtherSop && StringUtils.isNotEmpty(toChangeIntoOtherSop.getToChangeIntoSopId())){
                            List<WeSopExecuteTarget> executeTargets = this.list(new LambdaQueryWrapper<WeSopExecuteTarget>()
                                    .eq(WeSopExecuteTarget::getSopBaseId, toChangeIntoOtherSop.getToChangeIntoSopId()));
                            if(CollectionUtil.isNotEmpty(executeTargets)){
                                List<String> executeTargetIds=executeTargets.stream().map(WeSopExecuteTarget::getTargetId).collect(toList());
                                if(CollectionUtil.isNotEmpty(executeTargetIds)){
                                    sopCustomerIds.addAll(executeTargetIds);
                                }
                            }

                        }

                        //提前结束的客户
                        List<String> earlyEndCustomerIds=new ArrayList<>();

                        if(weSopBase.getEarlyEnd()==1){//任意条件

                            earlyEndCustomerIds.addAll(tagCustomerIds);
                            earlyEndCustomerIds.addAll(goupCustomerIds);
                            earlyEndCustomerIds.addAll(sopCustomerIds);

                        }else if(weSopBase.getEarlyEnd()==2){//全部条件

                            if(CollectionUtil.isNotEmpty(tagCustomerIds)&&CollectionUtil.isNotEmpty(goupCustomerIds)
                                    &&CollectionUtil.isNotEmpty(sopCustomerIds)){
                                 tagCustomerIds.retainAll(goupCustomerIds);
                                 tagCustomerIds.retainAll(sopCustomerIds);
                            }


                        }


                        if(CollectionUtil.isNotEmpty(earlyEndCustomerIds)){//提前结束的客户

                            //提前结束当前目标下的sop
                            this.update(WeSopExecuteTarget.builder()
                                    .executeState(SopExecuteStatus.SOP_STATUS_ADVANCE.getType())
                                    .build(), new LambdaQueryWrapper<WeSopExecuteTarget>()
                                    .eq(WeSopExecuteTarget::getExecuteState,SopExecuteStatus.SOP_STATUS_ING.getType())
                                    .eq(WeSopExecuteTarget::getSopBaseId,weSopBase.getId())
                                    .in(WeSopExecuteTarget::getTargetId,earlyEndCustomerIds)
                            );







                        }





                    }



                }else{//客群sop,提前结束处理
                    WeSopExecuteEndVo endContent = weSopBase.getEndContent();
                    if(null != endContent){
                        WeSopExecuteEndVo.ExecuteTag executeTag = endContent.getExecuteTag();
                        if(null != executeTag && CollectionUtil.isNotEmpty(executeTag.getTagIds())){
                            //筛选当前标签下对应的群
                            List<LinkGroupChatListVo> chatListVos = iWeGroupService.selectWeGroupListByApp(WeGroupChatQuery.builder()
                                    .tagIds(StringUtils.join(executeTag.getTagIds(), ","))
                                    .build());
                            if(CollectionUtil.isNotEmpty(chatListVos)){
                                //提前结束当前目标下的sop
                                this.update(WeSopExecuteTarget.builder()
                                                .executeState(SopExecuteStatus.SOP_STATUS_ADVANCE.getType())
                                        .build(), new LambdaQueryWrapper<WeSopExecuteTarget>()
                                        .eq(WeSopExecuteTarget::getExecuteState,SopExecuteStatus.SOP_STATUS_ING.getType())
                                        .eq(WeSopExecuteTarget::getSopBaseId,weSopBase.getId())
                                        .in(WeSopExecuteTarget::getTargetId,chatListVos.stream().map(LinkGroupChatListVo::getChatId).collect(toList()))
                                );
                            }
                        }
                    }
                }
            });


        }



    }


    @Override
    @Transactional
    public void sopExecuteEndAction(Long executeTargetId) {

        WeSopExecuteTarget executeTarget = this.getById(executeTargetId);
        if(null != executeTarget){

            //更新sop执行表状态
            executeTarget.setExecuteState(SopExecuteStatus.SOP_STATUS_COMMON.getType());
            executeTarget.setExecuteEndTime(new Date());
            //执行相关动作
            WeSopBase weSopBase
                    = iWeSopBaseService.getById(executeTarget.getSopBaseId());
            if(null != weSopBase){
                if(weSopBase.getBaseType()==1){//客户sop
                    WeSopExecuteEndVo endContent = weSopBase.getEndContent();
                    if(null != endContent) {//非提前结束sop
                        //标签行为
                        WeSopExecuteEndVo.ExecuteTag executeTag = endContent.getExecuteTag();
                        if(null != executeTag && CollectionUtil.isNotEmpty(executeTag.getTagIds())){
                            List<WeTag> weTags=new ArrayList<>();

                            executeTag.getTagIds().stream().forEach(tagId->{
                                weTags.add(
                                        WeTag.builder()
                                                .tagId(tagId)
                                                .build()
                                );
                            });

                            iWeCustomerService.makeLabel(WeMakeCustomerTag.builder()
                                    .externalUserid(executeTarget.getTargetId())
                                    .userId(executeTarget.getExecuteWeUserId())
                                    .isCompanyTag(true)
                                    .addTag(weTags)
                                    .build());
                        }



                        //转入其他sop
                        WeSopExecuteEndVo.ToChangeIntoOtherSop toChangeIntoOtherSop = endContent.getToChangeIntoOtherSop();
                        if(null != toChangeIntoOtherSop && StringUtils.isNotEmpty(toChangeIntoOtherSop.getToChangeIntoSopId())){


                            //入库至其他sop表中
                            iWeSopChangeService.save(
                                    WeSopChange.builder()
                                            .addUserId(executeTarget.getExecuteWeUserId())
                                            .sopBaseId(Long.parseLong(toChangeIntoOtherSop.getToChangeIntoSopId()))
                                            .externalUserid(executeTarget.getTargetId())
                                            .build()
                            );







//
//                            WeSopBase toChangeIntoSop = iWeSopBaseService.getById(toChangeIntoOtherSop.getToChangeIntoSopId());
//
//
//                            if(StringUtils.isNotEmpty(toChangeIntoSop.getExecuteWeUserIds())){
//                                //当前执行人是否符合转接sop的条件
//                                String executeUserId=null;
//                                //当前成员在转入的sop执行计划中不存在
//                                if(this.count(new LambdaQueryWrapper<WeSopExecuteTarget>()
//                                        .eq(WeSopExecuteTarget::getTargetId,executeTarget.getTargetId())
//                                        .eq(WeSopExecuteTarget::getSopBaseId,toChangeIntoSop.getId()))<=0){
//                                    //执行成员在转入的sop执行成员列表中
//                                    List<String> weSopExecuteTargetsIds = Arrays.asList(toChangeIntoSop.getExecuteWeUserIds().split(","));
//
//                                    if(weSopExecuteTargetsIds.contains(executeTarget.getExecuteWeUserId())){
//                                        executeUserId=executeTarget.getExecuteWeUserId();
//                                    }else{
//                                      //转入的sop执行成员池中选择一个当前执行对象的添加人
//                                        List<WeCustomer> weCustomers = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
//                                                .eq(WeCustomer::getExternalUserid, executeTarget.getTargetId())
//                                                .ne(WeCustomer::getTrackState, TrackState.STATE_YLS.getType())
//                                                .eq(WeCustomer::getDelFlag, Constants.COMMON_STATE));
//
//
//                                        if(CollectionUtil.isNotEmpty(weCustomers)){
//                                            //执行客户添加人id列表
//                                            List<String> addUserIds = weCustomers.stream().map(WeCustomer::getAddUserId).collect(toList());
//                                            //符合转入的sop执行成员列表的添加员工
//                                            List<String> executeUserIds
//                                                    = weSopExecuteTargetsIds.stream().filter(item -> addUserIds.contains(item)).collect(toList());
//                                            if(CollectionUtil.isNotEmpty(executeUserIds)){
//                                                executeUserId=executeUserIds.stream().findFirst().get();//筛选出一个符合条件的员工作为执行人员
//                                            }else{
//                                                executeUserId=weSopExecuteTargetsIds.stream().findFirst().get();//随机筛选一个非好友员工作为执行人员
//                                            }
//
//
//                                            if(StringUtils.isNotEmpty(executeUserId)){
//                                                //构建指定人的执行计划
//                                                iWeSopBaseService.builderExecuteCustomerSopPlan(toChangeIntoSop,
//                                                        MapUtil.builder(executeUserId,
//                                                                ListUtil.list(false, iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
//                                                                        .eq(WeCustomer::getAddUserId, executeUserId)
//                                                                        .eq(WeCustomer::getExternalUserid, executeTarget.getTargetId())))).build()
//                                                        ,true,false);
//                                            }
//
//
//
//                                        }
//
//
//
//
//                                    }
//
//                                }
//
//
//                            }


                        }


                    }




                }else if(weSopBase.getBaseType()==2){ //客群sop(对应的客群打标签)

                    WeSopExecuteEndVo endContent = weSopBase.getEndContent();
                    if(null != endContent && weSopBase.getEarlyEnd()==0){//非提前结束sop
                        WeSopExecuteEndVo.ExecuteTag executeTag = endContent.getExecuteTag();
                        if(null != executeTag && StringUtils.isNotEmpty(executeTag.getTagIds())){
                            weGroupTagRelService.makeGroupTag(
                                    WeMakeGroupTagQuery.builder()
                                            .chatId(executeTarget.getTargetId())
                                            .tagIds(executeTag.getTagIds())
                                            .build()
                            );
                        }

                    }
                }

            }


            this.updateById(
                    executeTarget
            );


        }








    }

    @Override
    public void builderCycleExecutionPlan() {
        List<WeSopBase> weSopBases = iWeSopBaseService.list(new LambdaQueryWrapper<WeSopBase>()
                .eq(WeSopBase::getSopState, 1)
                .eq(WeSopBase::getBusinessType, SopType.SOP_TYPE_ZQYX.getSopKey())
                .eq(WeSopBase::getDelFlag, Constants.COMMON_STATE));

        if(CollectionUtil.isNotEmpty(weSopBases)){
            weSopBases.stream().forEach(weSopBase -> {
                Set<String> executeWeUserIds
                        = iWeSopBaseService.builderExecuteWeUserIds(weSopBase.getExecuteWeUser());
                //构建客群sop执行计划
                iWeSopBaseService.builderExecuteGroupSopPlan(weSopBase
                        ,iWeSopBaseService.builderExecuteGroup(weSopBase,(WeSopExecuteConditVo) weSopBase.getExecuteCustomerOrGroup(), executeWeUserIds),true,false);

            });
        }
    }

}
