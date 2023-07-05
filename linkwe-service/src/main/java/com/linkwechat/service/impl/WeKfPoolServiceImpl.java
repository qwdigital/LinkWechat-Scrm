package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.query.WeKfAddMsgQuery;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfEvaluationVo;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.kf.vo.WeKfRecordListVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.domain.wecom.query.kf.WeKfStateQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfStateVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeKfPoolMapper;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfPoolService;
import com.linkwechat.service.IWeKfServicerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客服接待池表(WeKfPool)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
@Slf4j
@Service
public class WeKfPoolServiceImpl extends ServiceImpl<WeKfPoolMapper, WeKfPool> implements IWeKfPoolService {

    @Resource
    private QwKfClient qwKfClient;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Autowired
    private IWeKfServicerService weKfServicerService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Value("wecom.kf.cycle-num.key:we-kf-cycle-num-key-{}-{}")
    private String cycleNumKey;

    @Override
    public WeKfPool getKfPoolInfo(String corpId, String openKfId, String externalUserId) {
        return getOne(new LambdaQueryWrapper<WeKfPool>()
                .eq(WeKfPool::getCorpId,corpId)
                .eq(WeKfPool::getOpenKfId, openKfId)
                .eq(WeKfPool::getExternalUserId, externalUserId)
                .eq(WeKfPool::getDelFlag, 0)
                .last("limit 1"));
    }


    //@KfNotice
    @Override
    public void updateKfPoolInfo(WeKfPool weKfPool) {
        update(weKfPool, new LambdaQueryWrapper<WeKfPool>()
                .eq(WeKfPool::getCorpId, weKfPool.getCorpId())
                .eq(WeKfPool::getOpenKfId, weKfPool.getOpenKfId())
                .eq(WeKfPool::getExternalUserId, weKfPool.getExternalUserId())
                .eq(WeKfPool::getDelFlag, 0));
    }

    @Override
    public void updateKfServicer(String corpId, String openKfId, String externalUserId, String userId) {
        WeKfPool weKfPool = new WeKfPool();
        WeKfPool kfPoolInfo = getKfPoolInfo(corpId, openKfId, externalUserId);
        BeanUtil.copyProperties(kfPoolInfo, weKfPool);
        kfPoolInfo.setDelFlag(1);
        kfPoolInfo.setSessionEndTime(new Date());
        updateById(kfPoolInfo);

        weKfPool.setSessionStartTime(new Date());
        saveKfPoolInfo(weKfPool);
    }

    @Override
    public void saveKfPoolInfo(WeKfPool weKfPool) {
        save(weKfPool);
    }

