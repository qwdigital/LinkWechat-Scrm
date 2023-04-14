package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.linkwechat.common.constant.WeComeStateContants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.uuid.UUID;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupcode.query.WeMakeGroupCodeTagQuery;
import com.linkwechat.domain.groupcode.vo.WeGroupChatInfoVo;
import com.linkwechat.domain.groupcode.vo.WeGroupCodeCountTrendVo;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatAddJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatAddJoinWayVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeGroupCodeMapper;
import com.linkwechat.service.IWeGroupCodeService;
import com.linkwechat.service.IWeGroupCodeTagRelService;
import com.linkwechat.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leejoker
 * @date 2022/4/6 23:25
 */
@Service
public class WeGroupCodeServiceImpl extends ServiceImpl<WeGroupCodeMapper, WeGroupCode> implements IWeGroupCodeService {


    @Autowired
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private IWeGroupCodeTagRelService iWeGroupCodeTagRelService;



    @Override
    public List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode) {

        List<WeGroupCode> weGroupCodes = this.baseMapper.findWeGroupCodeList(weGroupCode.getActivityName(), weGroupCode.getTagIds());
        this.setCountData(weGroupCodes);
        return weGroupCodes;
    }


    @Override
    public WeGroupCode findWeGroupCodeById(Long groupCodeId){

        WeGroupCode weGroupCode = getById(groupCodeId);
        if(null != weGroupCode){
            this.setCountData(ListUtil.toList(weGroupCode));
        }

        return weGroupCode;
    }

    //设置群码统计相关数据
    private void setCountData(List<WeGroupCode> weGroupCodes){
        if(CollectionUtil.isNotEmpty(weGroupCodes)){
            weGroupCodes.stream().forEach(k->{
                List<WeGroupChatInfoVo> weGroupChatInfoVo = this.baseMapper.findWeGroupChatInfoVo(k.getChatIdList(), k.getState());
                if(CollectionUtil.isNotEmpty(weGroupChatInfoVo)){

                    if(StringUtils.isNotEmpty(k.getChatIdList())){
                        k.setChatGroupNum(k.getChatIdList().split(",").length);
                    }

                    k.setChatGroupMemberTotalNum(weGroupChatInfoVo.stream()
                            .collect(Collectors.summingInt(WeGroupChatInfoVo::getChatGroupMemberTotalNum)));

                    k.setOldChatGroupMemberTotalNum(
                            weGroupChatInfoVo.stream()
                                    .collect(Collectors.summingInt(WeGroupChatInfoVo::getOldChatGroupMemberTotalNum))
                    );

                    k.setJoinChatGroupTotalMemberNum(
                            weGroupChatInfoVo.stream()
                                    .collect(Collectors.summingInt(WeGroupChatInfoVo::getJoinChatGroupTotalMemberNum))
                    );


                    k.setOldJoinChatGroupTotalMemberNum(
                            weGroupChatInfoVo.stream()
                                    .collect(Collectors.summingInt(WeGroupChatInfoVo::getOldJoinChatGroupTotalMemberNum))
                    );

                    k.setExitChatGroupTotalMemberNum(
                            weGroupChatInfoVo.stream()
                                    .collect(Collectors.summingInt(WeGroupChatInfoVo::getExitChatGroupTotalMemberNum))
                    );

                    k.setOldExitChatGroupTotalMemberNum(
                            weGroupChatInfoVo.stream()
                                    .collect(Collectors.summingInt(WeGroupChatInfoVo::getOldExitChatGroupTotalMemberNum))
                    );

                    k.setNewJoinChatGroupTotalMemberNum(
                            weGroupChatInfoVo.stream()
                                    .collect(Collectors.summingInt(WeGroupChatInfoVo::getNewJoinChatGroupTotalMemberNum))
                    );
                }
            });
        }
    }

    @Override
    @Transactional
    public void insertWeGroupCode(WeGroupCode weGroupCode) {

        //设置进群方式
        weGroupCode.setState(
                WeComeStateContants.QHM_STATE + UUID.get16UUID()
        );
        //配置进群方式
        WeGroupChatAddJoinWayVo addJoinWayVo = this.builderGroupCodeConfig(weGroupCode);

        if(null != addJoinWayVo && StringUtils.isNotEmpty(addJoinWayVo.getConfig_id())){

            weGroupCode.setConfigId(
                    addJoinWayVo.getConfig_id()
            );
            //获取进群二维码
            WeGroupChatGetJoinWayVo getJoinWayVo = qwCustomerClient.getJoinWayForGroupChat(WeGroupChatJoinWayQuery.builder()
                    .config_id(addJoinWayVo.getConfig_id())
                    .build()).getData();
            if(null != getJoinWayVo && null != getJoinWayVo.getJoin_way()
            && StringUtils.isNotEmpty(getJoinWayVo.getJoin_way().getQr_code())){

                weGroupCode.setCodeUrl(
                        getJoinWayVo.getJoin_way().getQr_code()
                );
                if(save(weGroupCode)){
                    String tagIds = weGroupCode.getTagIds();
                    if(StringUtils.isNotEmpty(tagIds)){
                        iWeGroupCodeTagRelService.makeGroupCodeTag(
                                WeMakeGroupCodeTagQuery.builder()
                                        .groupCodeId(weGroupCode.getId())
                                        .tagIds(Arrays.asList(tagIds.split(",")))
                                        .build()
                        );
                    }

                }
            }
        }else{

            throw new WeComException(WeErrorCodeEnum.parseEnum(addJoinWayVo.getErrCode().intValue()).getErrorMsg());
        }
    }



    @Override
    @Transactional
    public WeGroupCode updateWeGroupCode(WeGroupCode weGroupCode) {
        WeResultVo weResultVo = qwCustomerClient.updateJoinWayForGroupChat(
                WeGroupChatUpdateJoinWayQuery.builder()
                        .config_id(weGroupCode.getConfigId())
                        .scene(2)
                        .auto_create_room(weGroupCode.getAutoCreateRoom())
                        .room_base_id(weGroupCode.getRoomBaseId())
                        .room_base_name(weGroupCode.getRoomBaseName())
                        .chat_id_list(Arrays.asList(weGroupCode.getChatIdList().split(",")))
                        .state(weGroupCode.getState())
                        .build()
        ).getData();
        if(null != weResultVo && weResultVo.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())){
            if(updateById(weGroupCode)){
                String tagIds = weGroupCode.getTagIds();
                if(StringUtils.isNotEmpty(tagIds)){
                    iWeGroupCodeTagRelService.makeGroupCodeTag(
                            WeMakeGroupCodeTagQuery.builder()
                                    .groupCodeId(weGroupCode.getId())
                                    .tagIds(Arrays.asList(tagIds.split(",")))
                                    .build()
                    );
                }
            }
        }else{
            throw new WeComException(WeErrorCodeEnum.parseEnum(weResultVo.getErrCode().intValue()).getErrorMsg());
        }
        return weGroupCode;
    }



    @Override
    @Transactional
    public void batchRemoveByIds(List<Long> ids) {

        ids.stream().forEach(id->{
            WeGroupCode weGroupCode = getById(id);

            if(null != weGroupCode && StringUtils.isNotEmpty(weGroupCode.getConfigId())){
                if(removeById(id)){
                    qwCustomerClient.delJoinWayForGroupChat(
                            WeGroupChatJoinWayQuery.builder()
                                    .config_id(weGroupCode.getConfigId())
                                    .build()
                    );
                }
            }

        });

    }

    @Override
    public List<WeGroupChatInfoVo> findWeGroupChatInfoVos(Long groupId) {
        List<WeGroupChatInfoVo> weGroupChatInfoVos=new ArrayList<>();
        WeGroupCode weGroupCode = getById(groupId);
        if(null != weGroupCode){
            weGroupChatInfoVos=this.baseMapper.findWeGroupChatInfoVo(weGroupCode.getChatIdList(), weGroupCode.getState());
        }
        return weGroupChatInfoVos;
    }

    @Override
    public List<WeGroupCodeCountTrendVo> findWeGroupCodeCountTrend(String state, String beginTime, String endTime){
        List<WeGroupCodeCountTrendVo> weGroupCodeCountTrend
                = this.baseMapper.findWeGroupCodeCountTrend(state, beginTime, endTime);
        return weGroupCodeCountTrend;
    }

    @Override
    public WeGroupChatGetJoinWayVo builderGroupCodeUrl(WeGroupCode weGroupCode) {

        WeGroupChatAddJoinWayVo addJoinWayVo = this.builderGroupCodeConfig(weGroupCode);

        if(null != addJoinWayVo && StringUtils.isNotEmpty(addJoinWayVo.getConfig_id())){

            //获取进群二维码
            return qwCustomerClient.getJoinWayForGroupChat(WeGroupChatJoinWayQuery.builder()
                    .config_id(addJoinWayVo.getConfig_id())
                    .build()).getData();

        }



        return  null;
    }

    @Override
    public WeGroupChatAddJoinWayVo builderGroupCodeConfig(WeGroupCode weGroupCode) {

        //配置进群方式
        WeGroupChatAddJoinWayVo addJoinWayVo = qwCustomerClient.addJoinWayForGroupChat(
                WeGroupChatAddJoinWayQuery.builder()
                        .scene(2)
                        .auto_create_room(weGroupCode.getAutoCreateRoom())
                        .room_base_name(weGroupCode.getRoomBaseName())
                        .room_base_id(weGroupCode.getRoomBaseId())
                        .chat_id_list(Arrays.asList(weGroupCode.getChatIdList().split(",")))
                        .state(weGroupCode.getState())
                        .build()
        ).getData();


        return addJoinWayVo;
    }



}
