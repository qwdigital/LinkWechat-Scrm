package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeSensitiveAuditScope;

import java.util.List;

/**
 * 敏感词审计范围Service接口
 *
 * @author ruoyi
 * @date 2020-12-29
 */
public interface IWeSensitiveAuditScopeService {
    /**
     * 查询敏感词审计范围
     *
     * @param id 敏感词审计范围ID
     * @return 敏感词审计范围
     */
    public WeSensitiveAuditScope selectWeSensitiveAuditScopeById(Long id);

    /**
     * 查询敏感词审计范围列表
     *
     * @param weSensitiveAuditScope 敏感词审计范围
     * @return 敏感词审计范围集合
     */
    public List<WeSensitiveAuditScope> selectWeSensitiveAuditScopeList(WeSensitiveAuditScope weSensitiveAuditScope);

    /**
     * 新增敏感词审计范围
     *
     * @param weSensitiveAuditScope 敏感词审计范围
     * @return 结果
     */
    public int insertWeSensitiveAuditScope(WeSensitiveAuditScope weSensitiveAuditScope);

    /**
     * 批量新增
     *
     * @param weSensitiveAuditScopeList
     * @return
     */
    public int insertWeSensitiveAuditScopeList(List<WeSensitiveAuditScope> weSensitiveAuditScopeList);

    /**
     * 修改敏感词审计范围
     *
     * @param weSensitiveAuditScope 敏感词审计范围
     * @return 结果
     */
    public int updateWeSensitiveAuditScope(WeSensitiveAuditScope weSensitiveAuditScope);

    /**
     * 批量删除敏感词审计范围
     *
     * @param ids 需要删除的敏感词审计范围ID
     * @return 结果
     */
    public int deleteWeSensitiveAuditScopeByIds(Long[] ids);

    /**
     * 删除敏感词审计范围信息
     *
     * @param id 敏感词审计范围ID
     * @return 结果
     */
    public int deleteWeSensitiveAuditScopeById(Long id);

    public int deleteAuditScopeBySensitiveId(Long sensitiveId);
    public int deleteAuditScopeBySensitiveIds(Long[] sensitiveIds);
}
