package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.constant.WeComeStateContants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.uuid.UUID;
import com.linkwechat.domain.WeBuildUserOrGroupConditVo;
import com.linkwechat.domain.fission.vo.WeExecuteUserOrGroupConditVo;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.storecode.entity.WeStoreCode;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
import com.linkwechat.domain.storecode.query.WeStoreCodeQuery;
import com.linkwechat.domain.storecode.vo.WeStoreCodeTableVo;
import com.linkwechat.domain.storecode.vo.WeStoreCodesVo;
import com.linkwechat.domain.storecode.vo.datareport.WeStoreGroupReportVo;
import com.linkwechat.domain.storecode.vo.datareport.WeStoreShopGuideReportVo;
import com.linkwechat.domain.storecode.vo.drum.WeStoreGroupDrumVo;
import com.linkwechat.domain.storecode.vo.drum.WeStoreShopGuideDrumVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreGroupTabVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreShopGuideTabVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreTabVo;
import com.linkwechat.domain.storecode.vo.trend.WeStoreGroupTrendVo;
import com.linkwechat.domain.storecode.vo.trend.WeStoreShopGuideTrendVo;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeStoreCodeCountMapper;
import com.linkwechat.mapper.WeStoreCodeMapper;
import com.linkwechat.service.IWeGroupCodeService;
import com.linkwechat.service.IWeStoreCodeConfigService;
import com.linkwechat.service.IWeStoreCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;



