package com.linkwechat.wecom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.wecom.service.IWeConversationArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sxw
 * @description 会话存档业务实现类
 * @date 2020/12/19 14:00
 **/
@Slf4j
@Service
public class WeConversationArchiveServiceImpl implements IWeConversationArchiveService {
    @Autowired
    private ElasticSearch elasticSearch;

    /**
     * 根据用户ID 获取对应内部联系人列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<JSONObject> getInternalContactList(String userId) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(1000);
        builder.sort("msgtime", SortOrder.ASC);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("tolist.keyword", userId))
                .should(QueryBuilders.termQuery("from",userId))
                .minimumShouldMatch(1)
                .must(QueryBuilders.termQuery("type",1))
                .must(QueryBuilders.termQuery("roomid", ""));
        builder.query(boolQueryBuilder);
        List<JSONObject> searchList = elasticSearch.search(WeConstans.WECOM_FINANCE_INDEX, builder, JSONObject.class);
        Map<String, List<JSONObject>> from = Optional.ofNullable(searchList)
                .orElseGet(ArrayList::new).stream()
                .collect(Collectors.groupingBy(item -> item.getString("from")));
        return null;
    }
}
