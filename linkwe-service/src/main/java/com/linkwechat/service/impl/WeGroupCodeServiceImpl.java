package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeComeStateContants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.uuid.UUID;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeGroupCodeRange;
import com.linkwechat.domain.WeCommonLinkStat;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.vo.WeCustomerChannelCountVo;
import com.linkwechat.domain.groupchat.vo.WeGroupChannelCountVo;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupcode.query.WeMakeGroupCodeTagQuery;
import com.linkwechat.domain.groupcode.vo.WeGroupChatInfoVo;
import com.linkwechat.domain.groupcode.vo.WeGroupCodeCountTrendVo;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.qr.vo.WeQrCodeScanCountVo;
import com.linkwechat.domain.qr.vo.WeQrCodeScanLineCountVo;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatAddJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.query.weixin.WxJumpWxaQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatAddJoinWayVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.weixin.WxJumpWxaVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QxAppletClient;
import com.linkwechat.mapper.WeGroupCodeMapper;
import com.linkwechat.service.IWeGroupCodeRangeService;
import com.linkwechat.service.IWeGroupCodeService;
import com.linkwechat.service.IWeGroupCodeTagRelService;
import com.linkwechat.service.IWeTagService;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWeCommonLinkStatService weCommonLinkStatService;

    @Autowired
    private IWeGroupCodeRangeService iWeGroupCodeRangeService;


    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Value("${weixin.short.env-version:develop}")
    private String shortEnvVersion;

    @Resource
    private QxAppletClient qxAppletClient;



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
            weGroupCodes.forEach(k->{

                String encode = Base62NumUtil.encode(k.getId());
                k.setQrShortLink(linkWeChatConfig.getQrGroupShortLinkDomainName() + encode);

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

        if(CollectionUtil.isNotEmpty(weGroupChatInfoVos)){
            //设置关联状态
            weGroupChatInfoVos.stream().forEach(k->{
                List<WeGroupCodeRange> weGroupCodeRanges = iWeGroupCodeRangeService.list(new LambdaQueryWrapper<WeGroupCodeRange>()
                        .eq(WeGroupCodeRange::getCodeId, groupId)
                        .eq(WeGroupCodeRange::getChatId, k.getChatId()));
                if(CollectionUtil.isNotEmpty(weGroupCodeRanges)){
                            k.setStatus(
                                    weGroupCodeRanges.stream().findFirst().get().getStatus()
                            );
                }


            });

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

    @Override
    public JSONObject getShort2LongUrl(String shortUrl) {
        long id = Base62NumUtil.decode(shortUrl);
        JSONObject resObj = new JSONObject();
        WeGroupCode groupCode = getById(id);
        if (Objects.isNull(groupCode)) {
            resObj.put("errorMsg", "无效链接");
            return resObj;
        }
        resObj.put("type",3);

        if (StringUtils.isNotEmpty(groupCode.getCodeUrl())) {
            resObj.put("qrCode", groupCode.getCodeUrl());
        }
        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        if (Objects.isNull(corpAccount)) {
            resObj.put("errorMsg", "请未配置企业信息");
            return resObj;
            //throw new WeComException("请未配置企业信息");
        }
        if (StringUtils.isEmpty(corpAccount.getWxAppletOriginalId())) {
            resObj.put("errorMsg", "请未配置小程序原始ID");
            return resObj;
            //throw new WeComException("请未配置小程序原始ID");
        }

        WxJumpWxaQuery wxaQuery = new WxJumpWxaQuery();
        WxJumpWxaQuery.JumpWxa wxa = new WxJumpWxaQuery.JumpWxa();
        wxa.setPath(linkWeChatConfig.getShortAppletUrl());
        wxa.setQuery("id=" + shortUrl + "&sence=gqr");
        wxa.setEnv_version(shortEnvVersion);
        wxaQuery.setJump_wxa(wxa);
        WxJumpWxaVo wxJumpWxa = qxAppletClient.generateScheme(wxaQuery).getData();
        if (Objects.nonNull(wxJumpWxa) && StringUtils.isNotEmpty(wxJumpWxa.getOpenLink())) {
            resObj.put("url_scheme", wxJumpWxa.getOpenLink());
        } else {
            resObj.put("errorMsg", "生成小程序跳转链接失败");
            //throw new WeComException("生成小程序跳转链接失败");
        }
        resObj.put("user_name", corpAccount.getWxAppletOriginalId());
        resObj.put("path", linkWeChatConfig.getShortAppletUrl());
        resObj.put("query", "id=" + shortUrl + "&sence=gqr");
        return resObj;
    }

    @Override
    public WeGroupCode getDetail(String id) {
        WeGroupCode groupCode = getById(id);
        String encode = Base62NumUtil.encode(groupCode.getId());
        groupCode.setQrShortLink(linkWeChatConfig.getQrGroupShortLinkDomainName() + encode);
        return groupCode;
    }

    @Override
    public WeQrCodeScanCountVo getWeQrCodeScanTotalCount(WeGroupCode weGroupCode) {

        WeQrCodeScanCountVo weQrCodeScanCountVo = new WeQrCodeScanCountVo();
        WeGroupCode weQrCode = getById(weGroupCode.getId());
        if(Objects.isNull(weQrCode)){
            throw new WeComException("无效群活码ID");
        }
        List<WeGroupChannelCountVo> memberNumByState = weGroupMemberService.getMemberNumByState(weQrCode.getState(),null,null);

        if(CollectionUtil.isNotEmpty(memberNumByState)){
            //累计扫码次数
            int totalNum = memberNumByState.stream().mapToInt(WeGroupChannelCountVo::getMemberNumber).sum();
            weQrCodeScanCountVo.setTotal(totalNum);
            //今日扫码次数
            int todayNum = memberNumByState.stream().filter(item -> ObjectUtil.equal(item.getDate(), DateUtil.today())).mapToInt(WeGroupChannelCountVo::getMemberNumber).sum();
            weQrCodeScanCountVo.setToday(todayNum);
        }

        String shortUrl = Base62NumUtil.encode(weGroupCode.getId());
        //今日PV
        int todayPvNum = (int) Optional.ofNullable(redisService.getCacheObject(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.PV + "gqr:" + shortUrl)).orElse(0);
        weQrCodeScanCountVo.setTodayLinkVisitsTotal(todayPvNum);
        //今日UV
        Long todayUvNum =redisService.hyperLogLogCount(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.UV + "gqr:" + shortUrl);
        weQrCodeScanCountVo.setTodayLinkVisitsPeopleTotal(todayUvNum.intValue());

        List<WeCommonLinkStat> statList = weCommonLinkStatService.getStatByShortId(weQrCode.getId(),"gqr");
        if(CollectionUtil.isNotEmpty(statList)){
            int pvNum = statList.stream().mapToInt(WeCommonLinkStat::getPvNum).sum();
            weQrCodeScanCountVo.setLinkVisitsTotal(pvNum + todayPvNum);

            int uvNum = statList.stream().mapToInt(WeCommonLinkStat::getUvNum).sum();
            weQrCodeScanCountVo.setLinkVisitsPeopleTotal(uvNum + todayUvNum.intValue());
        }

        return weQrCodeScanCountVo;
    }

    @Override
    public List<WeQrCodeScanLineCountVo> getWeQrCodeScanLineCount(WeGroupCode weGroupCode) {
        List<WeQrCodeScanLineCountVo> weQrCodeScanCountList = new LinkedList<>();
        WeGroupCode weQrCode = getById(weGroupCode.getId());
        if(Objects.isNull(weQrCode)){
            throw new WeComException("无效群活码ID");
        }
        DateTime startTime = StringUtils.isNotBlank(weGroupCode.getBeginTime())?DateUtil.parseDate(weGroupCode.getBeginTime()):DateUtil.offsetDay(new Date(),-7);
        DateTime endTime = StringUtils.isNotBlank(weGroupCode.getEndTime())?DateUtil.parseDate(weGroupCode.getEndTime()):DateUtil.date();
        List<WeGroupChannelCountVo> memberNumByState = weGroupMemberService.getMemberNumByState(weQrCode.getState(),startTime,endTime);
        List<DateTime> dateTimes = DateUtil.rangeToList(startTime, endTime, DateField.DAY_OF_YEAR);

        Map<String, List<WeGroupChannelCountVo>> memberListMap = new HashMap<>();

        if(CollectionUtil.isNotEmpty(memberNumByState)){
            memberListMap = memberNumByState.stream().collect(Collectors.groupingBy(WeGroupChannelCountVo::getDate));
        }


        Map<String, List<WeCommonLinkStat>> statListMap = new HashMap<>();

        String shortUrl = Base62NumUtil.encode(weGroupCode.getId());
        //今日PV
        int todayPvNum = (int) Optional.ofNullable(redisService.getCacheObject(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.PV + "qr:" + shortUrl)).orElse(0);
        //今日UV
        Long todayUvNum =redisService.hyperLogLogCount(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.UV + "gqr:" + shortUrl);

        WeCommonLinkStat weCommonLinkStat = new WeCommonLinkStat();
        weCommonLinkStat.setDateTime(DateUtil.date());
        weCommonLinkStat.setPvNum(todayPvNum);
        weCommonLinkStat.setUvNum(todayUvNum.intValue());
        statListMap.put(DateUtil.today(), Collections.singletonList(weCommonLinkStat));

        List<WeCommonLinkStat> statList = weCommonLinkStatService.getStatByShortId(weQrCode.getId(), "gqr");
        if(CollectionUtil.isNotEmpty(statList)){
            statListMap = statList.stream().collect(Collectors.groupingBy(item -> DateUtil.formatDate(item.getDateTime())));
        }

        for (DateTime dateTime : dateTimes) {
            WeQrCodeScanLineCountVo weQrCodeScanLineCountVo = new WeQrCodeScanLineCountVo();
            weQrCodeScanLineCountVo.setDateTime(dateTime.toDateStr());

            if(memberListMap.containsKey(dateTime.toDateStr())){
                //扫码次数
                int todayNum = memberListMap.get(dateTime.toDateStr()).stream().mapToInt(WeGroupChannelCountVo::getMemberNumber).sum();
                weQrCodeScanLineCountVo.setToday(todayNum);
            }

            if(statListMap.containsKey(dateTime.toDateStr())){
                int pvNum = statListMap.get(dateTime.toDateStr()).stream().mapToInt(WeCommonLinkStat::getPvNum).sum();
                weQrCodeScanLineCountVo.setTodayLinkVisitsTotal(pvNum);

                int uvNum = statListMap.get(dateTime.toDateStr()).stream().mapToInt(WeCommonLinkStat::getUvNum).sum();
                weQrCodeScanLineCountVo.setTodayLinkVisitsPeopleTotal(uvNum + todayUvNum.intValue());
            }
            weQrCodeScanCountList.add(weQrCodeScanLineCountVo);
        }
        return weQrCodeScanCountList;
    }

    @Override
    public List<WeQrCodeScanLineCountVo> getWeQrCodeScanSheetCount(WeGroupCode weGroupCode) {
        List<WeQrCodeScanLineCountVo> weQrCodeScanCountList = new LinkedList<>();
        WeGroupCode weQrCode = getById(weGroupCode.getId());
        if(Objects.isNull(weQrCode)){
            throw new WeComException("无效群活码ID");
        }
        DateTime startTime = StringUtils.isNotBlank(weGroupCode.getBeginTime())?DateUtil.parseDate(weGroupCode.getBeginTime()):DateUtil.offsetDay(new Date(),-7);
        DateTime endTime = StringUtils.isNotBlank(weGroupCode.getEndTime())?DateUtil.parseDate(weGroupCode.getEndTime()):DateUtil.date();
        List<WeGroupChannelCountVo> memberNumByState = weGroupMemberService.getMemberNumByState(weQrCode.getState(),null,endTime);
        List<DateTime> dateTimes = DateUtil.rangeToList(startTime, endTime, DateField.DAY_OF_YEAR);

        Map<String, List<WeGroupChannelCountVo>> memberListMap = new HashMap<>();

        if(CollectionUtil.isNotEmpty(memberNumByState)){
            memberListMap = memberNumByState.stream().collect(Collectors.groupingBy(WeGroupChannelCountVo::getDate));
        }


        Map<String, List<WeCommonLinkStat>> statListMap = new HashMap<>();

        String shortUrl = Base62NumUtil.encode(weGroupCode.getId());
        //今日PV
        int todayPvNum = (int) Optional.ofNullable(redisService.getCacheObject(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.PV + "gqr:" + shortUrl)).orElse(0);
        //今日UV
        Long todayUvNum =redisService.hyperLogLogCount(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.UV + "gqr:" + shortUrl);

        WeCommonLinkStat weCommonLinkStat = new WeCommonLinkStat();
        weCommonLinkStat.setDateTime(DateUtil.date());
        weCommonLinkStat.setPvNum(todayPvNum);
        weCommonLinkStat.setUvNum(todayUvNum.intValue());
        statListMap.put(DateUtil.today(), Collections.singletonList(weCommonLinkStat));

        List<WeCommonLinkStat> statList = weCommonLinkStatService.getStatByShortId(weQrCode.getId(), "qr");
        if(CollectionUtil.isNotEmpty(statList)){
            statListMap = statList.stream().collect(Collectors.groupingBy(item -> DateUtil.formatDate(item.getDateTime())));
        }

        for (DateTime dateTime : dateTimes) {
            WeQrCodeScanLineCountVo weQrCodeScanLineCountVo = new WeQrCodeScanLineCountVo();
            weQrCodeScanLineCountVo.setDateTime(dateTime.toDateStr());

            int totalNum = memberNumByState.stream().filter(item -> DateUtil.compare(DateUtil.parseDate(item.getDate()), dateTime) <= 0).mapToInt(WeGroupChannelCountVo::getMemberNumber).sum();
            weQrCodeScanLineCountVo.setTotal(totalNum);

            int totalPvNum = statList.stream().filter(item -> DateUtil.compare(item.getDateTime(), dateTime) <= 0).mapToInt(WeCommonLinkStat::getPvNum).sum();
            weQrCodeScanLineCountVo.setLinkVisitsTotal(totalPvNum);
            int totalUvNum = statList.stream().filter(item -> DateUtil.compare(item.getDateTime(), dateTime) <= 0).mapToInt(WeCommonLinkStat::getUvNum).sum();
            weQrCodeScanLineCountVo.setLinkVisitsPeopleTotal(totalUvNum);

            if(memberListMap.containsKey(dateTime.toDateStr())){
                //扫码次数
                int todayNum = memberListMap.get(dateTime.toDateStr()).stream().mapToInt(WeGroupChannelCountVo::getMemberNumber).sum();
                weQrCodeScanLineCountVo.setToday(todayNum);
            }

            if(statListMap.containsKey(dateTime.toDateStr())){
                int pvNum = statListMap.get(dateTime.toDateStr()).stream().mapToInt(WeCommonLinkStat::getPvNum).sum();
                weQrCodeScanLineCountVo.setTodayLinkVisitsTotal(pvNum);

                int uvNum = statListMap.get(dateTime.toDateStr()).stream().mapToInt(WeCommonLinkStat::getUvNum).sum();
                weQrCodeScanLineCountVo.setTodayLinkVisitsPeopleTotal(uvNum + todayUvNum.intValue());
            }
            weQrCodeScanCountList.add(weQrCodeScanLineCountVo);
        }
        return weQrCodeScanCountList;
    }


}
