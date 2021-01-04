package com.linkwechat.wecom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeSensitive;
import com.linkwechat.wecom.domain.WeSensitiveAuditScope;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.query.WeSensitiveHitQuery;
import com.linkwechat.wecom.mapper.WeSensitiveMapper;
import com.linkwechat.wecom.service.IWeSensitiveAuditScopeService;
import com.linkwechat.wecom.service.IWeSensitiveService;
import com.linkwechat.wecom.service.IWeUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 敏感词设置Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-29
 */
@Service
@Slf4j
public class WeSensitiveServiceImpl implements IWeSensitiveService {
    @Autowired
    private WeSensitiveMapper weSensitiveMapper;

    @Autowired
    private IWeSensitiveAuditScopeService sensitiveAuditScopeService;

    @Autowired
    private ElasticSearch elasticSearch;

    @Autowired
    private IWeUserService weUserService;

    /**
     * 查询敏感词设置
     *
     * @param id 敏感词设置ID
     * @return 敏感词设置
     */
    @Override
    public WeSensitive selectWeSensitiveById(Long id) {
        return weSensitiveMapper.selectWeSensitiveById(id);
    }

    /**
     * 查询敏感词设置列表
     *
     * @param weSensitive 敏感词设置
     * @return 敏感词设置
     */
    @Override
    public List<WeSensitive> selectWeSensitiveList(WeSensitive weSensitive) {
        return weSensitiveMapper.selectWeSensitiveList(weSensitive);
    }

    /**
     * 新增敏感词设置
     *
     * @param weSensitive 敏感词设置
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWeSensitive(WeSensitive weSensitive) {
        weSensitive.setCreateBy(SecurityUtils.getUsername());
        weSensitive.setCreateTime(DateUtils.getNowDate());
        int insertResult = weSensitiveMapper.insertWeSensitive(weSensitive);
        if (insertResult > 0) {
            if (CollectionUtils.isNotEmpty(weSensitive.getAuditUserScope())) {
                if (weSensitive.getId() != null) {
                    weSensitive.getAuditUserScope().forEach(scope -> {
                        scope.setSensitiveId(weSensitive.getId());
                    });
                    sensitiveAuditScopeService.insertWeSensitiveAuditScopeList(weSensitive.getAuditUserScope());
                }
            }
        }
        return insertResult;
    }

    /**
     * 修改敏感词设置
     *
     * @param weSensitive 敏感词设置
     * @return 结果
     */
    @Override
    @Transactional
    public int updateWeSensitive(WeSensitive weSensitive) {
        weSensitive.setUpdateBy(SecurityUtils.getUsername());
        weSensitive.setUpdateTime(DateUtils.getNowDate());
        int updateResult = weSensitiveMapper.updateWeSensitive(weSensitive);
        if (updateResult > 0 && weSensitive.getAuditUserScope() != null) {
            //删除原有关联信息
            sensitiveAuditScopeService.deleteAuditScopeBySensitiveId(weSensitive.getId());
            if (CollectionUtils.isNotEmpty(weSensitive.getAuditUserScope())) {
                weSensitive.getAuditUserScope().forEach(scope -> {
                    scope.setSensitiveId(weSensitive.getId());
                });
                sensitiveAuditScopeService.insertWeSensitiveAuditScopeList(weSensitive.getAuditUserScope());
            }
        }
        return updateResult;
    }

    /**
     * 批量删除敏感词设置
     *
     * @param ids 需要删除的敏感词设置ID
     * @return 结果
     */
    @Override
    public int deleteWeSensitiveByIds(Long[] ids) {
        List<WeSensitive> sensitiveList = weSensitiveMapper.selectWeSensitiveByIds(ids);
        sensitiveList.forEach(sensitive -> {
            sensitive.setDelFlag(1);
            sensitive.setUpdateBy(SecurityUtils.getUsername());
            sensitive.setUpdateTime(DateUtils.getNowDate());
        });
        return weSensitiveMapper.batchUpdateWeSensitive(sensitiveList);
    }

    /**
     * 批量删除敏感词配置数据
     *
     * @param ids 敏感词设置ID
     * @return 结果
     */
    @Override
    @Transactional
    public int destroyWeSensitiveByIds(Long[] ids) {
        int deleteResult = weSensitiveMapper.deleteWeSensitiveByIds(ids);
        if (deleteResult > 0) {
            //删除关联数据
            sensitiveAuditScopeService.deleteAuditScopeBySensitiveIds(ids);
        }
        return deleteResult;
    }

