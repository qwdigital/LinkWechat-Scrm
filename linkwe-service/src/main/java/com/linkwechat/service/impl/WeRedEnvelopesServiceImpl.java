package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.enums.ReEnvelopesStateType;
import com.linkwechat.common.enums.RedEnvelopesReturnStatus;
import com.linkwechat.common.enums.RedEnvelopesType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.*;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.envelopes.WeRedEnvelopes;
import com.linkwechat.domain.envelopes.WeRedEnvelopesLimit;
import com.linkwechat.domain.envelopes.WeRedEnvelopesRecord;
import com.linkwechat.domain.envelopes.WeUserRedEnvelopsLimit;
import com.linkwechat.domain.envelopes.dto.H5RedEnvelopesDetailDto;
import com.linkwechat.domain.envelopes.dto.WeRedEnvelopesParmDto;
import com.linkwechat.domain.envelopes.dto.WeRedEnvelopesResultDto;
import com.linkwechat.domain.envelopes.query.H5RedEnvelopesParmQuery;
import com.linkwechat.domain.envelopes.query.WeRedEnvelopeListQuery;
import com.linkwechat.domain.envelopes.vo.WeCutomerRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeGroupRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeRedEnvelopesCountVo;
import com.linkwechat.mapper.WeRedEnvelopesLimitMapper;
import com.linkwechat.mapper.WeRedEnvelopesMapper;
import com.linkwechat.mapper.WeRedEnvelopesRecordMapper;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeRedEnvelopesService;
import com.linkwechat.service.IWeUserRedEnvelopsLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WeRedEnvelopesServiceImpl extends ServiceImpl<WeRedEnvelopesMapper, WeRedEnvelopes> implements IWeRedEnvelopesService {

    @Autowired
    private WeRedEnvelopesLimitMapper redEnvelopesLimitMapper;


    @Autowired
    private WeRedEnvelopesRecordMapper weRedEnvelopesRecordMapper;

    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeUserRedEnvelopsLimitService iWeUserRedEnvelopsLimitService;


    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Override
    public WeRedEnvelopesLimit findLimitRedEnvelopes() {
        List<WeRedEnvelopesLimit> weRedEnvelopesLimits =
                redEnvelopesLimitMapper.selectList(new LambdaQueryWrapper<>());
        if(CollectionUtil.isNotEmpty(weRedEnvelopesLimits)){
            return weRedEnvelopesLimits.stream().findFirst().get();
        }
        return WeRedEnvelopesLimit.builder().build();
    }

    @Override
    public void limitRedEnvelopes(WeRedEnvelopesLimit envelopesLimit) {
        WeRedEnvelopesLimit weRedEnvelopesLimit
                = redEnvelopesLimitMapper.selectById(envelopesLimit.getId());
        if(null != weRedEnvelopesLimit){
            redEnvelopesLimitMapper.updateById(envelopesLimit);
        }else{
            redEnvelopesLimitMapper.insert(envelopesLimit);
        }
    }

    @Override
    public List<WeCutomerRedEnvelopesVo> findRedEnveForUser(WeRedEnvelopesRecord redEnvelopesRecord) {
        return weRedEnvelopesRecordMapper.findRedEnveForUser(redEnvelopesRecord);
    }

    @Override
    public List<WeGroupRedEnvelopesVo> findRedEnveForGroup(WeRedEnvelopesRecord redEnvelopesRecord) {
        return weRedEnvelopesRecordMapper.findRedEnveForGroup(redEnvelopesRecord);
    }

    @Override
    public List<WeRedEnvelopesCountVo> countLineChart(String startTime, String endTime) {
        return weRedEnvelopesRecordMapper.countLineChart(startTime,endTime, DateUtils.sliceUpDateRange(startTime,endTime,3));
    }

    @Override
    public List<WeCutomerRedEnvelopesVo> findRedEnveForGroupUser(String chatId, String orderNo) {
        return weRedEnvelopesRecordMapper.findRedEnveForGroupUser(chatId,orderNo);
    }

    @Override
    public WeRedEnvelopesCountVo countTab() {
        return weRedEnvelopesRecordMapper.countTab();
    }

    @Override
    public List<WeRedEnvelopesCountVo> findRecordGroupByUserId(String startTime, String endTime) {
        return weRedEnvelopesRecordMapper.findRecordGroupByUserId(startTime,endTime);
    }

    @Override
    @Transactional
    public String createCustomerRedEnvelopesOrder(String redenvelopesId, int redEnvelopeAmount, String redEnvelopeName, Integer redEnvelopeNum, String sendUserId, Integer fromType, String externalUserId) {

        String returnMsg = checkWeUserQuotaLimit(sendUserId,redEnvelopeAmount);

        if(StringUtils.isNotEmpty(returnMsg)){
            throw new WeComException(HttpStatus.NOT_ORTHER_IMPLEMENTED,returnMsg);
        }

        String orderNo= WxPayUtils.getMchBillNo();

        WeCustomer weCustomer = iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddUserId, sendUserId)
                .eq(WeCustomer::getExternalUserid, externalUserId));

        WeRedEnvelopesRecord weRedEnvelopesRecord=WeRedEnvelopesRecord.builder()
                .userId(sendUserId)
                .receiveOrderNo(orderNo)
                .receiveName(weCustomer!=null?weCustomer.getCustomerName():"")
                .avatar(weCustomer!=null?weCustomer.getAvatar():"")
                .redEnvelopeNum(redEnvelopeNum)
                .redEnvelopeName(redEnvelopeName)
                .redEnvelopeMoney(redEnvelopeAmount)
                .sendState(ReEnvelopesStateType.STATE_TYPE_DLQ.getKey())
                .orderNo(orderNo)
                .fromType(fromType)
                .source(2)
                .build();

        weRedEnvelopesRecordMapper.insert(weRedEnvelopesRecord);

        recordRedEnvelopesNum(redenvelopesId);

        return orderNo;
    }

    @Override
    public String checkWeUserQuotaLimit(String sendUserId, int redEnvelopeAmount) {
        StringBuilder sb=new StringBuilder();

        List<WeUserRedEnvelopsLimit> weUserRedEnvelopsLimits = iWeUserRedEnvelopsLimitService.list(new LambdaQueryWrapper<WeUserRedEnvelopsLimit>()
                .eq(WeUserRedEnvelopsLimit::getWeUserId, sendUserId));

        if(CollectionUtil.isEmpty(weUserRedEnvelopsLimits)){//员工为配置限额不可发红包
            sb.append("请联系管理员配置员工限额");
        }else{

            WeUserRedEnvelopsLimit weUserRedEnvelopsLimit
                    = weUserRedEnvelopsLimits.stream().findFirst().get();
            Integer customerReceiveMoney = weUserRedEnvelopsLimit.getSingleCustomerReceiveMoney();
            List<WeRedEnvelopesRecord> recordList = weRedEnvelopesRecordMapper.selectList(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                    .eq(WeRedEnvelopesRecord::getUserId, sendUserId)
                    .eq(WeRedEnvelopesRecord::getFromType, 1));
            int redMoneyTotal = recordList.stream().mapToInt(WeRedEnvelopesRecord::getRedEnvelopeMoney).sum();
            if(customerReceiveMoney < redEnvelopeAmount + redMoneyTotal){
                return "当前红包额度不够";
            }

            //获取当前员工发送的个人红包，同时被人领取的

            List<WeRedEnvelopesRecord> weRedEnvelopesRecords = weRedEnvelopesRecordMapper.selectList(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                    .eq(WeRedEnvelopesRecord::getUserId, sendUserId)
                    .eq(WeRedEnvelopesRecord::getFromType, 1)
                    .eq(WeRedEnvelopesRecord::getSendState, 2));

            if(CollectionUtil.isNotEmpty(weRedEnvelopesRecords)){
                if(weRedEnvelopesRecords.size()>=weUserRedEnvelopsLimit.getSingleCustomerReceiveNum()){
                    sb.append("当前发送红包超过限定次数");
                }

                if(weRedEnvelopesRecords.stream().mapToInt(WeRedEnvelopesRecord::getRedEnvelopeMoney).sum()
                        >=weUserRedEnvelopsLimit.getSingleCustomerReceiveMoney()){
                    sb.append("当前发送红包超过限定额度");
                }
            }

        }


        return  sb.toString();
    }

    @Override
    public String checkRedEnvelopesTplLimit() {
        StringBuilder sb=new StringBuilder();

        WeRedEnvelopesLimit weRedEnvelopesLimit = redEnvelopesLimitMapper.selectOne(new LambdaQueryWrapper<>());

        if(null == weRedEnvelopesLimit){
            sb.append("企业限额暂为配置,请联系管理员");
        }
        List<WeRedEnvelopesRecord> weRedEnvelopesRecords = weRedEnvelopesRecordMapper.selectList(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                .eq(WeRedEnvelopesRecord::getFromType, 2)
                .eq(WeRedEnvelopesRecord::getSendState, 2));
        if(CollectionUtil.isNotEmpty(weRedEnvelopesRecords)){

            if(weRedEnvelopesRecords.stream().mapToInt(WeRedEnvelopesRecord::getRedEnvelopeMoney).sum()>=weRedEnvelopesLimit.getSingleDayPay()){
                sb.append("已超过企业当天付款总额");
            }

        }

        return  sb.toString();
    }

    /**
     *  生成群红包订单
     * @param redEnvelopeAmount
     * @param redEnvelopeName
     * @param redEnvelopeNum
     * @param chatId
     * @param sendUserId
     * @param redEnvelopesType
     * @return
     */
    @Override
    @Transactional
    public String createGroupRedEnvelopesOrder(String redenvelopesId, int redEnvelopeAmount, String redEnvelopeName, Integer redEnvelopeNum, String chatId, String sendUserId, Integer redEnvelopesType, Integer fromType) {

        String returnMsg = checkWeUserQuotaLimit(sendUserId, redEnvelopeAmount);

        if(StringUtils.isNotEmpty(returnMsg)){
            throw  new WeComException(HttpStatus.NOT_ORTHER_IMPLEMENTED,returnMsg);
        }
        String orderNo= WxPayUtils.getMchBillNo();

        weRedEnvelopesRecordMapper.insert(
                WeRedEnvelopesRecord.builder()
                        .userId(sendUserId)
                        .chatId(chatId)
                        .redEnvelopeNum(redEnvelopeNum)
                        .redEnvelopeName(redEnvelopeName)
                        .redEnvelopeMoney(redEnvelopeAmount)
                        .redEnvelopeType(redEnvelopesType)
                        .sendState(ReEnvelopesStateType.STATE_TYPE_DLQ.getKey())
                        .orderNo(orderNo)
                        .receiveType(2)
                        .fromType(fromType)
                        .build()

        );

        if(redEnvelopesType.equals(2)){//手气红包
            //随机金额
            float thresh=0.25f;
            if((redEnvelopeNum/(redEnvelopeAmount/100))<=3){
                thresh=0.00f;
            }

            if((redEnvelopeNum/(redEnvelopeAmount/100))<=2){
                thresh=0.1f;
            }
            List<BigDecimal> bigDecimals = RedPacketUtils.genRanddomList(new BigDecimal(redEnvelopeAmount).divide(new BigDecimal(100)),redEnvelopeNum,
                    new BigDecimal(0.3), new BigDecimal(200),thresh);
            if(CollectionUtil.isNotEmpty(bigDecimals)){
                bigDecimals.stream().forEach(k->{
                    weRedEnvelopesRecordMapper.insert(
                            WeRedEnvelopesRecord.builder()
                                    .userId(sendUserId)
                                    .chatId(chatId)
                                    .redEnvelopeNum(1)
                                    .redEnvelopeName(redEnvelopeName)
                                    .redEnvelopeMoney(k.multiply(new BigDecimal(100)).intValue())
                                    .redEnvelopeType(redEnvelopesType)
                                    .sendState(ReEnvelopesStateType.STATE_TYPE_DLQ.getKey())
                                    .orderNo(orderNo)
                                    .receiveOrderNo(WxPayUtils.getMchBillNo())
                                    .receiveType(2)
                                    .source(2)
                                    .build()
                    );

                });
            }
        }else if(redEnvelopesType.equals(1)){ //普通红包


            for(int i=0;i<redEnvelopeNum;i++){
                weRedEnvelopesRecordMapper.insert(
                        WeRedEnvelopesRecord.builder()
                                .userId(sendUserId)
                                .chatId(chatId)
                                .redEnvelopeNum(1)
                                .redEnvelopeName(redEnvelopeName)
                                .redEnvelopeMoney(redEnvelopeAmount/redEnvelopeNum)
                                .redEnvelopeType(redEnvelopesType)
                                .sendState(ReEnvelopesStateType.STATE_TYPE_DLQ.getKey())
                                .orderNo(orderNo)
                                .receiveOrderNo(WxPayUtils.getMchBillNo())
                                .receiveType(2)
                                .source(2)
                                .build()
                );

            }


        }
        recordRedEnvelopesNum(redenvelopesId);
        return orderNo;
    }

    @Override
    public String checkCustomerRedEnvelopesLimit(String officialAccountOpenId) {
        StringBuilder sb=new StringBuilder();

        WeRedEnvelopesLimit weRedEnvelopesLimit = redEnvelopesLimitMapper.selectOne(new LambdaQueryWrapper<>());

        if(null == weRedEnvelopesLimit){
            sb.append("企业限额暂未配置,请联系管理员");
        }

        List<WeRedEnvelopesRecord> weRedEnvelopesRecords = weRedEnvelopesRecordMapper.selectList(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                .eq(WeRedEnvelopesRecord::getOpenId, officialAccountOpenId)
                .eq(WeRedEnvelopesRecord::getSendState, 2));
        if(CollectionUtil.isNotEmpty(weRedEnvelopesRecords)){

            if(weRedEnvelopesRecords.size()>=weRedEnvelopesLimit.getSingleCustomerReceiveNum()){
                sb.append("今日领取红包超次数,请明日再领取");
            }
            if(weRedEnvelopesRecords.stream().mapToInt(WeRedEnvelopesRecord::getRedEnvelopeMoney).sum()>=weRedEnvelopesLimit.getSingleCustomerReceiveMoney()){
                sb.append("今日领取红包超额,请明日再领取");
            }
        }

        return sb.toString();
    }

    @Override
    public String customerReceiveRedEnvelopes(String orderNo, String officialAccountOpenId, String receiveName, String avatar) throws Exception {

        //领取红包
        String checkMsg = checkCustomerRedEnvelopesLimit(officialAccountOpenId);

        if(StringUtils.isNotEmpty(checkMsg)){
            throw new WeComException(HttpStatus.NOT_IMPLEMENTED,checkMsg);
        }

        StringBuilder sb=new StringBuilder();

        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);

        if(null == weCorpAccount ){
            sb.append("联系管理员,确认红包参数配置");
        }

        if(null != weCorpAccount && StringUtils.isNotEmpty(
                weCorpAccount.getMerChantName()
        ) && StringUtils.isNotEmpty(weCorpAccount.getMerChantNumber()) && StringUtils.isNotEmpty(
                weCorpAccount.getMerChantSecret()
        ) && StringUtils.isNotEmpty(weCorpAccount.getCertP12Url())) {

            //校验当前订单红包是否存在
            if(ObjectUtil.isEmpty(weRedEnvelopesRecordMapper.selectCount(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                    .eq(WeRedEnvelopesRecord::getOrderNo, orderNo)))){//不存在
                sb.append("当前红包不存在");
            }else{ //存在

                //判断当前客户是否领取过
                WeRedEnvelopesRecord weRedEnvelopesRecord = weRedEnvelopesRecordMapper.selectOne(
                        new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                                .eq(WeRedEnvelopesRecord::getOpenId,officialAccountOpenId)
                                .eq(WeRedEnvelopesRecord::getOrderNo, orderNo)
                );
                if(null != weRedEnvelopesRecord){//领取过
                    //领取失败重试
                    if(ReEnvelopesStateType.STATE_TYPE_FSSB.getKey().equals(
                            weRedEnvelopesRecord.getSendState())){
                        String returnMsg =
                                receiveRedEnvelopes(weRedEnvelopesRecord, orderNo, weCorpAccount, weCorpAccount.getWxAppId(), officialAccountOpenId, weRedEnvelopesRecord.getRedEnvelopeMoney(),receiveName,avatar);
                        if(StringUtils.isNotEmpty(returnMsg)){
                            sb.append(returnMsg);
                        }
                    }else if(ReEnvelopesStateType.STATE_TYPE_YTK.getKey().equals(
                            weRedEnvelopesRecord.getSendState())){//超时退款的
                        sb.append(RedEnvelopesReturnStatus.RETURN_STATUS_REFUND.getDesc());
                    }

                }else{//未领取过(首次领取)
                    //获取等待领取的订单
                    List<WeRedEnvelopesRecord> waitReceiveOrders = weRedEnvelopesRecordMapper.selectList(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                            .eq(WeRedEnvelopesRecord::getOrderNo, orderNo)
                            .eq(WeRedEnvelopesRecord::getSource,2)
                            .isNull(WeRedEnvelopesRecord::getOpenId));
                    if(CollectionUtil.isNotEmpty(waitReceiveOrders)){
                        WeRedEnvelopesRecord waitReceiveOrder = waitReceiveOrders.stream().findFirst().get();

                        if(waitReceiveOrder.getSendState()
                                .equals(ReEnvelopesStateType.STATE_TYPE_DLQ.getKey())){
                            String returnMsg =
                                    receiveRedEnvelopes(waitReceiveOrder, waitReceiveOrder.getReceiveOrderNo(), weCorpAccount, weCorpAccount.getWxAppId(), officialAccountOpenId, waitReceiveOrder.getRedEnvelopeMoney(),receiveName,avatar);
                            if(StringUtils.isNotEmpty(returnMsg)){
                                sb.append(returnMsg);
                            }

                        }
                    }
                }
            }
        }else{
            sb.append("联系管理员,确认红包参数配置");
        }


        return sb.toString();
    }

    @Override
    public H5RedEnvelopesDetailDto detailDto(String orderNo, String openid, String chatId) {
        H5RedEnvelopesDetailDto h5RedEnvelopesDetailDto
                = H5RedEnvelopesDetailDto.builder().build();

        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);
        if(null != weCorpAccount){
            h5RedEnvelopesDetailDto.setCorpName(weCorpAccount.getCompanyName());
            h5RedEnvelopesDetailDto.setLogo(weCorpAccount.getLogoUrl());
        }

        if(StringUtils.isEmpty(chatId)){//非群成员相关设置
            WeRedEnvelopesRecord weRedEnvelopesRecord=weRedEnvelopesRecordMapper.selectOne(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                    .eq(WeRedEnvelopesRecord::getOrderNo,orderNo)
                    .eq(WeRedEnvelopesRecord::getOpenId,openid));
            if(null != weRedEnvelopesRecord){
                h5RedEnvelopesDetailDto.setAcceptNum(1);
                h5RedEnvelopesDetailDto.setNoAcceptNum(1);
                h5RedEnvelopesDetailDto.setRedEnvelopesType(weRedEnvelopesRecord.getRedEnvelopeType());
                h5RedEnvelopesDetailDto.setCurrentAcceptMoney(weRedEnvelopesRecord.getRedEnvelopeMoney());
                h5RedEnvelopesDetailDto.setAccpectMoney(weRedEnvelopesRecord.getRedEnvelopeMoney());
                h5RedEnvelopesDetailDto.setTotalMoney(weRedEnvelopesRecord.getRedEnvelopeMoney());
                h5RedEnvelopesDetailDto.setRedEnvelopeNum(weRedEnvelopesRecord.getRedEnvelopeNum());
            }
        }else{//群成员相关红包设置
            WeRedEnvelopesRecord weRedEnvelopesRecord=weRedEnvelopesRecordMapper.selectOne(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                    .eq(WeRedEnvelopesRecord::getOrderNo,orderNo)
                    .eq(WeRedEnvelopesRecord::getSource,1)
                    .eq(WeRedEnvelopesRecord::getChatId,chatId));
            if(null != weRedEnvelopesRecord){
                h5RedEnvelopesDetailDto.setRedEnvelopesType(weRedEnvelopesRecord.getRedEnvelopeType());
                h5RedEnvelopesDetailDto.setTotalMoney(weRedEnvelopesRecord.getRedEnvelopeMoney());
            }
            WeRedEnvelopesRecord weCutomerRedEnvelopesRecord=weRedEnvelopesRecordMapper.selectOne(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                    .eq(WeRedEnvelopesRecord::getOrderNo,orderNo)
                    .eq(WeRedEnvelopesRecord::getChatId,chatId)
                    .eq(WeRedEnvelopesRecord::getOpenId,openid));
            if(null != weCutomerRedEnvelopesRecord){
                h5RedEnvelopesDetailDto.setCurrentAcceptMoney(weCutomerRedEnvelopesRecord.getRedEnvelopeMoney());
            }

            h5RedEnvelopesDetailDto.setNoAcceptNum(
                    weRedEnvelopesRecordMapper.selectCount(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                            .eq(WeRedEnvelopesRecord::getOrderNo,orderNo)
                            .eq(WeRedEnvelopesRecord::getSource,2)
                            .eq(WeRedEnvelopesRecord::getChatId,chatId)
                            .eq(WeRedEnvelopesRecord::getSendState,1))
            );

            h5RedEnvelopesDetailDto.setAcceptNum(
                    weRedEnvelopesRecordMapper.selectCount(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                            .eq(WeRedEnvelopesRecord::getOrderNo,orderNo)
                            .eq(WeRedEnvelopesRecord::getSource,2)
                            .eq(WeRedEnvelopesRecord::getChatId,chatId)
                            .eq(WeRedEnvelopesRecord::getSendState,2))
            );
        }


        h5RedEnvelopesDetailDto.setAccpectMoney(
                weRedEnvelopesRecordMapper.findAccpectMoney(orderNo)
        );

        h5RedEnvelopesDetailDto.setAccpestCustomerList(
                weRedEnvelopesRecordMapper.findAccpectCustomers(orderNo)
        );



        return h5RedEnvelopesDetailDto;
    }

    @Override
    public Boolean checkRecevieRedEnvelopes(String orderNo, String openid) {
        List<WeRedEnvelopesRecord> weRedEnvelopesRecords = weRedEnvelopesRecordMapper.selectList(new LambdaQueryWrapper<WeRedEnvelopesRecord>()
                .eq(WeRedEnvelopesRecord::getOpenId, openid)
                .eq(WeRedEnvelopesRecord::getOrderNo, orderNo)
                .eq(WeRedEnvelopesRecord::getSendState, 2));
        if(CollectionUtil.isEmpty(weRedEnvelopesRecords)){
            return false;
        }


        return true;
    }

    @Override
    public List<WeRedEnvelopes> getList(WeRedEnvelopeListQuery query) {
        List<WeRedEnvelopes> list = list(new LambdaQueryWrapper<WeRedEnvelopes>()
                .eq(Objects.nonNull(query.getStatus()), WeRedEnvelopes::getStatus, query.getStatus())
                .in(StringUtils.isNotBlank(query.getSceneType()), WeRedEnvelopes::getSceneType, Arrays.stream(query.getSceneType().split(",")).collect(Collectors.toList()))
                .eq(Objects.nonNull(query.getRedEnvelopesType()), WeRedEnvelopes::getRedEnvelopesType, query.getRedEnvelopesType())
                .like(StringUtils.isNotBlank(query.getName()), WeRedEnvelopes::getName, query.getName())
                .ge(Objects.nonNull(query.getBeginTime()), BaseEntity::getCreateTime, DateUtil.formatDate(query.getBeginTime()))
                .le(Objects.nonNull(query.getEndTime()), BaseEntity::getCreateTime, DateUtil.formatDate(query.getEndTime()))
                .orderByDesc(WeRedEnvelopes::getCreateTime));
        return list;
    }

    //记录红包发送次数
    private void recordRedEnvelopesNum(String redenvelopesId){

        if(StringUtils.isNotEmpty(redenvelopesId)){
            WeRedEnvelopes weRedEnvelopes
                    = this.getById(redenvelopesId);
            if(null != weRedEnvelopes){
                weRedEnvelopes.setSendTimes(
                        weRedEnvelopes.getSendTimes()+1
                );
                this.updateById(weRedEnvelopes);
            }
        }
    }


    private String receiveRedEnvelopes(WeRedEnvelopesRecord weRedEnvelopesRecord,String orderNo,WeCorpAccount weCorpAccount,String appId,String officialAccountOpenId,Integer amount
            ,String receiveName,String avatar) throws Exception {


        //超过24小时不可重新领取,设置为退款
        if(!DateUtils.judgmentDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,weRedEnvelopesRecord.getCreateTime()),24)){
            weRedEnvelopesRecord.setErrCode(RedEnvelopesReturnStatus.RETURN_STATUS_REFUND.getCode());
            weRedEnvelopesRecord.setErrCodeDes(RedEnvelopesReturnStatus.RETURN_STATUS_REFUND.getDesc());
            weRedEnvelopesRecord.setSendState(ReEnvelopesStateType.STATE_TYPE_YTK.getKey());
            weRedEnvelopesRecord.setReason(RedEnvelopesReturnStatus.RETURN_STATUS_REFUND.getDesc());
            weRedEnvelopesRecordMapper.updateById(
                    weRedEnvelopesRecord
            );

            return RedEnvelopesReturnStatus.RETURN_STATUS_REFUND.getDesc();
        }


        WeRedEnvelopesParmDto weRedEnvelopes = WeRedEnvelopesParmDto.builder()
                .mch_appid(appId)
                .mchid(weCorpAccount.getMerChantNumber())
                .nonce_str(StringUtils.getRandomString(32))
                .partner_trade_no(orderNo)
                .openid(officialAccountOpenId)
                .check_name("NO_CHECK")
                .amount(amount)
                .desc(weCorpAccount.getCompanyName()+"的红包")
                .build();
        weRedEnvelopes.setSign(
                WxPayUtils.redEnvelopeSign(ObjectUtils.convertBean(weRedEnvelopes),  weCorpAccount.getMerChantSecret())
        );

        WeRedEnvelopesResultDto resultDto = XmlUtils.convertToJavaBean(
                WxPayUtils.doPostRedEnvelope(XmlUtils.convertToXml(weRedEnvelopes),weCorpAccount.getCertP12Url(),weCorpAccount.getMerChantNumber())
                ,WeRedEnvelopesResultDto.class);

        if(resultDto.getReturnCode().equals("SUCCESS")){//发送成功修改红包状态
            weRedEnvelopesRecord.setErrCodeDes(StringUtils.isNotEmpty(resultDto.getErrCode())?RedEnvelopesReturnStatus.of(resultDto.getErrCode()).getDesc():"");
            weRedEnvelopesRecord.setErrCode(resultDto.getErrCode());
            weRedEnvelopesRecord.setOpenId(officialAccountOpenId);
            weRedEnvelopesRecord.setAvatar(avatar);
            weRedEnvelopesRecord.setReason(resultDto.getErrCodeDes());
            weRedEnvelopesRecord.setReceiveName(receiveName);
            weRedEnvelopesRecord.setSendState(resultDto.getResultCode().equals("SUCCESS")?ReEnvelopesStateType.STATE_TYPE_YLQ.getKey():ReEnvelopesStateType.STATE_TYPE_FSSB.getKey());
            weRedEnvelopesRecordMapper.updateById(
                    weRedEnvelopesRecord
            );
        }
        return StringUtils.isNotEmpty(resultDto.getErrCode())?RedEnvelopesReturnStatus.of(resultDto.getErrCode()).getDesc():"";
    }




}
