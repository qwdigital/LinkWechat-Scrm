package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.common.utils.uuid.UUID;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeCommunityNewGroupServiceImpl extends ServiceImpl<WeCommunityNewGroupMapper, WeCommunityNewGroup> implements IWeCommunityNewGroupService {




    @Autowired
    private IWeGroupCodeService iWeGroupCodeService;

    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Autowired
    private IWeTagService iWeTagService;


    @Autowired
    private IWeGroupMemberService iWeGroupMemberService;






    @Override
    @Transactional
    public void add(WeCommunityNewGroupQuery weCommunityNewGroupQuery) {

        //获取员工活码
        String emplCodeState=WelcomeMsgTypeEnum.WE_QR_XKLQ_PREFIX.getType() + UUID.get16UUID();
        WeAddWayVo weContactWay =  qwCustomerClient.addContactWay(WeAddWayQuery.builder()
                .type(2)
                .scene(2)
                .state(emplCodeState)
                .user(ListUtil.toList(weCommunityNewGroupQuery.getEmplList().split(",")))
                .skip_verify(weCommunityNewGroupQuery.getSkipVerify())
                .build()).getData();

        if(null != weContactWay){
            if(StringUtils.isNotEmpty(weContactWay.getConfigId())&&StringUtils.isNotEmpty(weContactWay.getQrCode())){

                // 保存新客自动拉群信息
                WeCommunityNewGroup communityNewGroup = new WeCommunityNewGroup();
                BeanUtils.copyBeanProp(communityNewGroup,weCommunityNewGroupQuery);
                communityNewGroup.setEmplCodeUrl(weContactWay.getQrCode());
                communityNewGroup.setEmplCodeConfigId(weContactWay.getConfigId());
                communityNewGroup.setEmplCodeState(emplCodeState);

                //构造群活码相关
                communityNewGroup.setGroupCodeState(
                        WelcomeMsgTypeEnum.WE_QR_XKLQ_PREFIX + UUID.get16UUID()
                );

                //配置进群方式
                WeGroupChatGetJoinWayVo addJoinWayVo = iWeGroupCodeService.builderGroupCodeUrl(
                        WeGroupCode.builder()
                                .autoCreateRoom(weCommunityNewGroupQuery.getAutoCreateRoom())
                                .roomBaseId(weCommunityNewGroupQuery.getRoomBaseId())
                                .roomBaseName(weCommunityNewGroupQuery.getRoomBaseName())
                                .chatIdList(weCommunityNewGroupQuery.getChatIdList())
                                .state(communityNewGroup.getGroupCodeState())
                                .build()
                );

                if(null != addJoinWayVo && null != addJoinWayVo.getJoin_way()
                        && StringUtils.isNotEmpty(addJoinWayVo.getJoin_way().getQr_code())){

                    communityNewGroup.setGroupCodeConfigId(addJoinWayVo.getJoin_way().getConfig_id());

                    communityNewGroup.setGroupCodeUrl(addJoinWayVo.getJoin_way().getQr_code());

                    save(communityNewGroup);

                }else{
                    throw new WeComException(WeErrorCodeEnum.parseEnum(addJoinWayVo.getErrCode().intValue()).getErrorMsg());
                }

            }else{
                throw new WeComException(WeErrorCodeEnum.parseEnum(weContactWay.getErrCode().intValue()).getErrorMsg());
            }
        }





    }

    @Override
    public WeCommunityNewGroup findWeCommunityNewGroupById(String id) {
        WeCommunityNewGroup weCommunityNewGroup = this.getById(id);

        this.getCompleteEmplCodeInfo(weCommunityNewGroup);


        return weCommunityNewGroup;
    }


    @Override
    public List<WeCommunityNewGroup> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup) {
        List<WeCommunityNewGroup> weCommunityNewGroups = this.list(new LambdaQueryWrapper<WeCommunityNewGroup>()
                .like(StringUtils.isNotEmpty(weCommunityNewGroup.getCodeName()), WeCommunityNewGroup::getCodeName, weCommunityNewGroup.getCodeName()));
        if (StringUtils.isNotEmpty(weCommunityNewGroups)) {
            weCommunityNewGroups.forEach(this::getCompleteEmplCodeInfo);
        }
        return weCommunityNewGroups;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWeCommunityNewGroup(WeCommunityNewGroupQuery weCommunityNewGroupQuery) {

        //更新群活码相关
        WeCommunityNewGroup weCommunityNewGroup = this.getById(weCommunityNewGroupQuery.getId());
        if(null == weCommunityNewGroup){
            throw new WeComException("新客拉群信息不存在！");
        }
        weCommunityNewGroup.setCodeName(weCommunityNewGroupQuery.getCodeName());
        weCommunityNewGroup.setEmplList(weCommunityNewGroupQuery.getEmplList());
        weCommunityNewGroup.setTagList(weCommunityNewGroupQuery.getTagList());
        weCommunityNewGroup.setSkipVerify(weCommunityNewGroupQuery.getSkipVerify());
        weCommunityNewGroup.setWelcomeMsg(weCommunityNewGroupQuery.getWelcomeMsg());
        weCommunityNewGroup.setChatIdList(weCommunityNewGroupQuery.getChatIdList());
        weCommunityNewGroup.setAutoCreateRoom(weCommunityNewGroupQuery.getAutoCreateRoom());
        weCommunityNewGroup.setRoomBaseName(weCommunityNewGroupQuery.getRoomBaseName());
        weCommunityNewGroup.setRoomBaseId(weCommunityNewGroupQuery.getRoomBaseId());


        //更新员工活码
        WeResultVo resultVo = qwCustomerClient.updateContactWay(WeAddWayQuery.builder()
                .type(2)
                .scene(2)
                .config_id(weCommunityNewGroupQuery.getEmplCodeConfigId())
                .user(ListUtil.toList(weCommunityNewGroupQuery.getEmplList().split(",")))
                .skip_verify(weCommunityNewGroupQuery.getSkipVerify())
                .build()).getData();

        if(!resultVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
            throw new WeComException(WeErrorCodeEnum.parseEnum(resultVo.getErrCode().intValue()).getErrorMsg());
        }


        //更新群活码
        WeResultVo weResultVo = qwCustomerClient.updateJoinWayForGroupChat(
                WeGroupChatUpdateJoinWayQuery.builder()
                        .config_id(weCommunityNewGroupQuery.getGroupCodeConfigId())
                        .scene(2)
                        .auto_create_room(weCommunityNewGroupQuery.getAutoCreateRoom())
                        .room_base_id(weCommunityNewGroupQuery.getRoomBaseId())
                        .room_base_name(weCommunityNewGroupQuery.getRoomBaseName())
                        .chat_id_list(Arrays.asList(weCommunityNewGroupQuery.getChatIdList().split(",")))
                        .build()
        ).getData();


        if(null != weResultVo && weResultVo.getErrCode()
                .equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())){

            updateById(weCommunityNewGroup);

        }else{
            throw new WeComException(WeErrorCodeEnum.parseEnum(weResultVo.getErrCode().intValue()).getErrorMsg());
        }

    }


    /**
     * 获取完整的新客自动拉群相关信息
     *
     * @param weCommunityNewGroup 新客自动拉群
     */
    private void getCompleteEmplCodeInfo(WeCommunityNewGroup weCommunityNewGroup) {

        if(null != weCommunityNewGroup){

            //设置群名
            List<WeGroup> weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
                    .in(WeGroup::getChatId, weCommunityNewGroup.getChatIdList().split(",")));
            if(CollectionUtil.isNotEmpty(weGroups)){
                weCommunityNewGroup.setGroupNames(
                        String.join(",",weGroups.stream().map(WeGroup::getGroupName).collect(Collectors.toList()))
                );
            }

            //设置标签名
            List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                    .in(WeTag::getTagId, weCommunityNewGroup.getTagList().split(",")));
            if(CollectionUtil.isNotEmpty(weTags)){
                weCommunityNewGroup.setTagNames(
                        String.join(",",weTags.stream().map(WeTag::getName).collect(Collectors.toList()))
                );
            }


            //设置添加客户数
            if(StringUtils.isNotEmpty(weCommunityNewGroup.getEmplCodeState())){
                weCommunityNewGroup.setAddCustomerNumber(
                        iWeCustomerService.count(new LambdaQueryWrapper<WeCustomer>()
                                .eq(WeCustomer::getState,weCommunityNewGroup.getEmplCodeState()))
                );
            }

            //设置进群数
            if(StringUtils.isNotEmpty(weCommunityNewGroup.getGroupCodeState())){
                weCommunityNewGroup.setJoinGroupNumber(
                        iWeGroupMemberService.count(new LambdaQueryWrapper<WeGroupMember>()
                                .eq(WeGroupMember::getState,weCommunityNewGroup.getGroupCodeState()))
                );
            }

        }

    }



}