    @Override
    public WeKfPool getLastServicer(String corpId, String openKfId, String externalUserId) {
        List<WeKfPool> list = list(new LambdaQueryWrapper<WeKfPool>()
                .eq(WeKfPool::getCorpId, corpId)
                .eq(WeKfPool::getOpenKfId, openKfId)
                .eq(WeKfPool::getExternalUserId, externalUserId)
                .eq(WeKfPool::getDelFlag, 1)
                .orderByDesc(WeKfPool::getCreateTime));
        List<WeKfPool> resultList = Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .filter(item -> StringUtils.isNotEmpty(item.getUserId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(resultList)) {
            return list.get(0);
        } else {
            return null;
        }
    }


    @Override
    public String allocationServicer(String corpId, String openKfId, String externalUserId, String userId, Integer status) {
        Date date = new Date();
        WeKfPool weKfPool = new WeKfPool();
        weKfPool.setCorpId(corpId);
        weKfPool.setStatus(status);
        weKfPool.setOpenKfId(openKfId);
        weKfPool.setExternalUserId(externalUserId);

        WeKfStateQuery stateQuery = new WeKfStateQuery();
        if (StringUtils.isNotEmpty(userId)) {
            stateQuery.setServicer_userid(userId);
            weKfPool.setUserId(userId);
            weKfPool.setReceptionTime(date);
        }
        if (ObjectUtil.equal(WeKfStatusEnum.AI_RECEPTION.getType(), status)
                || ObjectUtil.equal(WeKfStatusEnum.SERVICER.getType(), status)) {
            weKfPool.setSessionStartTime(date);
        }
        stateQuery.setCorpid(corpId);
        stateQuery.setService_state(status);
        stateQuery.setOpen_kfid(openKfId);
        stateQuery.setExternal_userid(externalUserId);
        WeKfStateVo kfStateVo = qwKfClient.updateSessionStatus(stateQuery).getData();
        if (kfStateVo != null && StringUtils.isNotEmpty(kfStateVo.getMsgCode())) {
            weKfPool.setMsgCode(kfStateVo.getMsgCode());
            if (ObjectUtil.equal(WeKfStatusEnum.STAR_OR_END.getType(), status)) {
                weKfPool.setSessionEndTime(date);
                weKfPool.setDelFlag(1);
            }
        }
        //更新接待信息
        SpringUtils.getBean(IWeKfPoolService.class).updateKfPoolInfo(weKfPool);
        return weKfPool.getMsgCode();
    }

    @Override
    public int getReceptionCnt(String corpId, String openKfId, String userId) {
        return count(new LambdaQueryWrapper<WeKfPool>()
                .eq(WeKfPool::getCorpId, corpId)
                .eq(WeKfPool::getOpenKfId, openKfId)
                .eq(WeKfPool::getUserId, userId)
                .eq(WeKfPool::getDelFlag, 0));
    }

    @Override
    public List<Map<String, Object>> getReceptionGroupByCnt(String corpId, String openKfId, String externalUserId, List<String> userIds) {
        QueryWrapper<WeKfPool> wrapper = new QueryWrapper<>();
        wrapper.select("user_id as userId", "count(1) as num")
                .eq("corp_id", corpId)
                .eq("open_kf_id", openKfId)
                .eq("external_userid", externalUserId)
                .in("user_id", userIds)
                .eq("del_flag", 0)
                .groupBy("user_id");
        List<Map<String, Object>> listMaps = listMaps(wrapper);
        return listMaps;
    }


    @Override
    public void transferReceptionPoolCustomer(String corpId, String openKfId, String externalUserId) {
        WeKfPool weKfPool = getOne(new LambdaQueryWrapper<WeKfPool>()
                .eq(WeKfPool::getCorpId, corpId)
                .eq(WeKfPool::getOpenKfId, openKfId)
                .eq(WeKfPool::getDelFlag, 0)
                .eq(WeKfPool::getStatus, WeKfStatusEnum.ACCESS_POOL.getType())
                .orderByAsc(WeKfPool::getEnterTime)
                .last("limit 1"));
        if (weKfPool == null) {
            return;
        }
        transServiceHandler(corpId, openKfId, weKfPool.getExternalUserId());
    }

    @Override
    public void transServiceHandler(String corpId, String openKfId, String externalUserId) {
        transServiceHandler(corpId, openKfId, externalUserId, true);
    }

    /**
     * 转人工处理
     *
     * @param corpId
     * @param openKfId       客服ID
     * @param externalUserId 客户id
     */
    @Override
    public void transServiceHandler(String corpId, String openKfId, String externalUserId, Boolean isAI) {
        WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(corpId, openKfId);
        if (weKfInfo == null) {
            return;
        }
        //接待方式: 1-人工客服 2-智能助手
        Integer receptionType = weKfInfo.getReceptionType();
        if (isAI && ObjectUtil.equal(2, receptionType)) {
            //转智能助手
            allocationServicer(corpId, openKfId
                    , externalUserId, null, WeKfStatusEnum.AI_RECEPTION.getType());
            return;
        }
        //是否优先分配: 1-否 2-是
        Integer isPriority = weKfInfo.getIsPriority();

        List<WeKfUser> kfUserIdList = weKfServicerService.getKfUserIdList(weKfInfo.getId());
        redisService.setCacheObject(StringUtils.format(cycleNumKey,corpId, openKfId),kfUserIdList.size());
        if (ObjectUtil.equal(2, isPriority)) {
            //成功优先分配后返回
            if (isPriority(corpId, openKfId, externalUserId)) {
                return;
            }
        }
        if (!isPolling(corpId,openKfId, externalUserId)) {
            //放入接待池
            String msgCode = allocationServicer(corpId, openKfId
                    , externalUserId, null, WeKfStatusEnum.ACCESS_POOL.getType());
            //排队提醒: 1-开启 2-关闭
            Integer queueNotice = weKfInfo.getQueueNotice();
            if (ObjectUtil.equal(1, queueNotice)) {
                String queueNoticeContent = weKfInfo.getQueueNoticeContent();
                JSONObject msg = new JSONObject();
                msg.put("content", queueNoticeContent);
                WeKfMsgQuery kfMsgQuery = new WeKfMsgQuery();
                kfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                kfMsgQuery.setCode(msgCode);
                kfMsgQuery.setContext(msg);
                qwKfClient.sendMsgOnEvent(kfMsgQuery);
            }
        }
    }


    //判断是否优先分配
    private Boolean isPriority(String corpId, String openKfId, String externalUserId) {
        WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(corpId, openKfId);
        if (weKfInfo == null) {
            return false;
        }
        WeKfPool lastServicer = getLastServicer(corpId, openKfId, externalUserId);
        if (lastServicer != null) {
            //判断上次接待人员是否还是接待人员并且还在接待状态
            boolean isKfServicer = weKfInfo.getUserIdList().stream()
                    .filter(item -> ObjectUtil.equal(1,item.getStatus())).map(WeKfUser::getUserId)
                    .anyMatch(item -> item.equals(lastServicer.getUserId()));
            //接待限制
            Integer receiveLimit = weKfInfo.getReceiveLimit();
            int receptionNum = getReceptionCnt(corpId, openKfId, lastServicer.getUserId());
            //满足条件后转人工接待
            if (isKfServicer && receptionNum <= receiveLimit) {
                allocationServicer(corpId, openKfId
                        , externalUserId, lastServicer.getUserId(), WeKfStatusEnum.SERVICER.getType());
                return true;
            }
        }
        return false;
    }


    /**
     * 客服接待人员轮询或空闲
     *
     * @return
     */
    private Boolean isPolling(String corpId, String openKfId, String externalUserId) {
        Integer cycleNum =(Integer) redisService.getCacheObject(StringUtils.format(cycleNumKey,corpId, openKfId));
        if (cycleNum <= 0) {
            return false;
        }
        WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(corpId, openKfId);
        if (weKfInfo == null) {
            return false;
        }
        //分配方式: 1-轮流 2-空闲
        Integer allocationWay = weKfInfo.getAllocationWay();
        //接待限制
        Integer receiveLimit = weKfInfo.getReceiveLimit();

        List<WeKfUser> kfUserList = weKfServicerService.getKfUserIdList(weKfInfo.getId());
        //轮流分配
        if (ObjectUtil.equal(1, allocationWay)) {

            String pollingKey = StringUtils.format(WeConstans.KF_SERVICER_POLLING_KEY,corpId, openKfId);
            //获取客服接待人员列表
            Integer pollingNum = redisService.keyIsExists(pollingKey) ? redisService.getCacheObject(pollingKey) : 0;

            if (pollingNum >= Integer.MAX_VALUE) {
                pollingNum = 0;
                redisService.setCacheObject(pollingKey, 0L);
            }
            int idx = pollingNum % kfUserList.size();
            WeKfUser weKfUser = kfUserList.get(idx < 0 ? -idx : idx);
            int receptionNum = getReceptionCnt(corpId, openKfId, weKfUser.getUserId());
            redisService.increment(pollingKey);
            if (ObjectUtil.equal(1,weKfUser.getStatus()) && receptionNum < receiveLimit) {
                allocationServicer(corpId, openKfId
                        , externalUserId, weKfUser.getUserId(), WeKfStatusEnum.SERVICER.getType());
            } else {
                redisService.decrement(StringUtils.format(cycleNumKey,corpId, openKfId));
                return isPolling(corpId, openKfId, externalUserId);
            }
        } else {
            String userId = null;
            //空闲分配
            List<String> servicerList = kfUserList.stream()
                    .filter(servicer -> servicer.getStatus().equals(1))
                    .map(WeKfUser::getUserId).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(servicerList)){
                return false;
            }
            List<Map<String, Object>> receptionCntList = getReceptionGroupByCnt(corpId, openKfId, externalUserId, servicerList);

            //当前全部空闲
            if (CollectionUtil.isEmpty(receptionCntList)) {
                //随机取一个
                Collections.shuffle(servicerList);
                userId = servicerList.get(0);
            }
            //当前部分空闲
            else if(ObjectUtil.notEqual(servicerList.size(),receptionCntList.size())){
                List<String> serverUserIds = receptionCntList.stream().map(Map::keySet).flatMap(Collection::stream).collect(Collectors.toList());
                List<String> subList = CollectionUtil.subtractToList(servicerList, serverUserIds);
                Collections.shuffle(subList);
                userId = subList.get(0);
            } else {
                Map<String, Object> receptionCnt = receptionCntList.stream().min(Comparator.comparingInt(item -> (Integer) item.get("num"))).get();
                Integer num = (Integer) receptionCnt.get("num");
                userId = (String) receptionCnt.get("userId");
                if (num >= receiveLimit) {
                    return false;
                }
            }

            allocationServicer(corpId, openKfId
                    , externalUserId, userId, WeKfStatusEnum.SERVICER.getType());
        }
        return true;
    }


