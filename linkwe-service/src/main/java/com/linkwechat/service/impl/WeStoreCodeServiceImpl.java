package com.linkwechat.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.storecode.entity.WeStoreCode;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
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
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeStoreCodeCountMapper;
import com.linkwechat.mapper.WeStoreCodeMapper;
import com.linkwechat.service.IWeGroupCodeService;
import com.linkwechat.service.IWeStoreCodeConfigService;
import com.linkwechat.service.IWeStoreCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class WeStoreCodeServiceImpl extends ServiceImpl<WeStoreCodeMapper, WeStoreCode>
        implements IWeStoreCodeService {


    @Autowired
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private WeStoreCodeCountMapper weStoreCodeCountMapper;


    @Autowired
    private IWeStoreCodeConfigService iWeStoreCodeConfigService;


    @Autowired
    private IWeGroupCodeService weGroupCodeService;


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

//            if(StringUtils.isNotEmpty(weStoreCode.getShopGuideId())){
//                if(StringUtils.isNotEmpty(weStoreCode.getConfigId())){
//                    qwCustomerClient.updateContactWay(WeAddWayQuery.builder()
//                            .config_id(weStoreCode.getConfigId())
//                            .user(ListUtil.toList(weStoreCode.getShopGuideId().split(",")))
//                            .build());
//                }else{
//                    weStoreCode.setState(WeConstans.WE_STORE_CODE_PREFIX +SnowFlakeUtil.nextId());
//                    WeAddWayVo weAddWayResult = qwCustomerClient.addContactWay(WeAddWayQuery.builder()
//                            .type(2)
//                            .state(weStoreCode.getState())
//                            .scene(2)
//                            .user(ListUtil.toList(weStoreCode.getShopGuideId().split(",")))
//                            .build()).getData();
//                    weStoreCode.setConfigId(weAddWayResult.getConfigId());
//                    weStoreCode.setShopGuideUrl(weAddWayResult.getQrCode());
//                }
//            }else if(null != weStoreCode.getGroupCodeId()){ //群
//                WeGroupCode weGroupCode = weGroupCodeService.getById(weStoreCode.getGroupCodeId());
//                if(null != weGroupCode){
//                    weStoreCode.setState(weGroupCode.getState());
//                }
//            }

              saveOrUpdate(weStoreCode);
    }



    @Override
    public WeStoreShopGuideTabVo countWeStoreShopGuideTab() {
        return weStoreCodeCountMapper.countWeStoreShopGuideTab();
    }

    @Override
    public WeStoreTabVo countWeStoreTab(Long storeCodeId) {
        return weStoreCodeCountMapper.countWeStoreTab(storeCodeId,getById(storeCodeId).getGroupCodeId());
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


}
