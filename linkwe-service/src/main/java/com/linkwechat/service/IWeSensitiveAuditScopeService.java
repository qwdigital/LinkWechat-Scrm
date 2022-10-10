package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeSensitiveAuditScope;

import java.util.List;

/**
 * 敏感词审计范围(WeSensitiveAuditScope)
 *
 * @author danmo
 * @since 2022-06-10 10:38:47
 */
public interface IWeSensitiveAuditScopeService extends IService<WeSensitiveAuditScope> {

    void deleteAuditScopeBySensitiveId(Long sensitiveId);

    void deleteAuditScopeBySensitiveIds(List<Long> sensitiveIds);
}
