package com.linkwechat.scheduler.service.impl.sop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.SettingUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.SopType;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.enums.strategiccrowd.CorpAddStateEnum;
import com.linkwechat.common.enums.strategiccrowd.CrowdSwipeTypeEnum;
import com.linkwechat.common.enums.strategiccrowd.CustomerAttributesEnum;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.MapUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeSopChange;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.sop.WeSopBase;
import com.linkwechat.domain.sop.dto.WeSopBaseDto;
import com.linkwechat.domain.sop.vo.WeSopExecuteConditVo;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import com.linkwechat.scheduler.service.AbstractCrowdService;
import com.linkwechat.scheduler.service.SopTaskService;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SopTaskServiceImpl implements SopTaskService {

    @Autowired
    private IWeSopBaseService iWeSopBaseService;

    @Autowired
    private IWeCustomerService iWeCustomerService;






    @Override
    public void createOrUpdateSop(WeSopBaseDto weSopBaseDto) {
        if (null != weSopBaseDto && weSopBaseDto.getLoginUser() != null) {
            LoginUser loginUser = weSopBaseDto.getLoginUser();
            SecurityContextHolder.setCorpId(loginUser.getCorpId());
            SecurityContextHolder.setUserName(loginUser.getUserName());
            SecurityContextHolder.setUserType(loginUser.getUserType());

            boolean isCreateOrUpdate = weSopBaseDto.isCreateOrUpdate();

            WeSopBase weSopBase = iWeSopBaseService.getById(weSopBaseDto.getSopBaseId());

            if (null != weSopBase) {



                    if (weSopBase.getBaseType() == 1) { //客户sop
                        //构建客户sop执行计划
                        iWeSopBaseService.builderExecuteCustomerSopPlan(weSopBase, builderExecuteWeCustomer(weSopBase,false), isCreateOrUpdate, false);
                    }else if (weSopBase.getBaseType() == 2) { //客群sop

                            Set<String> executeWeUserIds
                                    = iWeSopBaseService.builderExecuteWeUserIds(weSopBase.getExecuteWeUser());
                        if (CollectionUtil.isNotEmpty(executeWeUserIds)) {
                            //构建客群sop执行计划
                            iWeSopBaseService.builderExecuteGroupSopPlan(weSopBase
                                    , iWeSopBaseService.builderExecuteGroup(weSopBase,(WeSopExecuteConditVo) weSopBase.getExecuteCustomerOrGroup(), executeWeUserIds), isCreateOrUpdate,false);

                        }

                }

            }

        }
    }

    @Override
    public void builderNewWeCustomer(WeCustomer weCustomer) {
//        List<WeCustomersVo> weCustomerList = iWeCustomerService.findWeCustomerList(WeCustomersQuery.builder()
//                .externalUserid(weCustomer.getExternalUserid())
//                .firstUserId(weCustomer.getAddUserId())
//                .delFlag(Constants.COMMON_STATE)
//                .build(), null);


//        if(CollectionUtil.isNotEmpty(weCustomerList)){
//            WeCustomersVo weCustomersVo = weCustomerList.stream().findFirst().get();

//            if(null != weCustomersVo){
                //获取执行中的新客sop
                List<WeSopBase> weSopBases = iWeSopBaseService.list(new LambdaQueryWrapper<WeSopBase>()
                        .eq(WeSopBase::getBusinessType, SopType.SOP_TYPE_XK.getSopKey())
                        .eq(WeSopBase::getDelFlag, Constants.COMMON_STATE)
                        .eq(WeSopBase::getSopState, 1));
                if(CollectionUtil.isNotEmpty(weSopBases)){
                    weSopBases.stream().forEach(weSopBase -> {


                        WeCustomersQuery weCustomersQuery = weSopBase.getWeCustomersQuery();

                        if(null == weCustomersQuery){
                            weCustomersQuery=new WeCustomersQuery();
                        }

                        weCustomersQuery.setFirstUserId(weCustomer.getAddUserId());
                        weCustomersQuery.setExternalUserid(weCustomer.getExternalUserid());
                        weSopBase.setWeCustomersQuery(weCustomersQuery);
                        Map<String, List<WeCustomersVo>> stringListMap
                                = builderExecuteWeCustomer(weSopBase,true);

                        if(CollectionUtil.isNotEmpty(stringListMap)){
                            //加入新客sop
                            iWeSopBaseService.builderExecuteCustomerSopPlan(weSopBase,
                                    stringListMap
                                    , false, true);

                        }

                    });

                }




    }

    @Override
    public void builderNewGroup(LinkGroupChatListVo linkGroupChatListVo) {


        //获取执行中的新群sop
        List<WeSopBase> weSopBases = iWeSopBaseService.list(new LambdaQueryWrapper<WeSopBase>()
                .eq(WeSopBase::getBusinessType,SopType.SOP_TYPE_XQPY.getSopKey())
                .eq(WeSopBase::getDelFlag, Constants.COMMON_STATE)
                .eq(WeSopBase::getSopState, 1));

        if(CollectionUtil.isNotEmpty(weSopBases)){
            weSopBases.stream().forEach(weSopBase -> {


                //判断该群符不符合当前sop的条件
                Map<String, List<LinkGroupChatListVo>> groupListMap
                        = iWeSopBaseService.builderExecuteGroup(weSopBase, (WeSopExecuteConditVo) weSopBase.getExecuteCustomerOrGroup(),
                        CollUtil.newHashSet(linkGroupChatListVo.getOwner()));


                if(CollectionUtil.isNotEmpty(groupListMap)){
                    List<LinkGroupChatListVo> linkGroupChatListVos
                            = groupListMap.get(linkGroupChatListVo.getOwner());

                    if(CollectionUtil.isNotEmpty(linkGroupChatListVos)){

                        linkGroupChatListVos.stream().forEach(k->{

                            if(k.getChatId().equals(linkGroupChatListVo.getChatId())){
                                //构建新群sop
                                iWeSopBaseService.builderExecuteGroupSopPlan(weSopBase
                                        ,
                                        MapUtil.builder(linkGroupChatListVo.getOwner(),ListUtil.list(false, linkGroupChatListVo)).build()
                                        , true,true);
                            }

                        });

                    }




                }




            });







        }

    }

    @Override
    public void handleChangeSop(WeSopChange weSopChange) {


        WeSopBase weSopBase
                = iWeSopBaseService.getById(weSopChange.getSopBaseId());
        if(weSopBase != null && weSopBase.getBaseType().equals(1)){

            List<WeCustomersVo> weCustomerList = iWeCustomerService.findWeCustomerList(WeCustomersQuery.builder()
                    .firstUserId(weSopChange.getAddUserId())
                    .externalUserid(weSopChange.getExternalUserid())
                    .build(), null);
            if(CollectionUtil.isNotEmpty(weCustomerList)){
                //构建转入sop的计划
                iWeSopBaseService.builderExecuteCustomerSopPlan(weSopBase,
                        weCustomerList.stream().collect(Collectors.groupingBy(WeCustomersVo::getFirstUserId)),
                        false, true);

            }

        }



    }



    //isSourceNewWeCustomer 是否来自新客回调新客 true是 false不是
    private Map<String, List<WeCustomersVo>> builderExecuteWeCustomer(WeSopBase weSopBase,boolean isSourceNewWeCustomer) {

        List<WeCustomersVo> weCustomersVoList=new ArrayList<>();

        log.error("新客SOP"+ JSONUtil.toJsonStr(weSopBase));

        if(null != weSopBase){
            if(!isSourceNewWeCustomer){
                if(weSopBase.getBusinessType().intValue()==SopType.SOP_TYPE_XK.getSopKey()){
                    return new HashMap<>();
                }
            }

            //全部客户
            if(new Integer(0).equals(weSopBase.getScopeType())){
                if(weSopBase.getWeCustomersQuery() == null){
                    weCustomersVoList=iWeCustomerService.findLimitWeCustomerList();
                }else{
                    weCustomersVoList=iWeCustomerService.findLimitWeCustomerList(weSopBase.getWeCustomersQuery());
                }
            //按照条件筛选部分客户
            }else if(new Integer(1).equals(weSopBase.getScopeType())){
                //不等于新客sop，新客sop中的客户来源为新加入的客户
                WeCustomersQuery weCustomersQuery = weSopBase.getWeCustomersQuery();
                if(null != weCustomersQuery){
                    weCustomersQuery.setDelFlag(Constants.COMMON_STATE);
                    weCustomersVoList=iWeCustomerService.findWeCustomerList(weCustomersQuery, null);
                }

            }
        }

        return weCustomersVoList.stream().collect(Collectors.groupingBy(WeCustomersVo::getFirstUserId));


    }





}
