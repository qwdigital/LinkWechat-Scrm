package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeSensitive;
import com.linkwechat.domain.WeSensitiveAuditScope;
import com.linkwechat.domain.msgaudit.query.WeSensitiveHitQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactSensitiveMsgVo;

import java.util.List;

/**
 * 敏感词设置表(WeSensitive)
 *
 * @author danmo
 * @since 2022-06-10 10:38:46
 */
public interface IWeSensitiveService extends IService<WeSensitive> {
    /**
     * 查询敏感词设置
     *
     * @param id 敏感词设置ID
     * @return 敏感词设置
     */
    public WeSensitive selectWeSensitiveById(Long id);

    /**
     * 查询敏感词设置列表
     *
     * @param weSensitive 敏感词设置
     * @return 敏感词设置集合
     */
    public List<WeSensitive> selectWeSensitiveList(WeSensitive weSensitive);

    /**
     * 新增敏感词设置
     *
     * @param weSensitive 敏感词设置
     */
    public void insertWeSensitive(WeSensitive weSensitive);

    /**
     * 修改敏感词设置
     *
     * @param weSensitive 敏感词设置
     */
    public void updateWeSensitive(WeSensitive weSensitive);

    /**
     * 批量删除敏感词设置
     *
     * @param ids 需要删除的敏感词设置ID
     * @return 结果
     */
    public int deleteWeSensitiveByIds(Long[] ids);

    /**
     * 批量删除敏感词设置信息
     *
     * @param ids 敏感词设置ID
     */
    public void destroyWeSensitiveByIds(Long[] ids);

    public List<WeChatContactSensitiveMsgVo> getHitSensitiveList(WeSensitiveHitQuery query);

    List<String> getScopeUsers(List<WeSensitiveAuditScope> auditUserScope);
} 
