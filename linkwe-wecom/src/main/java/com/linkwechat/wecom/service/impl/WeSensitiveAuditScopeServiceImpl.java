package com.linkwechat.wecom.service.impl;

import com.linkwechat.wecom.domain.WeSensitiveAuditScope;
import com.linkwechat.wecom.mapper.WeSensitiveAuditScopeMapper;
import com.linkwechat.wecom.service.IWeSensitiveAuditScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感词审计范围Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-29
 */
@Service
public class WeSensitiveAuditScopeServiceImpl implements IWeSensitiveAuditScopeService {
    @Autowired
    private WeSensitiveAuditScopeMapper weSensitiveAuditScopeMapper;

    /**
     * 查询敏感词审计范围
     *
     * @param id 敏感词审计范围ID
     * @return 敏感词审计范围
     */
    @Override
    public WeSensitiveAuditScope selectWeSensitiveAuditScopeById(Long id) {
        return weSensitiveAuditScopeMapper.selectWeSensitiveAuditScopeById(id);
    }

    /**
     * 查询敏感词审计范围列表
     *
     * @param weSensitiveAuditScope 敏感词审计范围
     * @return 敏感词审计范围
     */
    @Override
    public List<WeSensitiveAuditScope> selectWeSensitiveAuditScopeList(WeSensitiveAuditScope weSensitiveAuditScope) {
        return weSensitiveAuditScopeMapper.selectWeSensitiveAuditScopeList(weSensitiveAuditScope);
    }

    /**
     * 新增敏感词审计范围
     *
     * @param weSensitiveAuditScope 敏感词审计范围
     * @return 结果
     */
    @Override
    public int insertWeSensitiveAuditScope(WeSensitiveAuditScope weSensitiveAuditScope) {
        return weSensitiveAuditScopeMapper.insertWeSensitiveAuditScope(weSensitiveAuditScope);
    }

    @Override
    public int insertWeSensitiveAuditScopeList(List<WeSensitiveAuditScope> weSensitiveAuditScopeList) {
        return weSensitiveAuditScopeMapper.insertWeSensitiveAuditScopeList(weSensitiveAuditScopeList);
    }

    /**
     * 修改敏感词审计范围
     *
     * @param weSensitiveAuditScope 敏感词审计范围
     * @return 结果
     */
    @Override
    public int updateWeSensitiveAuditScope(WeSensitiveAuditScope weSensitiveAuditScope) {
        return weSensitiveAuditScopeMapper.updateWeSensitiveAuditScope(weSensitiveAuditScope);
    }

    /**
     * 批量删除敏感词审计范围
     *
     * @param ids 需要删除的敏感词审计范围ID
     * @return 结果
     */
    @Override
    public int deleteWeSensitiveAuditScopeByIds(Long[] ids) {
        return weSensitiveAuditScopeMapper.deleteWeSensitiveAuditScopeByIds(ids);
    }

    /**
     * 删除敏感词审计范围信息
     *
     * @param id 敏感词审计范围ID
     * @return 结果
     */
    @Override
    public int deleteWeSensitiveAuditScopeById(Long id) {
        return weSensitiveAuditScopeMapper.deleteWeSensitiveAuditScopeById(id);
    }

    @Override
    public int deleteAuditScopeBySensitiveId(Long sensitiveId) {
        return weSensitiveAuditScopeMapper.deleteAuditScopeBySensitiveId(sensitiveId);
    }

    @Override
    public int deleteAuditScopeBySensitiveIds(Long[] sensitiveIds) {
        return weSensitiveAuditScopeMapper.deleteAuditScopeBySensitiveIds(sensitiveIds);
    }
}
