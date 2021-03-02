package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.ConversationArchiveQuery;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.service.IWeConversationArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 会话存档业务实现类
 * @date 2020/12/19 14:00
 **/
@Slf4j
@Service
public class WeConversationArchiveServiceImpl implements IWeConversationArchiveService {
    @Autowired
    private ElasticSearch elasticSearch;

    @Value("${wecome.chatKey}")
    private String chartKey;

    /**
     * 根据用户ID 获取对应内部联系人列表
     *
     * @param query 入参
     * @return
     */
    @Override
    public PageInfo<JSONObject> getChatContactList(ConversationArchiveQuery query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum() == null ? 1 : pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize() == null ? 10 : pageDomain.getPageSize();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        int from = (pageNum - 1) * pageSize;
        builder.size(pageSize);
        builder.from(from);
        builder.sort("msgtime", SortOrder.ASC);
        BoolQueryBuilder fromBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", ""))
                .must(QueryBuilders.matchQuery("from", query.getFromId()))
                .must(QueryBuilders.matchQuery("tolist.keyword", query.getReceiveId()));

        BoolQueryBuilder toLsitBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", ""))
                .must(QueryBuilders.matchQuery("from", query.getReceiveId()))
                .must(QueryBuilders.matchQuery("tolist.keyword", query.getFromId()));
        //查询聊天类型
        if (StringUtils.isNotEmpty(query.getMsgType())) {
            fromBuilder.must(QueryBuilders.termQuery("msgtype", query.getMsgType()));
            toLsitBuilder.must(QueryBuilders.termQuery("msgtype", query.getMsgType()));
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(fromBuilder)
                .should(toLsitBuilder)
                .minimumShouldMatch(1);
        //时间范围查询
        if (StringUtils.isNotEmpty(query.getBeginTime()) && StringUtils.isNotEmpty(query.getEndTime())) {
            Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD,query.getBeginTime());
            Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD,query.getEndTime());
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("msgtime").gte(beginTime).lte(endTime));
        }
        builder.query(boolQueryBuilder);
        return elasticSearch.searchPage(chartKey, builder, pageNum, pageSize, JSONObject.class);
    }

    @Override
    public PageInfo<JSONObject> getChatRoomContactList(ConversationArchiveQuery query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum() == null ? 1 : pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize() == null ? 10 : pageDomain.getPageSize();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        int from = (pageNum - 1) * pageSize;
        builder.size(pageSize);
        builder.from(from);
        builder.sort("msgtime", SortOrder.ASC);

        BoolQueryBuilder fromBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", query.getRoomId()))
                .must(QueryBuilders.matchQuery("from", query.getFromId()));

        BoolQueryBuilder roomidBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", query.getRoomId()))
                .must(QueryBuilders.matchQuery("tolist.keyword", query.getFromId()));

        //查询聊天类型
        if (StringUtils.isNotEmpty(query.getMsgType())) {
            fromBuilder.must(QueryBuilders.termQuery("msgtype", query.getMsgType()));
            roomidBuilder.must(QueryBuilders.termQuery("msgtype", query.getMsgType()));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(fromBuilder)
                .should(roomidBuilder);

        //时间范围查询
        if (StringUtils.isNotEmpty(query.getBeginTime()) && StringUtils.isNotEmpty(query.getEndTime())) {
            Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD,query.getBeginTime());
            Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD,query.getEndTime());
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("msgtime").gte(beginTime).lte(endTime));
        }

        builder.query(boolQueryBuilder);
        return elasticSearch.searchPage(chartKey, builder, pageNum, pageSize, JSONObject.class);
    }


    @Override
    public JSONObject getFinalChatContactInfo(String fromId, String receiveId) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.sort("msgtime", SortOrder.ASC);
        builder.size(1);
        BoolQueryBuilder fromBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", ""))
                .must(QueryBuilders.matchQuery("from", fromId))
                .must(QueryBuilders.matchQuery("tolist.keyword", receiveId));

        BoolQueryBuilder toLsitBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", ""))
                .must(QueryBuilders.matchQuery("from", receiveId))
                .must(QueryBuilders.matchQuery("tolist.keyword", fromId));
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(fromBuilder)
                .should(toLsitBuilder)
                .minimumShouldMatch(1);
        builder.query(boolQueryBuilder);
        List<JSONObject> resultList = elasticSearch.search(chartKey, builder, JSONObject.class);
        if (CollectionUtil.isNotEmpty(resultList)) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public JSONObject getFinalChatRoomContactInfo(String fromId, String roomId) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.sort("msgtime", SortOrder.ASC);
        builder.size(1);
        BoolQueryBuilder fromBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", roomId))
                .must(QueryBuilders.matchQuery("from", fromId));

        BoolQueryBuilder roomidBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("roomid", roomId))
                .must(QueryBuilders.matchQuery("tolist.keyword", fromId));
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(fromBuilder)
                .should(roomidBuilder);
        builder.query(boolQueryBuilder);
        List<JSONObject> resultList = elasticSearch.search(chartKey, builder, JSONObject.class);
        if (CollectionUtil.isNotEmpty(resultList)) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PageInfo<JSONObject> getChatAllList(ConversationArchiveQuery query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum() == null ? 1 : pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize() == null ? 10 : pageDomain.getPageSize();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        int from = (pageNum - 1) * pageSize;
        builder.size(pageSize);
        builder.from(from);
        builder.sort("msgtime", SortOrder.ASC);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("msgtype", "text"));
        //成员姓名查询
        if(StringUtils.isNotEmpty(query.getUserName())){
            boolQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("fromInfo.name",query.getUserName()))
                    .mustNot(QueryBuilders.existsQuery("fromInfo.externalUserid")));
       }
        //客户姓名查询
        if(StringUtils.isNotEmpty(query.getCustomerName())){
            boolQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("fromInfo.name",query.getCustomerName()))
                    .must(QueryBuilders.existsQuery("fromInfo.externalUserid")));
        }

        //消息动作
        if(StringUtils.isNotEmpty(query.getAction())){
            boolQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("action",query.getAction())));
        }

        //关键词查询并高亮显示
        if (StringUtils.isNotEmpty(query.getKeyWord())){
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(query.getKeyWord(),"text.content"));
            builder.highlighter(new HighlightBuilder().field("text.content"));
        }

        //时间范围查询
        if (StringUtils.isNotEmpty(query.getBeginTime()) && StringUtils.isNotEmpty(query.getEndTime())) {
            Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD,query.getBeginTime());
            Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD,query.getEndTime());
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("msgtime").gte(beginTime).lte(endTime));
        }
        builder.query(boolQueryBuilder);
        PageInfo<JSONObject> pageInfo = elasticSearch.searchPage(chartKey, builder, pageNum, pageSize, JSONObject.class);
        return pageInfo;
    }
}
