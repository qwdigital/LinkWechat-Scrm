package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.elasticsearch.ElasticSearch;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeChatContactSensitiveMsg;
import com.linkwechat.wecom.domain.WeSensitive;
import com.linkwechat.wecom.domain.WeSensitiveAuditScope;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.query.WeSensitiveHitQuery;
import com.linkwechat.wecom.domain.vo.WeChatContactSensitiveMsgVO;
import com.linkwechat.wecom.mapper.WeChatContactSensitiveMsgMapper;
import com.linkwechat.wecom.mapper.WeSensitiveMapper;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeSensitiveAuditScopeService;
import com.linkwechat.wecom.service.IWeSensitiveService;
import com.linkwechat.wecom.service.IWeUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private WeMessagePushClient weMessagePushClient;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private WeChatContactSensitiveMsgMapper weChatContactSensitiveMsgMapper;

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
    public List<WeChatContactSensitiveMsgVO> getHitSensitiveList(WeSensitiveHitQuery weSensitiveHitQuery) {
        List<String> userIds = Lists.newArrayList();
        if (ObjectUtil.equal(WeConstans.USE_SCOP_BUSINESSID_TYPE_USER,weSensitiveHitQuery.getScopeType())) {
            userIds.add(weSensitiveHitQuery.getAuditScopeId());
        } else {
            List<String> userIdList = weUserService.getList(WeUser.builder().department(weSensitiveHitQuery.getAuditScopeId()).build())
                    .stream().filter(Objects::nonNull).map(WeUser::getUserId).collect(Collectors.toList());
            userIds.addAll(userIdList);
        }
        LambdaQueryWrapper<WeChatContactSensitiveMsg> wrapper = new LambdaQueryWrapper<>();
        if(CollectionUtil.isNotEmpty(userIds)){
            wrapper.in(WeChatContactSensitiveMsg::getFromId, userIds);
        }
        if (StringUtils.isNotBlank(weSensitiveHitQuery.getKeyword())) {
            wrapper.like(WeChatContactSensitiveMsg::getPatternWords, weSensitiveHitQuery.getKeyword());
        }
        wrapper.orderByDesc(WeChatContactSensitiveMsg::getMsgTime);
        List<WeChatContactSensitiveMsg> msgList = weChatContactSensitiveMsgMapper.selectList(wrapper);
        return hitPageInfoHandler(msgList);
    }

    private List<WeChatContactSensitiveMsgVO> hitPageInfoHandler(List<WeChatContactSensitiveMsg> msgList) {
        List<WeChatContactSensitiveMsgVO> voList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(msgList)) {
            msgList.forEach(msg -> {
                WeChatContactSensitiveMsgVO vo = new WeChatContactSensitiveMsgVO();
                vo.setContent(msg.getContent());
                vo.setFromId(msg.getFromId());
                vo.setMsgtime(msg.getMsgTime());
                vo.setStatus(msg.getSendStatus().toString());
                vo.setPatternWords(msg.getPatternWords());
                voList.add(vo);
            });
        }
        return voList;
    }

    public List<String> getScopeUsers(List<WeSensitiveAuditScope> scopeList) {
        List<String> users = Lists.newArrayList();
        scopeList.forEach(scope -> {
            if (scope.getScopeType().equals(WeConstans.USE_SCOP_BUSINESSID_TYPE_USER)) {
                users.add(scope.getAuditScopeId());
            } else {
                List<String> userIdList = weUserService.getList(WeUser.builder().department(scope.getAuditScopeId()).build())
                        .stream().filter(Objects::nonNull).map(WeUser::getUserId).collect(Collectors.toList());
                users.addAll(userIdList);
            }
        });
        return users;
    }
}
