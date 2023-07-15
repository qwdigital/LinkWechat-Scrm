package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.sql.SqlUtil;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.corp.query.WeCorpAccountQuery;
import com.linkwechat.domain.corp.vo.WeCorpAccountVo;
import com.linkwechat.domain.customer.vo.WeCustomerChannelCountVo;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import com.linkwechat.domain.qr.query.WeQrCodeListQuery;
import com.linkwechat.domain.qr.query.WeQrUserInfoQuery;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeContactWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeQrCodeMapper;
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

/**
 * 活码信息表(WeQrCode)$desc
 *
 * @author danmo
 * @since 2021-11-07 02:17:43
 */
@Slf4j
@Service
public class WeQrCodeServiceImpl extends ServiceImpl<WeQrCodeMapper, WeQrCode> implements IWeQrCodeService {

    @Autowired
    private IWeQrTagRelService tagRelService;

    @Autowired
    private IWeQrScopeService scopeService;

    @Autowired
    private IWeQrAttachmentsService attachmentsService;

    @Autowired
    @Lazy
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Resource
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private RabbitMQSettingConfig mqSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 新增员工活码
     *
     * @param weQrAddQuery 入参
     */
    @Transactional(rollbackFor = {WeComException.class,Exception.class})
    @Override
    public void addQrCode(WeQrAddQuery weQrAddQuery) {
        //校验排期是否存在冲突
        checkScope(weQrAddQuery.getQrUserInfos());
        WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();
        WeAddWayVo weAddWayResult = qwCustomerClient.addContactWay(weContactWay).getData();
        if (weAddWayResult != null && ObjectUtil.equal(0, weAddWayResult.getErrCode())) {
            WeQrCode weQrCode = weQrAddQuery.getWeQrCodeEntity(weAddWayResult.getConfigId(), weAddWayResult.getQrCode());
            if (save(weQrCode)) {
                //保存标签数据
                tagRelService.saveBatchByQrId(weQrCode.getId(),1,  weQrAddQuery.getQrTags());
                //保存活码范围数据
                scopeService.saveBatchByQrId(weQrCode.getId(), weQrAddQuery.getQrUserInfos());
                //保存活码素材
                attachmentsService.saveBatchByQrId(weQrCode.getId(),1, weQrAddQuery.getAttachments());
            }
            rabbitTemplate.convertAndSend(mqSettingConfig.getWeQrCodeChangeEx(),mqSettingConfig.getWeQrCodeChangeRk(),String.valueOf(weQrCode.getId()));
        }else {
            throw new WeComException(weAddWayResult.getErrCode(), WeErrorCodeEnum.parseEnum(weAddWayResult.getErrCode()).getErrorMsg());
        }
    }

