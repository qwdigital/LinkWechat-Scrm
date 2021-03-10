package com.linkwechat.wecom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.domain.WeSensitive;
import com.linkwechat.wecom.domain.WeSensitiveAuditScope;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.domain.query.WeSensitiveHitQuery;
import com.linkwechat.wecom.mapper.WeSensitiveMapper;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeSensitiveAuditScopeService;
import com.linkwechat.wecom.service.IWeSensitiveService;
import com.linkwechat.wecom.service.IWeUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private WeMessagePushClient weMessagePushClient;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Value("${wecome.chatKey}")
    private String chartKey;

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
        log.info("执行敏感词命中过滤,time=[{}]", System.currentTimeMillis());
        //获取所有的敏感词规则
        List<WeSensitive> allSensitiveRules = weSensitiveMapper.selectWeSensitiveList(new WeSensitive());
        //根据规则过滤命中
        if (CollectionUtils.isNotEmpty(allSensitiveRules)) {
            allSensitiveRules.parallelStream().forEach(weSensitive -> {
                List<JSONObject> jsonList = Lists.newArrayList();
                List<String> patternWords = Arrays.asList(weSensitive.getPatternWords().split(","));
                List<String> users = getScopeUsers(weSensitive.getAuditUserScope());
                patternWords.forEach(patternWord -> {
                    jsonList.addAll(hitSensitiveInES(patternWord, users));
                });
                //将命中结果插入es
                addHitSensitiveList(jsonList, weSensitive);
            });
        }
    }

    @Override
    public PageInfo<JSONObject> getHitSensitiveList(WeSensitiveHitQuery weSensitiveHitQuery) {
        elasticSearch.createIndex2(WeConstans.WECOM_SENSITIVE_HIT_INDEX, getSensitiveHitMapping());
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
        PageInfo<JSONObject> pageInfo = elasticSearch.searchPage(WeConstans.WECOM_SENSITIVE_HIT_INDEX, builder, pageNum, pageSize, JSONObject.class);
        return hitPageInfoHandler(pageInfo);
    }

    private PageInfo<JSONObject> hitPageInfoHandler(PageInfo<JSONObject> pageInfo) {
        List<JSONObject> jsonList = pageInfo.getList();
        if (CollectionUtils.isNotEmpty(jsonList)) {
            List<JSONObject> newList = jsonList.stream().map(j -> {
                JSONObject json = new JSONObject();
                String userId = j.getString("from");
                WeUser user = new WeUser();
                user.setUserId(userId);
                List<WeUser> uList = weUserService.selectWeUserList(user);
                if (CollectionUtils.isNotEmpty(uList)) {
                    json.put("from", uList.get(0).getName());
                    json.put("content", j.getJSONObject("text").getString("content"));
                    json.put("msgtime", j.getString("msgtime"));
                    json.put("status", j.getString("status"));
                    json.put("patternWords", j.getString("pattern_words"));
                }
                return json;
            }).collect(Collectors.toList());
            pageInfo.setList(newList);
        }
        return pageInfo;
    }

    private void addHitSensitiveList(List<JSONObject> json, WeSensitive weSensitive) {
        elasticSearch.createIndex2(WeConstans.WECOM_SENSITIVE_HIT_INDEX, getSensitiveHitMapping());
        //批量提交插入记录
        if (CollectionUtils.isNotEmpty(json)) {
            List<ElasticSearchEntity> list = json.stream().filter(Objects::nonNull).map(j -> {
                ElasticSearchEntity ese = new ElasticSearchEntity();
                j.put("status", "0");
                ese.setData(j);
                ese.setId(j.getString("msgid"));
                return ese;
            }).collect(Collectors.toList());
            elasticSearch.insertBatchAsync(WeConstans.WECOM_SENSITIVE_HIT_INDEX, list, this::sendMessage, weSensitive);
        }
    }

    private void sendMessage(Object listObj, Object weSensitiveObj) {
        WeSensitive weSensitive = (WeSensitive) weSensitiveObj;
        List<ElasticSearchEntity> list = (List<ElasticSearchEntity>) listObj;
        if (weSensitive.getAlertFlag().equals(1) && CollectionUtils.isNotEmpty(list)) {
            //发送消息通知给相应的审计人
            WeCorpAccount weCorpAccount = weCorpAccountService.findValidWeCorpAccount();
            String auditUserId = weSensitive.getAuditUserId();
            String content = "有消息触发敏感词，请登录系统及时处理!";
            TextMessageDto textMessageDto = new TextMessageDto();
            textMessageDto.setContent(content);
            WeMessagePushDto pushDto = new WeMessagePushDto();
            pushDto.setTouser(auditUserId);
            pushDto.setMsgtype(MessageType.TEXT.getMessageType());
            pushDto.setText(textMessageDto);
            weMessagePushClient.sendMessageToUser(pushDto, weCorpAccount.getAgentId());
            //批量更新
            list = list.stream().peek(entity -> {
                Map map = entity.getData();
                map.put("status", "1");
                entity.setData(map);
            }).collect(Collectors.toList());
            elasticSearch.updateBatch(WeConstans.WECOM_SENSITIVE_HIT_INDEX, list);
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

    private List<JSONObject> hitSensitiveInES(String patternWord, List<String> users) {
        int pieceSize = userPiecesCount(users);
        if (pieceSize != -1) {
            List<JSONObject> resultList = Lists.newArrayList();
            for (int i = 0; i < pieceSize; i++) {
                List<String> subUsers = users.subList(i + i * pieceSize, i + pieceSize - 1);
                SearchSourceBuilder builder = new SearchSourceBuilder();
                builder.sort("msgtime", SortOrder.DESC);
                BoolQueryBuilder userBuilder = QueryBuilders.boolQuery();
                subUsers.parallelStream().forEach(user -> userBuilder.should(QueryBuilders.termQuery("from", user)));
                userBuilder.minimumShouldMatch(1);
                BoolQueryBuilder searchBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("text.content", patternWord)).must(userBuilder);
                builder.query(searchBuilder);
                List<JSONObject> list = elasticSearch.search(chartKey, builder, JSONObject.class);
                list.parallelStream().forEach(j -> j.put("pattern_words", patternWord));
                resultList.addAll(list);
            }
            return resultList;
        }
        return Lists.newArrayList();
    }

    private int userPiecesCount(List<String> users) {
        if (CollectionUtils.isNotEmpty(users)) {
            if (users.size() % WeConstans.SENSITIVE_USER_PIECE != 0) {
                return users.size() / WeConstans.SENSITIVE_USER_PIECE + 1;
            } else {
                return users.size() / WeConstans.SENSITIVE_USER_PIECE;
            }
        }
        return -1;
    }

    private XContentBuilder getSensitiveHitMapping() {
        try {
            //创建索引
            XContentBuilder mapping = XContentFactory.jsonBuilder()
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
                    .startObject("status")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("pattern_words")
                    .field("type", "keyword")
                    .endObject()
                    .startObject("text")
                    .startObject("content")
                    .field("type", "text")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            return mapping;
        } catch (Exception e) {
            log.warn("create sensitive-hit mapping failed, exception={}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
