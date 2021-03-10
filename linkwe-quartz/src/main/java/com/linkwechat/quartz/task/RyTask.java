package com.linkwechat.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerMessageTimeTask;
import com.linkwechat.wecom.mapper.WeCustomerMessageTimeTaskMapper;
import com.linkwechat.wecom.service.*;
import com.tencent.wework.FinanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Slf4j
@Component("ryTask")
public class RyTask {
    @Autowired
    private ElasticSearch elasticSearch;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IWeChatContactMappingService weChatContactMappingService;
    @Autowired
    private IWeSensitiveService weSensitiveService;
    @Autowired
    private IWeSensitiveActHitService weSensitiveActHitService;
    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private WeCustomerMessageTimeTaskMapper customerMessageTimeTaskMapper;

    @Autowired
    private IWeCustomerMessageService weCustomerMessageService;

    @Value("${wecome.chatKey}")
    private String chartKey;


    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }


    public void FinanceTask(String corpId, String secret) throws IOException {
        log.info("执行有参方法: params:{},{}", corpId, secret);
        //创建索引
        elasticSearch.createIndex2(chartKey, elasticSearch.getFinanceMapping());
        //从缓存中获取消息标识

        Object seqObject = Optional.ofNullable(redisCache.getCacheObject(WeConstans.CONTACT_SEQ_KEY)).orElse(0L);
        Long seqLong = Long.valueOf(String.valueOf(seqObject));
        AtomicLong index = new AtomicLong(seqLong);
        if (index.get() == 0) {
            setRedisCacheSeqValue(index);
        }

        log.info(">>>>>>>seq:{}", index.get());
        FinanceUtils.initSDK(corpId, secret);
        List<JSONObject> chatDataList = FinanceUtils.getChatData(index.get(),
                "",
                "", redisCache);
        if (CollectionUtil.isNotEmpty(chatDataList)) {
            try {
                List<JSONObject> elasticSearchEntities = weChatContactMappingService.saveWeChatContactMapping(chatDataList);
                //获取敏感行为命中信息
                weSensitiveActHitService.hitWeSensitiveAct(chatDataList);
                elasticSearch.insertBatchAsync(chartKey, elasticSearchEntities, weSensitiveService::hitSensitive);
            } catch (Exception e) {
                log.error("消息处理异常：ex:{}", e);
                e.printStackTrace();
            }
        }
    }

    public void WeCustomers() {
        //查询系统所有客户
        List<WeCustomer> cacheList = redisCache.getCacheList(WeConstans.WECUSTOMERS_KEY);
        if (CollectionUtils.isEmpty(cacheList)) {
            List<WeCustomer> customers = weCustomerService.selectWeCustomerList(null);
            redisCache.setCacheList(WeConstans.WECUSTOMERS_KEY, customers);
        } else {
            List<WeCustomer> customers = weCustomerService.selectWeCustomerList(null);
            List<WeCustomer> weCustomers = redisCache.getCacheList(WeConstans.WECUSTOMERS_KEY);
            if (CollectionUtils.isNotEmpty(weCustomers) && weCustomers.size() < customers.size()) {
                redisCache.setCacheList(WeConstans.WECUSTOMERS_KEY, customers);
            }
        }
    }

    private void setRedisCacheSeqValue(AtomicLong index) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SortBuilder<?> sortBuilderPrice = SortBuilders.fieldSort(WeConstans.CONTACT_SEQ_KEY).order(SortOrder.DESC);
        searchSourceBuilder.sort(sortBuilderPrice);
        searchSourceBuilder.size(1);
        List<JSONObject> searchResultList = elasticSearch.search(chartKey, searchSourceBuilder, JSONObject.class);
        searchResultList.stream().findFirst().ifPresent(result -> {
            index.set(result.getLong(WeConstans.CONTACT_SEQ_KEY) + 1);
        });
        redisCache.setCacheObject(WeConstans.CONTACT_SEQ_KEY, index);
    }

    /**
     * @param corpId 企业id
     * @param secret 会话密钥
     */
    public void getPermitUserList(String corpId, String secret) {
        log.info("执行有参方法: params:{},{}", corpId, secret);

    }

    /**
     * 扫描群发消息定时任务
     */
    public void messageTask() {

        //获的当前时间的毫秒数
        long currentTime = System.currentTimeMillis();
        //customerMessageTimeTaskMapper
        List<WeCustomerMessageTimeTask> weCustomerMessageTimeTasks = customerMessageTimeTaskMapper.selectWeCustomerMessageTimeTaskGteSettingTime(currentTime);

        final Semaphore semaphore = new Semaphore(5);

        if (CollectionUtils.isNotEmpty(weCustomerMessageTimeTasks)) {

            weCustomerMessageTimeTasks.forEach(

                    s -> {

                        try {

                            semaphore.acquire();

                            if (s.getMessageInfo() != null && s.getMessageId() != null || (s.getMessageInfo().getPushType().equals(WeConstans.SEND_MESSAGE_CUSTOMER)
                                    && CollectionUtils.isNotEmpty(s.getCustomersInfo())) || (s.getMessageInfo().getPushType().equals(WeConstans.SEND_MESSAGE_GROUP)
                                    && CollectionUtils.isNotEmpty(s.getGroupsInfo()))) {

                                weCustomerMessageService.sendMessgae(s.getMessageInfo(), s.getMessageId(), s.getCustomersInfo(), s.getGroupsInfo());

                                //更新消息处理状态
                                customerMessageTimeTaskMapper.updateTaskSolvedById(s.getTaskId());

                            }

                            semaphore.release();

                        } catch (JsonProcessingException | InterruptedException e) {
                            log.error("定时群发消息处理异常：ex:{}", e);
                            e.printStackTrace();
                        }
                    }

            );
        }

    }

}