    @Override
    public void hitSensitive(List<JSONObject> entityList) {
        //获取所有的敏感词规则
        List<WeSensitive> allSensitiveRules = weSensitiveMapper.selectWeSensitiveList(new WeSensitive());
        //根据规则过滤命中
        if (CollectionUtils.isNotEmpty(allSensitiveRules)) {
            allSensitiveRules.parallelStream().forEach(weSensitive -> {
                List<JSONObject> jsonList = Lists.newArrayList();
                List<String> patternWords = Arrays.asList(weSensitive.getPatternWords().split(","));
                List<String> users = getScopeUsers(weSensitive.getAuditUserScope());
                jsonList.addAll(hitSensitiveInES(patternWords, users));
                //将命中结果插入es
                try {
                    addHitSensitiveList(jsonList, weSensitive);
                } catch (IOException e) {
                    log.warn("添加敏感词命中信息失败, jsonList={}, auditUserId={}, exception={}",
                            jsonList, weSensitive.getAuditUserId(), ExceptionUtils.getStackTrace(e));
                }
            });
        }
    }

    @Override
    public PageInfo<JSONObject> getHitSensitiveList(WeSensitiveHitQuery weSensitiveHitQuery) {
        List<String> userIds = Lists.newArrayList();
        if (weSensitiveHitQuery.getScopeType().equals(WeConstans.USE_SCOP_BUSINESSID_TYPE_USER)) {
            userIds.add(weSensitiveHitQuery.getAuditScopeId());
        } else {
            List<String> userIdList = weUserService.selectWeUserList(WeUser.builder().department(new String[]{weSensitiveHitQuery.getAuditScopeId()}).build())
                    .stream().filter(Objects::nonNull).map(WeUser::getUserId).collect(Collectors.toList());
            userIds.addAll(userIdList);
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum() == null ? 1 : pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize() == null ? 10 : pageDomain.getPageSize();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        int from = (pageNum - 1) * pageSize;
        builder.size(pageSize);
        builder.from(from);
        builder.sort("msgtime", SortOrder.DESC);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder userBuilder = QueryBuilders.boolQuery();
        userIds.forEach(user -> {
            userBuilder.should(QueryBuilders.termQuery("from.keyword", user));
        });
        userBuilder.minimumShouldMatch(1);
        boolQueryBuilder.must(userBuilder);
        if (StringUtils.isNotBlank(weSensitiveHitQuery.getKeyword())) {
            BoolQueryBuilder keywordBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("text.content", weSensitiveHitQuery.getKeyword()));
            boolQueryBuilder.must(keywordBuilder);
        }
        builder.query(boolQueryBuilder);
        return elasticSearch.searchPage(WeConstans.WECOM_SENSITIVE_HIT_INDEX, builder, pageNum, pageSize, JSONObject.class);
    }

    private void addHitSensitiveList(List<JSONObject> json, WeSensitive weSensitive) throws IOException {
        //创建索引
        elasticSearch.createIndex2(WeConstans.WECOM_SENSITIVE_HIT_INDEX, elasticSearch.getFinanceMapping());
        if (weSensitive.getAlertFlag().equals(1)) {
            //针对每一条命中信息发送消息通知给相应的审计人 TODO
        }
        //批量提交插入记录
        if (CollectionUtils.isNotEmpty(json)) {
            List<ElasticSearchEntity> list = json.stream().filter(Objects::nonNull).map(j -> {
                ElasticSearchEntity ese = new ElasticSearchEntity();
                ese.setData(j);
                ese.setId(j.getString("msgid"));
                return ese;
            }).collect(Collectors.toList());
            elasticSearch.insertBatch(WeConstans.WECOM_SENSITIVE_HIT_INDEX, list);
        }
    }

    private List<String> getScopeUsers(List<WeSensitiveAuditScope> scopeList) {
        List<String> users = Lists.newArrayList();
        scopeList.forEach(scope -> {
            if (scope.getScopeType().equals(WeConstans.USE_SCOP_BUSINESSID_TYPE_USER)) {
                users.add(scope.getAuditScopeId());
            } else {
                List<String> userIdList = weUserService.selectWeUserList(WeUser.builder().department(new String[]{scope.getAuditScopeId()}).build())
                        .stream().filter(Objects::nonNull).map(WeUser::getUserId).collect(Collectors.toList());
                users.addAll(userIdList);
            }
        });
        return users;
    }

    private List<JSONObject> hitSensitiveInES(List<String> patternWords, List<String> users) {
        //TODO user过大时进行分组处理
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.sort("msgtime", SortOrder.DESC);
        BoolQueryBuilder patterWordsBuilder = QueryBuilders.boolQuery();
        patternWords.forEach(word -> {
            patterWordsBuilder.should(QueryBuilders.matchPhraseQuery("text.content", word));
        });
        patterWordsBuilder.minimumShouldMatch(1);

        BoolQueryBuilder userBuilder = QueryBuilders.boolQuery();
        users.forEach(user -> {
            userBuilder.should(QueryBuilders.termQuery("from.keyword", user));
        });
        userBuilder.minimumShouldMatch(1);

        BoolQueryBuilder searchBuilder = QueryBuilders.boolQuery().must(patterWordsBuilder).must(userBuilder);
        builder.query(searchBuilder);
        return elasticSearch.search(WeConstans.WECOM_FINANCE_INDEX, builder, JSONObject.class);
    }
}
