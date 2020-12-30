package com.linkwechat.common.core.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sxw
 * @description es工具类
 * @date 2020/12/9 14:02
 **/
@Slf4j
@Component
public class ElasticSearch {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * @param idxName 索引名称
     * @param idxSQL  索引描述
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:30
     * @since
     */
    public void createIndex(String idxName, String idxSQL) {
        try {
            if (!this.indexExist(idxName)) {
                //log.error(" idxName={} 已经存在,idxSql={}", idxName, idxSQL);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(idxSQL, XContentType.JSON);
//            request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param idxName 索引名称
     * @param builder 索引描述
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:30
     * @since
     */
    public void createIndex2(String idxName, XContentBuilder builder) {
        try {
            if (!this.indexExist(idxName)) {
                //log.error(" idxName={} 已经存在,idxSql={}", idxName, builder);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(builder);
//            request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断某个index是否存在
     *
     * @param idxName index名
     * @return boolean
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:27
     * @since
     */
    public boolean indexExist(String idxName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 断某个index是否存在
     *
     * @param idxName index名
     * @return boolean
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:27
     * @since
     */
    public boolean isExistsIndex(String idxName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(idxName), RequestOptions.DEFAULT);
    }

    /**
     * 设置分片
     *
     * @param request
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 19:27
     * @since
     */
    public void buildSetting(CreateIndexRequest request) {
        request.settings(Settings.builder().put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
    }

    /**
     * @param idxName index
     * @param entity  对象
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:27
     * @since
     */
    public void insertOrUpdateOne(String idxName, ElasticSearchEntity entity) {
        IndexRequest request = new IndexRequest(idxName, "_doc");
        log.error("Data : id={},entity={}", entity.getId(), JSON.toJSONString(entity.getData()));
        request.id(entity.getId());
        request.source(entity.getData(), XContentType.JSON);
//        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 批量插入数据
     *
     * @param idxName index
     * @param list    带插入列表
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:26
     * @since
     */
    public void insertBatch(String idxName, List<ElasticSearchEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(idxName, "_doc").id(item.getId())
                .source(item.getData(), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量删除
     *
     * @param idxName index
     * @param idList  待删除列表
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:14
     * @since
     */
    public <T> void deleteBatch(String idxName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, "_doc", item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param idxName index
     * @param builder 查询参数
     * @param c       结果类对象
     * @return java.util.List<T>
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:14
     * @since
     */
    public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            long totalHits = response.getHits().getTotalHits().value;
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> PageInfo<T> searchPage(String idxName, SearchSourceBuilder builder, int pageNum, int pageSize, Class<T> c) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            int totalHits =(int) response.getHits().getTotalHits().value;
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            // 封装分页
            PageHelper.startPage(1,2);

            PageInfo<T> page = new PageInfo<>();
            page.setList(res);
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            page.setTotal(totalHits);
            page.setPages(totalHits == 0 ? 0 : (totalHits % pageNum == 0 ? totalHits / pageNum : (totalHits / pageNum) + 1));
            page.setHasNextPage(page.getPageNum() < page.getPages());
            return page;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除index
     *
     * @param idxName
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:13
     * @since
     */
    public void deleteIndex(String idxName) {
        try {
            if (!this.indexExist(idxName)) {
                //log.error(" idxName={} 已经存在", idxName);
                return;
            }
            restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param idxName
     * @param builder
     * @return void
     * @throws
     * @author danmo
     * @See
     * @date 2019/10/17 17:13
     * @since
     */
    public void deleteByQuery(String idxName, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public XContentBuilder getFinanceMapping() throws IOException {
        // 创建 会话文本Mapping
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("msgid")
                .field("type", "keyword")
                .endObject()
                .startObject("seq")
                .field("type", "long")
                .endObject()
                .startObject("action")
                .field("type", "keyword")
                .endObject()
                .startObject("from")
                .field("type", "keyword")
                .endObject()
                .startObject("roomid")
                .field("type", "keyword")
                .endObject()
                .startObject("msgtime")
                .field("type", "long")
                .endObject()
                .startObject("msgtype")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject();
        return xContentBuilder;
    }
}