@Service
public class WeStoreCodeServiceImpl extends ServiceImpl<WeStoreCodeMapper, WeStoreCode>
        implements IWeStoreCodeService {



    @Autowired
    private WeStoreCodeCountMapper weStoreCodeCountMapper;


    @Autowired
    private IWeStoreCodeConfigService iWeStoreCodeConfigService;


    @Autowired
    private IWeGroupCodeService iWeGroupCodeService;



    @Autowired
    private QwCustomerClient qwCustomerClient;





    @Override
    @DataScope(type = "2", value = @DataColumn(alias = "we_store_code", name = "create_by_id", userid = "user_id"))
    public List<WeStoreCode> storeCodes(WeStoreCode weStoreCode) {
        LambdaQueryWrapper<WeStoreCode> queryWrapper = new LambdaQueryWrapper<WeStoreCode>()
                .like(StringUtils.isNotEmpty(weStoreCode.getStoreName()), WeStoreCode::getStoreName, weStoreCode.getStoreName())
                .eq(StringUtils.isNotEmpty(weStoreCode.getArea()), WeStoreCode::getArea, weStoreCode.getArea())
                .eq(weStoreCode.getStoreState() != null, WeStoreCode::getStoreState, weStoreCode.getStoreState())
                .orderByDesc(WeStoreCode::getCreateTime);

        if(StringUtils.isNotEmpty(weStoreCode.getParams().get("dataScope").toString())){
            queryWrapper.apply(""+weStoreCode.getParams().get("dataScope").toString()+"");
        }


        return list(queryWrapper);
    }

    @Override
    public void createOrUpdateStoreCode(WeStoreCode weStoreCode) {

        if(weStoreCode.getId() == null){ //新增
            weStoreCode.setId(SnowFlakeUtil.nextId());

            WeBuildUserOrGroupConditVo addWeUserOrGroupCode =
                    weStoreCode.getAddWeUserOrGroupCode();

            if(null != addWeUserOrGroupCode){
                WeGroupCode addGroupCode = addWeUserOrGroupCode.getAddGroupCode();

                //创建群活码
                if(null != addGroupCode){
                    weStoreCode.setGroupCodeState(WeComeStateContants.MDQM_STATE +weStoreCode.getId());
                    //配置进群方式
                    WeGroupChatGetJoinWayVo addJoinWayVo = iWeGroupCodeService.builderGroupCodeUrl(
                            WeGroupCode.builder()
                                    .autoCreateRoom(addGroupCode.getAutoCreateRoom())
                                    .roomBaseId(addGroupCode.getRoomBaseId())
                                    .roomBaseName(addGroupCode.getRoomBaseName())
                                    .chatIdList(addGroupCode.getChatIdList())
                                    .state(weStoreCode.getGroupCodeState())
                                    .build()
                    );

                    if(null != addJoinWayVo&&addJoinWayVo.getJoin_way() != null){
                        WeGroupChatGetJoinWayVo.JoinWay joinWay = addJoinWayVo.getJoin_way();
                        weStoreCode.setGroupCodeConfigId(joinWay.getConfig_id());
                        weStoreCode.setGroupCodeUrl(joinWay.getQr_code());
                    }else{
                        throw new WeComException(addJoinWayVo.getErrMsg());
                    }


                }



                WeQrAddQuery weQrAddQuery = addWeUserOrGroupCode.getWeQrAddQuery();

                //创建员工活码
                if(null != weQrAddQuery){
                    weStoreCode.setShopGuideState(WeComeStateContants.MDDG_STATE + weStoreCode.getId());
                    weQrAddQuery.setQrType(2);
                    WeAddWayQuery weContactWayByState = weQrAddQuery.getWeContactWayByState(weStoreCode.getShopGuideState());

                    WeAddWayVo weAddWayResult = qwCustomerClient.addContactWay(weContactWayByState).getData();


                    if (weAddWayResult != null && ObjectUtil.equal(0, weAddWayResult.getErrCode())) {
                        weStoreCode.setShopGuideUrl(weAddWayResult.getQrCode());
                        weStoreCode.setShopGuideConfigId(weAddWayResult.getConfigId());
                    }else{
                        throw new WeComException(weAddWayResult.getErrMsg());
                    }

                }

            }





        }else{

            WeBuildUserOrGroupConditVo addWeUserOrGroupCode =
                    weStoreCode.getAddWeUserOrGroupCode();


            //更新群活码
            if(null != addWeUserOrGroupCode) {
                WeGroupCode addGroupCode = addWeUserOrGroupCode.getAddGroupCode();
                if (null != addGroupCode) {

                    //更新群活码
                    WeResultVo weResultVo = qwCustomerClient.updateJoinWayForGroupChat(
                            WeGroupChatUpdateJoinWayQuery.builder()
                                    .config_id(weStoreCode.getGroupCodeConfigId())
                                    .scene(2)
                                    .auto_create_room(addGroupCode.getAutoCreateRoom())
                                    .room_base_id(addGroupCode.getRoomBaseId())
                                    .room_base_name(addGroupCode.getRoomBaseName())
                                    .chat_id_list(Arrays.asList(addGroupCode.getChatIdList().split(",")))
                                    .build()
                    ).getData();

                    if(!weResultVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){

                        throw new WeComException(weResultVo.getErrMsg());
                    }




                }
            }

            WeQrAddQuery weQrAddQuery = addWeUserOrGroupCode.getWeQrAddQuery();

            if(null != weQrAddQuery){
                WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();
                WeResultVo weResultVo = qwCustomerClient.updateContactWay(weContactWay).getData();
                if(!weResultVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                    throw new WeComException(weResultVo.getErrMsg());
                }
            }




        }

       saveOrUpdate(weStoreCode);
    }



    @Override
    public WeStoreShopGuideTabVo countWeStoreShopGuideTab() {
        List<WeStoreCodeConfig> weStoreCodeConfigs = iWeStoreCodeConfigService.list(new LambdaQueryWrapper<WeStoreCodeConfig>()
                .eq(WeStoreCodeConfig::getStoreCodeType, 1));

        if(CollectionUtil.isNotEmpty(weStoreCodeConfigs)){
            String state = weStoreCodeConfigs.stream().findFirst().get().getState();
            if(StringUtils.isNotEmpty(state)){
                return weStoreCodeCountMapper.countWeStoreShopGuideTab(state);
            }

        }

        return new WeStoreShopGuideTabVo();
    }

    @Override
    public WeStoreTabVo countWeStoreTab(Long storeCodeId) {
        return weStoreCodeCountMapper.countWeStoreTab(storeCodeId,getById(storeCodeId).getId());
    }

    @Override
    public WeStoreGroupTabVo countWeStoreGroupTab() {
        return weStoreCodeCountMapper.countWeStoreGroupTab();
    }

    @Override
    public List<WeStoreShopGuideTrendVo> countStoreShopGuideTrend(WeStoreCode weStoreCode) {
        return weStoreCodeCountMapper.countStoreShopGuideTrend(weStoreCode);
    }

    @Override
    public List<WeStoreGroupTrendVo> countStoreGroupTrend(WeStoreCode weStoreCode) {

        return weStoreCodeCountMapper.countStoreGroupTrend(weStoreCode);
    }

    @Override
    public List<WeStoreShopGuideDrumVo> countStoreShopGuideDrum(String beginTime, String endTime) {
        return weStoreCodeCountMapper.countStoreShopGuideDrum(beginTime,endTime);
    }

    @Override
    public List<WeStoreGroupDrumVo> countStoreShopGroupDrum(String beginTime, String endTime) {
        return weStoreCodeCountMapper.countStoreShopGroupDrum(beginTime, endTime);
    }

    @Override
    public List<WeStoreShopGuideReportVo> countShopGuideReport(WeStoreCode weStoreCode) {
        return weStoreCodeCountMapper.countShopGuideReport(weStoreCode);
    }

    @Override
    public List<WeStoreGroupReportVo> countStoreGroupReport(WeStoreCode weStoreCode) {
        return weStoreCodeCountMapper.countStoreGroupReport(weStoreCode);
    }

    @Override
    public  WeStoreCodesVo findStoreCode(Integer storeCodeType, String unionid, String longitude, String latitude,String area) {
        WeStoreCodesVo weStoreCodesVo=new WeStoreCodesVo();

        if(StringUtils.isNotEmpty(longitude) && StringUtils.isNotEmpty(latitude)){
            WeStoreCodeConfig weStoreCodeConfig = iWeStoreCodeConfigService.getWeStoreCodeConfig(storeCodeType);
            if(null != weStoreCodeConfig){
                weStoreCodesVo.setWelcomeMsg(weStoreCodeConfig.getWelcomeMsg());

                weStoreCodesVo.setWeStoreCodes(
                        this.baseMapper.findStoreCode(longitude, latitude, area, weStoreCodeConfig.getRaidus())
                );

            }
        }


        if(StringUtils.isNotEmpty(area)){
            weStoreCodesVo.setWeStoreCodes(
                    this.baseMapper.findStoreCode(longitude, latitude,area,null)
            );
        }
        return weStoreCodesVo;
    }

    @Override
    public void countUserBehavior(WeStoreCodeCount weStoreCodeCount) {
        weStoreCodeCountMapper.insert(weStoreCodeCount);
    }

        @Override
        public List<WeStoreCodeTableVo> findWeStoreCodeTables(WeStoreCodeQuery weStoreCodeQuery) {
            WeStoreCode weStoreCode = this.getById(weStoreCodeQuery.getStoreCodeId());
            if(null != weStoreCode){
                weStoreCodeQuery.setGroupCodeState(weStoreCode.getGroupCodeState());
                weStoreCodeQuery.setShopGuideState(weStoreCode.getShopGuideState());
            }

            return weStoreCodeCountMapper.findWeStoreCodeTables(weStoreCodeQuery);
        }


}