    @Transactional(rollbackFor = {WeComException.class,Exception.class})
    @Override
    public void updateQrCode(WeQrAddQuery weQrAddQuery) {
        WeQrCode qrCode = getById(weQrAddQuery.getQrId());
        if(Objects.isNull(qrCode)){
            throw new WeComException("无效活码ID");
        }
        //校验排期是否存在冲突
        checkScope(weQrAddQuery.getQrUserInfos());

        WeQrCode weQrCode = null;
        //当类型相同时
        if(ObjectUtil.equal(qrCode.getType(),weQrAddQuery.getQrType())){
            WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();
            WeResultVo weResultVo = qwCustomerClient.updateContactWay(weContactWay).getData();
            if(Objects.isNull(weResultVo)){
                throw new WeComException("活码生成失败！");
            }
            if (ObjectUtil.notEqual(0, weResultVo.getErrCode())) {
                throw new WeComException(weResultVo.getErrCode(), WeErrorCodeEnum.parseEnum(weResultVo.getErrCode()).getErrorMsg());
            }
            weQrCode = weQrAddQuery.getWeQrCodeEntity(null, null);
        }else {
            WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();
            weContactWay.setState(qrCode.getState());
            WeAddWayVo weResultVo = qwCustomerClient.addContactWay(weContactWay).getData();
            if(Objects.isNull(weResultVo)){
                throw new WeComException("活码生成失败！");
            }
            if (ObjectUtil.notEqual(0, weResultVo.getErrCode())) {
                throw new WeComException(weResultVo.getErrCode(), WeErrorCodeEnum.parseEnum(weResultVo.getErrCode()).getErrorMsg());
            }
            weQrCode = weQrAddQuery.getWeQrCodeEntity(weResultVo.getConfigId(), weResultVo.getQrCode());
            //删除原活码
            //JSONObject delQrCodeInfo = new JSONObject();
            //员工活码类型为 1
            //delQrCodeInfo.put("type","1");
            //delQrCodeInfo.put("configId",qrCode.getConfigId());
            //rabbitTemplate.convertAndSend(mqSettingConfig.getWeQrCodeChangeEx(),mqSettingConfig.getWeQrCodeDelRk(),delQrCodeInfo.toJSONString());
            ThreadUtil.execAsync(() ->{
                WeContactWayQuery weContactWayQuery = new WeContactWayQuery();
                weContactWayQuery.setConfig_id(qrCode.getConfigId());
                qwCustomerClient.delContactWay(weContactWayQuery);
            });
        }

        if (updateById(weQrCode)) {
            //修改标签数据
            tagRelService.updateBatchByQrId(weQrCode.getId(), 1, weQrAddQuery.getQrTags());
            //修改活码范围数据
            scopeService.updateBatchByQrId(weQrCode.getId(), weQrAddQuery.getQrUserInfos());
            //修改活码素材
            attachmentsService.updateBatchByQrId(weQrCode.getId(),1, weQrAddQuery.getAttachments());
        }
        rabbitTemplate.convertAndSend(mqSettingConfig.getWeQrCodeChangeEx(),mqSettingConfig.getWeQrCodeChangeRk(),String.valueOf(weQrCode.getId()));
    }

    @Override
    public WeQrCodeDetailVo getQrDetail(Long qrId) {
        WeQrCodeDetailVo weQrCodeDetailVo = this.baseMapper.getQrDetailByQrId(qrId);
        List<WeQrScopeVo> weQrScopeVoList = scopeService.getWeQrScopeByQrIds(ListUtil.toList(qrId));
        weQrCodeDetailVo.setQrUserInfos(weQrScopeVoList);
        return weQrCodeDetailVo;
    }

    @Override
    public List<WeQrCodeDetailVo> getQrDetailByQrIds(List<Long> qrIds) {
        List<WeQrCodeDetailVo> qrDetailByQrIds = this.baseMapper.getQrDetailByQrIds(qrIds);
        List<WeQrScopeVo> weQrScopeVoList = scopeService.getWeQrScopeByQrIds(qrIds);
        Map<Long, List<WeQrScopeVo>> weQrScopeMap = Optional.ofNullable(weQrScopeVoList).orElseGet(ArrayList::new).stream().collect(Collectors.groupingBy(WeQrScopeVo::getQrId));
        for (WeQrCodeDetailVo qrCodeDetail: qrDetailByQrIds) {
            if(weQrScopeMap.get(qrCodeDetail.getId()) != null){
                qrCodeDetail.setQrUserInfos(weQrScopeMap.get(qrCodeDetail.getId()));
            }
        }
        return qrDetailByQrIds ;
    }