    @Override
    public List<WeKfRecordListVo> getRecordList(WeKfRecordQuery query) {
        List<WeKfRecordListVo> recordList = this.baseMapper.getRecordList(query);
        if(CollectionUtil.isNotEmpty(recordList)){

            Map<String, String> userId2NameMap = new HashMap<>();
            Map<String, String> externalUserIdMap = new HashMap<>();

            Set<String> userIdSet = recordList.stream().map(WeKfRecordListVo::getUserId).collect(Collectors.toSet());
            if(CollectionUtil.isNotEmpty(userIdSet)){
                SysUserQuery userQuery = new SysUserQuery();
                userQuery.setWeUserIds(new ArrayList<>(userIdSet));
                try {
                    List<SysUserVo> sysUserList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                    if(CollectionUtil.isNotEmpty(sysUserList)){
                        Map<String, String> userMap = sysUserList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName, (key1, key2) -> key2));
                        userId2NameMap.putAll(userMap);
                    }
                } catch (Exception e) {
                    log.error("getRecordList 换取用户名称失败：userQuery：{}",JSONObject.toJSONString(userQuery),e);
                }
            }
            Set<String> externalUserIdSet = recordList.stream().map(WeKfRecordListVo::getExternalUserId).collect(Collectors.toSet());
            if(CollectionUtil.isNotEmpty(externalUserIdSet)){
                List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().select(WeCustomer::getExternalUserid).in(WeCustomer::getExternalUserid, externalUserIdSet).eq(WeCustomer::getDelFlag,0));
                if(CollectionUtil.isNotEmpty(customerList)){
                    Map<String, String> tempExternalUserIdMap = customerList.stream().collect(Collectors.toMap(WeCustomer::getExternalUserid, WeCustomer::getExternalUserid, (key1, key2) -> key2));
                    externalUserIdMap.putAll(tempExternalUserIdMap);
                }
            }
            for (WeKfRecordListVo weKfRecord : recordList) {
                if(externalUserIdMap.containsKey(weKfRecord.getExternalUserId())){
                    weKfRecord.setIsQyCustomer(0);
                }
                if(userId2NameMap.containsKey(weKfRecord.getUserId())){
                    weKfRecord.setUserName(userId2NameMap.get(weKfRecord.getUserId()));
                }
            }
        }
        return recordList;
    }


    @Override
    public WeKfEvaluationVo getEvaluation(WeKfAddMsgQuery query) {
        WeKfEvaluationVo weKfEvaluation = new WeKfEvaluationVo();
        WeKfPool weKfPool = getById(query.getPoolId());
        if(Objects.nonNull(weKfPool)){
            weKfEvaluation.setEvaluationType(weKfPool.getEvaluationType());
            weKfEvaluation.setContent(weKfPool.getEvaluation());
        }
        return weKfEvaluation;
    }

    @Override
    public void addEvaluation(WeKfAddMsgQuery query) {
        WeKfPool weKfPool = new WeKfPool();
        weKfPool.setId(Long.valueOf(query.getPoolId()));
        weKfPool.setEvaluationType(query.getEvaluationType());
        weKfPool.setEvaluation(query.getContent());
        updateById(weKfPool);
    }

    @Override
    public WeKfCustomerStat getKfCustomerStat(String dateTime) {
       return this.baseMapper.getKfCustomerStat(dateTime);
    }

    @Override
    public List<WeKfUserStat> getKfUserStat(String dateTime) {
        return this.baseMapper.getKfUserStat(dateTime);
    }

}
