package com.linkwechat.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.utils.StringUtils;
import com.tencent.wework.FinanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
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
        AtomicLong index = new AtomicLong(0);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SortBuilder<?> sortBuilderPrice = SortBuilders.fieldSort("seq").order( SortOrder.DESC);
        searchSourceBuilder.sort(sortBuilderPrice);
        List<JSONObject> searchResultList = elasticSearch.search(WeConstans.WECOM_FINANCE_INDEX, searchSourceBuilder, JSONObject.class);
        searchResultList.stream().findFirst().ifPresent(result ->{
            index.set(result.getLong("seq")+1);
        });
        if (index.get() == 0) {
            elasticSearch.createIndex2(WeConstans.WECOM_FINANCE_INDEX,elasticSearch.getFinanceMapping());
        }
        log.info("执行有参方法: params:{},{}", corpId, secret);

        FinanceUtils.initSDK(corpId, secret);
        List<ElasticSearchEntity> chatDataList = FinanceUtils.getChatData(index.get(), "", "");

        if (CollectionUtil.isNotEmpty(chatDataList)){
            try {
                elasticSearch.insertBatch(WeConstans.WECOM_FINANCE_INDEX, chatDataList);
            } catch (Exception e) {
                log.error("消息处理异常：ex:{}", e);
                e.printStackTrace();
            }
            index.incrementAndGet();
        }
    }
}
