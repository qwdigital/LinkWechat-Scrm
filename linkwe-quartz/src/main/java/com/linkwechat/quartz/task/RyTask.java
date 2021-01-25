package com.linkwechat.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.service.IWeChatContactMappingService;
import com.linkwechat.wecom.service.IWeSensitiveService;
import com.tencent.wework.FinanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

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
        elasticSearch.createIndex2(WeConstans.WECOM_FINANCE_INDEX,elasticSearch.getFinanceMapping());
        //从缓存中获取消息标识

        Object seqObject = Optional.ofNullable(redisCache.getCacheObject(WeConstans.CONTACT_SEQ_KEY)).orElse(0L);
        Long seqLong = Long.valueOf(String.valueOf(seqObject));
        AtomicLong index = new AtomicLong(seqLong);
        if (index.get() == 0){
            setRedisCacheSeqValue(index);
        }

        log.info(">>>>>>>seq:{}",index.get());
        FinanceUtils.initSDK(corpId, secret);
        List<JSONObject> chatDataList = FinanceUtils.getChatData(index.get(),
                "",
                "", redisCache);
        if (CollectionUtil.isNotEmpty(chatDataList)){
            try {
                List<JSONObject> elasticSearchEntities = weChatContactMappingService.saveWeChatContactMapping(chatDataList);
                List<Consumer<List<JSONObject>>> consumerList = Lists.newArrayList();
                consumerList.add(weSensitiveService::hitSensitive);
                elasticSearch.insertBatchAsync(WeConstans.WECOM_FINANCE_INDEX, elasticSearchEntities, consumerList);
            } catch (Exception e) {
                log.error("消息处理异常：ex:{}", e);
                e.printStackTrace();
            }
        }
    }

    private void setRedisCacheSeqValue(AtomicLong index){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SortBuilder<?> sortBuilderPrice = SortBuilders.fieldSort(WeConstans.CONTACT_SEQ_KEY).order( SortOrder.DESC);
        searchSourceBuilder.sort(sortBuilderPrice);
        searchSourceBuilder.size(1);
        List<JSONObject> searchResultList = elasticSearch.search(WeConstans.WECOM_FINANCE_INDEX, searchSourceBuilder, JSONObject.class);
        searchResultList.stream().findFirst().ifPresent(result ->{
            index.set(result.getLong(WeConstans.CONTACT_SEQ_KEY) + 1);
        });
        redisCache.setCacheObject(WeConstans.CONTACT_SEQ_KEY,index);
    }

    /**
     *
     * @param corpId 企业id
     * @param secret 会话密钥
     */
    public void getPermitUserList(String corpId, String secret){
        log.info("执行有参方法: params:{},{}", corpId, secret);

    }
}
