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
import com.linkwechat.domain.qr.query.WeQrUserInfoQuery;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.domain.storecode.entity.WeStoreCode;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
import com.linkwechat.domain.storecode.query.WeStoreCodeQuery;
import com.linkwechat.domain.storecode.query.WxStoreCodeQuery;
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
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeContactWayQuery;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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



        }else{

            WeStoreCode oldWeStoreCode = this.getById(weStoreCode.getId());
            if(oldWeStoreCode != null){

                //员工活码操作
                String shopGuideConfigId = oldWeStoreCode.getShopGuideConfigId();
                if(StringUtils.isNotEmpty(shopGuideConfigId)){
                    qwCustomerClient.delContactWay(WeContactWayQuery.builder().config_id(shopGuideConfigId).build());
                    weStoreCode.setShopGuideUrl(null);
                    weStoreCode.setShopGuideState(null);
                    weStoreCode.setShopGuideConfigId(null);
                }

                //群活码
                String groupCodeConfigId = oldWeStoreCode.getGroupCodeConfigId();
                if(StringUtils.isNotEmpty(groupCodeConfigId)){
                    qwCustomerClient.delJoinWayForGroupChat(WeGroupChatJoinWayQuery.builder().config_id(groupCodeConfigId).build());
                    weStoreCode.setGroupCodeUrl(null);
                    weStoreCode.setGroupCodeState(null);
                    weStoreCode.setGroupCodeConfigId(null);
                }
            }
        }

        WeBuildUserOrGroupConditVo addWeUserOrGroupCode =
                weStoreCode.getAddWeUserOrGroupCode();

        this.builderStoreCode(addWeUserOrGroupCode,weStoreCode);

       saveOrUpdate(weStoreCode);
    }



    private void builderStoreCode( WeBuildUserOrGroupConditVo addWeUserOrGroupCode,WeStoreCode weStoreCode){

        if(null != addWeUserOrGroupCode){
            WeGroupCode addGroupCode = addWeUserOrGroupCode.getAddGroupCode();

            //创建群活码
            if(null != addGroupCode){
                if(StringUtils.isNotEmpty(addGroupCode.getChatIdList())){
                    weStoreCode.setGroupCodeState(WeComeStateContants.MDQM_STATE +weStoreCode.getId());
                    addGroupCode.setGroupNames(weStoreCode.getGroupCodeName());
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


            }



            WeQrAddQuery weQrAddQuery = addWeUserOrGroupCode.getWeQrAddQuery();

            //创建员工活码
            if(null != weQrAddQuery){
                List<WeQrUserInfoQuery> qrUserInfos = weQrAddQuery.getQrUserInfos();
                if(CollectionUtil.isNotEmpty(qrUserInfos)){
                    weQrAddQuery.setQrType(2);
                    WeAddWayQuery weContactWayByState = weQrAddQuery.getWeContactWayByState(
                            WeComeStateContants.MDDG_STATE + weStoreCode.getId());
                    List<String> user = weContactWayByState.getUser();
                    if(CollectionUtil.isNotEmpty(user)){

                        // 使用Stream API过滤掉null字符串
                        List<String> filteredUserIds = user.stream()
                                .filter(str -> str != null)
                                .collect(Collectors.toList());
                        if(CollectionUtil.isNotEmpty(filteredUserIds)){
                            weStoreCode.setShopGuideState(WeComeStateContants.MDDG_STATE + weStoreCode.getId());

                            WeAddWayVo weAddWayResult = qwCustomerClient.addContactWay(weContactWayByState).getData();
                            if (weAddWayResult != null && ObjectUtil.equal(0, weAddWayResult.getErrCode())) {
                                weStoreCode.setShopGuideUrl(weAddWayResult.getQrCode());
                                weStoreCode.setShopGuideConfigId(weAddWayResult.getConfigId());
                            }else{
                                throw new WeComException(weAddWayResult.getErrMsg());
                            }
                        }


                    }


                }
            }

        }

    }
    @Override
    public WeStoreShopGuideTabVo countWeStoreShopGuideTab() {
        return weStoreCodeCountMapper.countWeStoreShopGuideTab(WeComeStateContants.MDDG_STATE);
    }

    @Override
    public WeStoreTabVo countWeStoreTab(Long storeCodeId) {
        return weStoreCodeCountMapper.countWeStoreTab(storeCodeId);
    }

    @Override
    public WeStoreGroupTabVo countWeStoreGroupTab() {
        return weStoreCodeCountMapper.countWeStoreGroupTab(WeComeStateContants.MDQM_STATE);
    }

    @Override
    public List<WeStoreShopGuideTrendVo> countStoreShopGuideTrend(WeStoreCode weStoreCode) {
        if(StringUtils.isEmpty(weStoreCode.getArea())&&weStoreCode.getStoreCodeId()==null){
            weStoreCode.setShopGuideState(WeComeStateContants.MDDG_STATE);
        }
        return weStoreCodeCountMapper.countStoreShopGuideTrend(weStoreCode);
    }

    @Override
    public List<WeStoreGroupTrendVo> countStoreGroupTrend(WeStoreCode weStoreCode) {
        if(weStoreCode.getStoreCodeId()==null && StringUtils.isEmpty(weStoreCode.getArea())){
            weStoreCode.setGroupCodeState(WeComeStateContants.MDQM_STATE);
        }
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
    public  WeStoreCodesVo findStoreCode(WxStoreCodeQuery wxStoreCodeQuery) {
        WeStoreCodesVo weStoreCodesVo=new WeStoreCodesVo();

        if(StringUtils.isNotEmpty(wxStoreCodeQuery.getLongitude()) && StringUtils.isNotEmpty(wxStoreCodeQuery.getLatitude())){

            List<WeStoreCodeConfig> weStoreCodeConfigList = iWeStoreCodeConfigService.list(new LambdaQueryWrapper<WeStoreCodeConfig>()
                    .eq(WeStoreCodeConfig::getStoreCodeType, wxStoreCodeQuery.getStoreCodeType()));


            if(CollectionUtil.isNotEmpty(weStoreCodeConfigList)){

                WeStoreCodeConfig weStoreCodeConfig = weStoreCodeConfigList.stream().findFirst().get();

                weStoreCodesVo.setWelcomeMsg(weStoreCodeConfig.getWelcomeMsg());

                List<WeStoreCode> storeCode
                        = this.baseMapper
                        .findStoreCode(wxStoreCodeQuery.getLongitude(), wxStoreCodeQuery.getLatitude(), wxStoreCodeQuery.getArea(), weStoreCodeConfig.getRaidus());

                if(CollectionUtil.isNotEmpty(storeCode)){
                        weStoreCodesVo.setWeStoreCodes(
                                storeCode
                        );
                        if(wxStoreCodeQuery.getIsCount()){
                            storeCode.stream().forEach(kk->{
                                wxStoreCodeQuery.setStoreCodeId(kk.getId());
                                wxStoreCodeQuery.setStoreCodeType(weStoreCodeConfig.getStoreCodeType());
                                this.countUserBehavior(wxStoreCodeQuery);

                            });

                        }

                }


            }
        }


        if(StringUtils.isNotEmpty(wxStoreCodeQuery.getArea())){
            weStoreCodesVo.setWeStoreCodes(
                    this.baseMapper.findStoreCode(wxStoreCodeQuery.getLongitude(), wxStoreCodeQuery.getLatitude(),wxStoreCodeQuery.getArea(),null)
            );
        }
        return weStoreCodesVo;
    }

    @Override
    public void countUserBehavior(WxStoreCodeQuery wxStoreCodeQuery) {
        List<WeStoreCodeCount> weStoreCodeCounts = weStoreCodeCountMapper.selectList(new LambdaQueryWrapper<WeStoreCodeCount>()
                .eq(WeStoreCodeCount::getUnionid, wxStoreCodeQuery.getUnionid())
                .eq(WeStoreCodeCount::getSource,wxStoreCodeQuery.getStoreCodeType())
                .eq(wxStoreCodeQuery.getStoreCodeId()!=null,WeStoreCodeCount::getStoreCodeId,wxStoreCodeQuery.getStoreCodeId())
                .apply("date_format (create_time,'%Y-%m-%d') = date_format ({0},'%Y-%m-%d')", new Date()));
        if(CollectionUtil.isEmpty(weStoreCodeCounts)){
            weStoreCodeCountMapper.insert(WeStoreCodeCount.builder()
                            .storeCodeId(wxStoreCodeQuery.getStoreCodeId())
                            .currentLat(wxStoreCodeQuery.getLatitude())
                            .currentLng(wxStoreCodeQuery.getLongitude())
                            .unionid(wxStoreCodeQuery.getUnionid())
                            .source(wxStoreCodeQuery.getStoreCodeType())
                    .build());
        }



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

    @Override
    public void batchUpdateState(Integer storeState, List<Long> ids) {
        this.baseMapper.batchUpdateState(storeState,ids);
    }


}