    @Override
    public PageInfo<WeQrCodeDetailVo> getQrCodeList(WeQrCodeListQuery qrCodeListQuery) {
        List<WeQrCodeDetailVo> weQrCodeList = new ArrayList<>();
        List<Long> qrCodeIdList = this.baseMapper.getQrCodeList(qrCodeListQuery);
        if (CollectionUtil.isNotEmpty(qrCodeIdList)) {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<WeQrCodeDetailVo> qrDetailByQrIds = this.baseMapper.getQrDetailByQrIds(qrCodeIdList);
            List<WeQrScopeVo> weQrScopeVoList = scopeService.getWeQrScopeByQrIds(qrCodeIdList);
            Map<Long, List<WeQrScopeVo>> weQrScopeMap = Optional.ofNullable(weQrScopeVoList).orElseGet(ArrayList::new).stream().collect(Collectors.groupingBy(WeQrScopeVo::getQrId));
            for (WeQrCodeDetailVo qrCodeDetail: qrDetailByQrIds) {
                if(weQrScopeMap.get(qrCodeDetail.getId()) != null){
                    qrCodeDetail.setQrUserInfos(weQrScopeMap.get(qrCodeDetail.getId()));
                }
            }
            weQrCodeList.addAll(qrDetailByQrIds);
        }
        PageInfo<Long> pageIdInfo = new PageInfo<>(qrCodeIdList);
        PageInfo<WeQrCodeDetailVo> pageInfo = new PageInfo<>(weQrCodeList);
        pageInfo.setTotal(pageIdInfo.getTotal());
        pageInfo.setPageNum(pageIdInfo.getPageNum());
        pageInfo.setPageSize(pageIdInfo.getPageSize());
        return pageInfo;
    }


