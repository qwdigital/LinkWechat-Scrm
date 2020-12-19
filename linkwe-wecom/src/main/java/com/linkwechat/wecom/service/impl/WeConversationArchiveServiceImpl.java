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
    public PageInfo<JSONObject> getInternalContactList(String userId, int pageSize, int pageNum) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        int from = (pageSize - 1) * pageNum;
        builder.size(pageNum);
        builder.from(from);
        builder.sort("msgtime", SortOrder.ASC);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("tolist.keyword", userId))
                .must(QueryBuilders.termQuery("roomid", ""));
        builder.query(boolQueryBuilder);
        return elasticSearch.searchPage(WeConstans.WECOM_FINANCE_INDEX, builder, pageNum, pageSize, JSONObject.class);
    }
}