    @Transactional(rollbackFor = {WeComException.class,Exception.class})
    @Override
    public void delQrCode(List<Long> qrIds) {
        List<WeQrCode> weQrCodes = this.listByIds(qrIds);
        if (CollectionUtil.isNotEmpty(weQrCodes)) {
            weQrCodes.forEach(item -> item.setDelFlag(1));
            if (this.updateBatchById(weQrCodes)) {
                //删除标签数据
                tagRelService.delBatchByQrIds(qrIds,1);
                //删除活码范围数据
                scopeService.delBatchByQrIds(qrIds);
                //删除活码素材
                attachmentsService.remove(new LambdaQueryWrapper<WeQrAttachments>()
                        .in(WeQrAttachments::getQrId,ListUtil.toList(qrIds))
                        .eq(WeQrAttachments::getBusinessType,1));;
            }
            //异步删除企微活码---最好使用mq
            weQrCodes.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getConfigId())) {
                    WeContactWayQuery weContactWayQuery = new WeContactWayQuery();
                    weContactWayQuery.setConfig_id(item.getConfigId());
                    qwCustomerClient.delContactWay(weContactWayQuery);
                }
            });
        }
    }

    @Override
    public WeQrCodeScanCountVo getWeQrCodeScanCount(WeQrCodeListQuery qrCodeListQuery) {
        WeQrCodeScanCountVo scanCountVo = new WeQrCodeScanCountVo();
        List<String> xAxis = new ArrayList<>();
        List<Integer> yAxis = new ArrayList<>();
        Long qrId = qrCodeListQuery.getQrId();
//        Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, qrCodeListQuery.getBeginTime());
//        Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, qrCodeListQuery.getEndTime());
        WeQrCode weQrCode = getById(qrId);
        Map<String, List<WeCustomer>> customerMap = new HashMap<>();

//        List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getState, weQrCode.getState()));
//        if (CollectionUtil.isNotEmpty(customerList)) {
        //累计扫码次数
        scanCountVo.setTotal(
                weCustomerService.totalScanCodeNumber(weQrCode.getState())
        );
        //今日扫码次数
        List<WeCustomerChannelCountVo> weCustomerChannelCountVos
                = weCustomerService.countCustomerChannel(weQrCode.getState(), DateUtils.getDate(), DateUtils.getDate(), null);
        if(CollectionUtil.isNotEmpty(weCustomerChannelCountVos)){
            scanCountVo.setToday(
                    weCustomerChannelCountVos.stream().findFirst().get().getCustomerNumber()
            );
        }else{
            scanCountVo.setToday(0);
        }


        weCustomerService
                .countCustomerChannel(weQrCode.getState(), qrCodeListQuery.getBeginTime(),
                        qrCodeListQuery.getEndTime(), null).stream().forEach(k->{
                    xAxis.add(k.getDate());
                    yAxis.add(k.getCustomerNumber());
                });




//            Map<String, List<WeCustomer>> listMap = customerList.stream().collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getAddTime())));
//            customerMap.putAll(listMap);
//        }
//        DateUtils.findDates(beginTime, endTime).stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
//                .forEach(date -> {
//                    xAxis.add(date);
//                    if (CollectionUtil.isNotEmpty(customerMap.get(date))) {
//                        yAxis.add(customerMap.get(date).size());
//                    } else {
//                        yAxis.add(0);
//                    }
//                });
        scanCountVo.setXAxis(xAxis);
        scanCountVo.setYAxis(yAxis);
        return scanCountVo;
    }

    /**
     * 校验排期是否重复
     *
     * @param qrUserInfos 范围参数
     */
    private void checkScope(List<WeQrUserInfoQuery> qrUserInfos) {
        List<WeQrUserInfoQuery> qrUserInfoList = qrUserInfos.stream().filter(item -> ObjectUtil.equal(2, item.getType())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(qrUserInfoList)) {
            for (int i = 0; i < qrUserInfoList.size() - 1; i++) {
                for (int j = i + 1; j < qrUserInfoList.size(); j++) {
                    int finalJ = j;
                    long userSum = qrUserInfoList.get(i).getUserIds().stream()
                            .filter(one -> qrUserInfoList.get(finalJ).getUserIds().stream()
                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                    /*long partySum = qrUserInfoList.get(i).getPartys().stream()
                            .filter(one -> qrUserInfoList.get(finalJ).getPartys().stream()
                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();*/
                    if (userSum > 0 /*|| partySum > 0*/) {
                        long workCycleSum = qrUserInfoList.get(i).getWorkCycle().stream()
                                .filter(one -> qrUserInfoList.get(finalJ).getWorkCycle().stream()
                                        .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                        if (workCycleSum > 0) {
                            String beginTime1 = qrUserInfoList.get(i).getBeginTime();
                            String endTime1 = qrUserInfoList.get(i).getEndTime();
                            String beginTime2 = qrUserInfoList.get(finalJ).getBeginTime();
                            String endTime2 = qrUserInfoList.get(finalJ).getEndTime();
                            if (match(beginTime1, endTime1, beginTime2, endTime2)) {
                                throw new WeComException("员工或部门时间有冲突!");
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean match(String startTime1, String endTime1, String startTime2, String endTime2) {
        DateTime parseStartTime1 = DateUtil.parse(startTime1, "HH:mm");
        DateTime parseEndTime1 = DateUtil.parse(endTime1, "HH:mm");

        DateTime parseStartTime2 = DateUtil.parse(startTime2, "HH:mm");
        DateTime parseEndTime2 = DateUtil.parse(endTime2, "HH:mm");
        return !(parseStartTime2.isAfter(parseEndTime1) || parseStartTime1.isAfter(parseEndTime2));
    }


    @Override
    public List<WeQrScopeVo> getWeQrScopeByTime(String formatTime, Long qrId) {
        return this.baseMapper.getWeQrScopeByTime(formatTime,qrId);
    }

    @Override
    public void deleteQrGroup(Long groupId) {
        WeQrCode weQrCode = new WeQrCode();
        weQrCode.setGroupId(1L);
        update(weQrCode,new LambdaQueryWrapper<WeQrCode>().eq(WeQrCode::getGroupId,groupId).eq(WeQrCode::getDelFlag,0));
    }

    @Override
    public WeAddWayVo createQrbyWeUserIds(List<String> weUserIds, String state) {
        return qwCustomerClient.addContactWay(WeAddWayQuery.builder().type(2).scene(2).state(state).user(weUserIds).build()).getData();
    }

    @Override
    public void updateQrbyWeUserIds(List<String> weUserIds, String configId) {
        qwCustomerClient.updateContactWay(WeAddWayQuery.builder().config_id(configId).user(weUserIds).build());
    }

    @Async
    @Override
    public void updateQrMultiplePeople(String state) {
        String stateKey = Constants.USER_CODE_KEY + state;
        try {
            //尝试加锁
            Boolean lock = redisService.tryLock(stateKey, "lock", 30L);
            if(Boolean.FALSE.equals(lock)){
                log.info("操作过于频繁，请稍后再试:{}", state);
                return;
            }
            WeQrCodeDetailVo weQrCodeDetail = this.baseMapper.getQrDetailByState(state);
            if (Objects.isNull(weQrCodeDetail)) {
                log.info("无效二维码配置ID:{}", state);
                return;
            }
            if (StringUtils.isEmpty(weQrCodeDetail.getConfigId())) {
                log.info("二维码配置ID为空:{}", state);
                return;
            }
            WeQrAddQuery weQrAddQuery = BeanUtil.copyProperties(weQrCodeDetail, WeQrAddQuery.class);
            weQrAddQuery.setQrId(weQrCodeDetail.getId());
            weQrAddQuery.setQrType(weQrCodeDetail.getType());
            List<WeQrScopeVo> weQrScopeList = scopeService.getWeQrScopeByQrIds(Collections.singletonList(weQrCodeDetail.getId()));
            // 查询企业客户
            List<WeCustomer> weCustomersList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getState, weQrCodeDetail.getState()));
            Map<String, List<WeCustomer>> customersListMap = weCustomersList.stream().collect(Collectors.groupingBy(WeCustomer::getAddUserId));

            List<WeQrUserInfoQuery> weQrUserInfoQueryList = Lists.newArrayList();
            for (WeQrScopeVo weQrScopeVo : weQrScopeList) {
                WeQrUserInfoQuery weQrUserInfoQuery = BeanUtil.copyProperties(weQrScopeVo, WeQrUserInfoQuery.class);

                List<String> userIds = Lists.newArrayList();
                List<WeQrScopeUserVo> weQrUserList = weQrScopeVo.getWeQrUserList();
                // 备用员工不参与
                List<WeQrScopeUserVo> filterWeQrUserList = weQrUserList.stream()
                        .filter(item -> Objects.equals(item.getIsSpareUser(), 0) && item.getSchedulingNum() > 0).collect(Collectors.toList());
                // 随机法打乱列表中元素的顺序
                if (Objects.equals(weQrCodeDetail.getRuleMode(), 3)) {
                    Collections.shuffle(filterWeQrUserList);
                }

                for (int i = 0; i < filterWeQrUserList.size(); i++) {
                    WeQrScopeUserVo weQrScopeUser = filterWeQrUserList.get(i);
                    List<WeCustomer> customersList = customersListMap.get(weQrScopeUser.getUserId());
                    int customersNum = CollUtil.isEmpty(customersList) ? 0 : customersList.size();
                    // 判断是否继续往下执行
                    String configIdKey = Constants.USER_CODE_KEY + state + ":" + weQrScopeUser.getUserId();
                    Integer cacheConfigId = redisService.getCacheObject(configIdKey);
                    if (null != cacheConfigId && cacheConfigId == customersNum) {
                        continue;
                    }
                    if (customersNum == 0) {
                        continue;
                    }
                    log.info("判断是否继续往下执行:{},{},{}", configIdKey, cacheConfigId, customersNum);
                    redisService.setCacheObject(configIdKey, customersNum);
                    // 缓存redis员工活码
                    String qrIdKey = Constants.USER_CODE_KEY + weQrCodeDetail.getConfigId();
                    Map<String, Integer> getCacheUserMap = redisService.getCacheMap(qrIdKey);

                    // 检验是否轮询过
                    Map<String, Integer> finalGetCacheUserMap = getCacheUserMap;
                    filterWeQrUserList.forEach(item -> {
                        if (!finalGetCacheUserMap.containsKey(item.getUserId())) {
                            Map<String, Integer> saveCacheUserMap = new HashMap<>(16);
                            saveCacheUserMap.put(item.getUserId(), item.getSchedulingNum());
                            redisService.setCacheMap(qrIdKey, saveCacheUserMap);
                        }
                    });
                    String configIdListKey = Constants.USER_CODE_KEY + weQrCodeDetail.getConfigId() + ":" + weQrScopeUser.getUserId();
                    Integer getConfigIdList = redisService.getCacheObject(configIdListKey) == null ? 0 : redisService.getCacheObject(configIdListKey);

                    // 轮询法
                    if (Objects.equals(weQrCodeDetail.getRuleMode(), 1)) {
                        getCacheUserMap = redisService.getCacheMap(qrIdKey);
                        Integer cacheSchedulingNum = getCacheUserMap.get(weQrScopeUser.getUserId());
                        // 检验是否有大于0的
                        int schedulingCount = Math.toIntExact(getCacheUserMap.entrySet().stream().filter(item -> item.getValue() >= 1).count());
                        if (cacheSchedulingNum <= 1 && schedulingCount == 1 && Objects.equals(weQrAddQuery.getOpenSpareUser(), 0)) {
                            // 检验是否轮询完一轮删除从新轮询，满足未启动备用员工
                            redisService.deleteObject(qrIdKey);
                            redisService.deleteObject(configIdListKey);
                            userIds.add(filterWeQrUserList.get(0).getUserId());
                            log.info("推送四:{},{}", state, filterWeQrUserList.get(0).getUserId());
                            break;
                        }
                        // 检验大于0的进行更新
                        if (cacheSchedulingNum >= 1 && schedulingCount > 0) {
                            Map<String, Integer> updateCacheUserMap = new HashMap<>(16);
                            updateCacheUserMap.put(weQrScopeUser.getUserId(), cacheSchedulingNum - 1);
                            redisService.setCacheMap(qrIdKey, updateCacheUserMap);
                            // 列表值大于循环次数取第一个
                            if (i > getConfigIdList && schedulingCount == 1) {
                                userIds.add(weQrScopeUser.getUserId());
                                log.info("推送一:{},{}", state, weQrScopeUser.getUserId());
                            } else if (i > getConfigIdList) {
                                userIds.add(filterWeQrUserList.get(0).getUserId());
                                log.info("推送二:{},{}", state, filterWeQrUserList.get(0).getUserId());
                            } else {
                                redisService.setCacheObject(configIdListKey, i + 1);
                                WeQrScopeUserVo weQrScopeUserVo = filterWeQrUserList.get(i + 1);
                                userIds.add(weQrScopeUserVo.getUserId());
                                log.info("推送三:{},{}", state, weQrScopeUserVo.getUserId());
                            }
                            break;
                        }
                    }

                    // 顺序法
                    if (Objects.equals(weQrCodeDetail.getRuleMode(), 2)) {
                        getCacheUserMap = redisService.getCacheMap(qrIdKey);
                        Integer cacheSchedulingNum = getCacheUserMap.get(weQrScopeUser.getUserId());
                        // 检验是否有大于0的
                        int schedulingCount = Math.toIntExact(getCacheUserMap.entrySet().stream().filter(item -> item.getValue() >= 1).count());
                        if (cacheSchedulingNum <= 1 && schedulingCount == 1 && Objects.equals(weQrAddQuery.getOpenSpareUser(), 0)) {
                            // 检验是否轮询完一轮删除从新轮询，满足未启动备用员工
                            redisService.deleteObject(qrIdKey);
                            redisService.deleteObject(configIdListKey);
                            userIds.add(filterWeQrUserList.get(0).getUserId());
                            log.info("推送四:{},{}", state, filterWeQrUserList.get(0).getUserId());
                            break;
                        }
                        // 检验大于0的进行更新
                        if (cacheSchedulingNum >= 1 && schedulingCount > 0) {
                            Map<String, Integer> updateCacheUserMap = new HashMap<>(16);
                            updateCacheUserMap.put(weQrScopeUser.getUserId(), cacheSchedulingNum - 1);
                            redisService.setCacheMap(qrIdKey, updateCacheUserMap);
                            // 列表值大于循环次数取第一个
                            if (cacheSchedulingNum > 1) {
                                redisService.setCacheObject(configIdListKey, i + 1);
                                userIds.add(weQrScopeUser.getUserId());
                                log.info("推送一:{},{}", state, weQrScopeUser.getUserId());
                            } else if (i > getConfigIdList) {
                                userIds.add(filterWeQrUserList.get(0).getUserId());
                                log.info("推送二:{},{}", state, filterWeQrUserList.get(0).getUserId());
                            } else {
                                redisService.setCacheObject(configIdListKey, i + 1);
                                WeQrScopeUserVo weQrScopeUserVo = filterWeQrUserList.get(i + 1);
                                userIds.add(weQrScopeUserVo.getUserId());
                                log.info("推送三:{},{}", state, weQrScopeUserVo.getUserId());
                            }
                            break;
                        }
                    }

                    // 随机法
                    if (Objects.equals(weQrCodeDetail.getRuleMode(), 3)) {
                        getCacheUserMap = redisService.getCacheMap(qrIdKey);
                        Integer cacheSchedulingNum = getCacheUserMap.get(weQrScopeUser.getUserId());
                        // 检验是否有大于0的
                        int schedulingCount = Math.toIntExact(getCacheUserMap.entrySet().stream().filter(item -> item.getValue() >= 1).count());
                        if (cacheSchedulingNum <= 1 && schedulingCount == 1 && Objects.equals(weQrAddQuery.getOpenSpareUser(), 0)) {
                            // 检验是否轮询完一轮删除从新轮询，满足未启动备用员工
                            redisService.deleteObject(qrIdKey);
                            redisService.deleteObject(configIdListKey);
                            userIds.add(filterWeQrUserList.get(0).getUserId());
                            log.info("推送四:{},{}", state, filterWeQrUserList.get(0).getUserId());
                            break;
                        }
                        // 检验大于0的进行更新
                        if (cacheSchedulingNum >= 1 && schedulingCount > 0) {
                            Map<String, Integer> updateCacheUserMap = new HashMap<>(16);
                            updateCacheUserMap.put(weQrScopeUser.getUserId(), cacheSchedulingNum - 1);
                            redisService.setCacheMap(qrIdKey, updateCacheUserMap);
                            // 列表值大于循环次数取第一个
                            if (i > getConfigIdList && schedulingCount == 1) {
                                userIds.add(weQrScopeUser.getUserId());
                                log.info("推送一:{},{}", state, weQrScopeUser.getUserId());
                            } else if (i > getConfigIdList) {
                                userIds.add(filterWeQrUserList.get(0).getUserId());
                                log.info("推送二:{},{}", state, filterWeQrUserList.get(0).getUserId());
                            } else {
                                redisService.setCacheObject(configIdListKey, i + 1);
                                WeQrScopeUserVo weQrScopeUserVo = filterWeQrUserList.get(i + 1);
                                userIds.add(weQrScopeUserVo.getUserId());
                                log.info("推送三:{},{}", state, weQrScopeUserVo.getUserId());
                            }
                            break;
                        }
                    }
                }
                // 是否开启备用员工 并且为 备用员工
                if (Objects.equals(weQrAddQuery.getOpenSpareUser(), 1) && CollUtil.isEmpty(userIds)) {
                    List<WeQrScopeUserVo> spareUserList = weQrUserList.stream()
                            .filter(item -> Objects.equals(item.getIsSpareUser(), 1)).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(spareUserList)) {
                        List<String> spareUserIds = spareUserList.stream().map(WeQrScopeUserVo::getUserId).collect(Collectors.toList());
                        userIds.addAll(spareUserIds);
                        log.info("推送总:{},{}", state, spareUserIds);
                    }
                }
                if (CollUtil.isNotEmpty(userIds)) {
                    weQrUserInfoQuery.setUserIds(userIds);
                    weQrUserInfoQueryList.add(weQrUserInfoQuery);
                }
            }

            // 更新联系方式
            if (CollUtil.isNotEmpty(weQrUserInfoQueryList)) {
                weQrAddQuery.setQrUserInfos(weQrUserInfoQueryList);
                WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();
                weContactWay.setState(weQrCodeDetail.getState());
                WeResultVo weResultVo = qwCustomerClient.updateContactWay(weContactWay).getData();
                if (Objects.isNull(weResultVo)) {
                    throw new WeComException("活码生成失败！");
                }
                log.info("更新多人员工活码返回:{},{}", JSON.toJSONString(weContactWay), JSON.toJSONString(weResultVo));
            }
        }catch (Exception e){
            log.error("updateQrMultiplePeople方法异常信息msg:{}",e.getMessage(),e);
        }
        finally {
            //释放锁
            redisService.unLock(stateKey, "lock");
        }
    }

    @Override
    public void qrCodeUpdateTask(Long qrCodeId) {
        List<WeQrScopeVo> weQrScopeList = getWeQrScopeByTime(DateUtil.formatDateTime(new Date()),qrCodeId);
        log.info("活码使用范围修改任务 weQrScopeList {}", JSONObject.toJSONString(weQrScopeList));
        if (CollectionUtil.isNotEmpty(weQrScopeList)) {
            Map<Long, List<WeQrScopeVo>> qrCodeMap = weQrScopeList.stream().collect(Collectors.groupingBy(WeQrScopeVo::getQrId));
            qrCodeMap.forEach((qrId,scopeList) ->{
                WeQrCodeDetailVo weQrCodeDetail = this.baseMapper.getQrDetailByQrId(qrId);
                WeQrScopeVo weCustomizeQrScope = scopeList.stream()
                        .filter(item -> ObjectUtil.equal(1, item.getType())).findFirst().orElse(null);
                if(weCustomizeQrScope != null){
                    extracted(weCustomizeQrScope,weQrCodeDetail.getConfigId());
                }else {
                    WeQrScopeVo weDefaultQrScope = scopeList.stream()
                            .filter(item -> ObjectUtil.equal(0, item.getType())).findFirst().orElse(null);
                    extracted(weDefaultQrScope,weQrCodeDetail.getConfigId());
                }
            });
        }
    }

    private void extracted(WeQrScopeVo weQrScope, String configId) {
        log.info("extracted ->>>>>>>>>>weQrScope:{}",JSONObject.toJSONString(weQrScope));
        if(ObjectUtil.equal(1,weQrScope.getStatus())){
            return;
        }
        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(null);

        if(Objects.nonNull(corpAccount)){
            List<WeQrScopeUserVo> weQrUserList = weQrScope.getWeQrUserList();
            List<WeQrScopePartyVo> weQrPartyList = weQrScope.getWeQrPartyList();
            WeAddWayQuery weContactWay = new WeAddWayQuery();
            weContactWay.setConfig_id(configId);
            weContactWay.setCorpid(corpAccount.getCorpId());
            if (StringUtils.isNotEmpty(weQrUserList)) {
                Set<String> userIds = weQrUserList.stream().map(WeQrScopeUserVo::getUserId).collect(Collectors.toSet());
                weContactWay.setUser(new ArrayList<>(userIds));
            }
            if (StringUtils.isNotEmpty(weQrPartyList)) {
                Set<Long> partyIds = weQrPartyList.stream().map(WeQrScopePartyVo::getParty).map(Long::parseLong).collect(Collectors.toSet());
                weContactWay.setParty(new ArrayList<>(partyIds));
            }
            WeResultVo data = qwCustomerClient.updateContactWay(weContactWay).getData();
            if(data != null && data.getErrCode() == 0){
                scopeService.updateScopeStatusByQrId(weQrScope.getQrId(),weQrScope.getScopeId());
            }
        }

    }

}
