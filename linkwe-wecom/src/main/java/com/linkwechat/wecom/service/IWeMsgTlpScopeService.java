package com.linkwechat.wecom.service;

import java.util.List;
import com.linkwechat.wecom.domain.WeMsgTlpScope;

/**
 * 模板使用人员范围Service接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
public interface IWeMsgTlpScopeService 
{
    /**
     * 查询模板使用人员范围
     * 
     * @param id 模板使用人员范围ID
     * @return 模板使用人员范围
     */
    public WeMsgTlpScope selectWeMsgTlpScopeById(Long id);

    /**
     * 查询模板使用人员范围列表
     * 
     * @param weMsgTlpScope 模板使用人员范围
     * @return 模板使用人员范围集合
     */
    public List<WeMsgTlpScope> selectWeMsgTlpScopeList(WeMsgTlpScope weMsgTlpScope);

    /**
     * 新增模板使用人员范围
     * 
     * @param weMsgTlpScope 模板使用人员范围
     * @return 结果
     */
    public int insertWeMsgTlpScope(WeMsgTlpScope weMsgTlpScope);

    /**
     * 修改模板使用人员范围
     * 
     * @param weMsgTlpScope 模板使用人员范围
     * @return 结果
     */
    public int updateWeMsgTlpScope(WeMsgTlpScope weMsgTlpScope);

    /**
     * 批量删除模板使用人员范围
     * 
     * @param ids 需要删除的模板使用人员范围ID
     * @return 结果
     */
    public int deleteWeMsgTlpScopeByIds(Long[] ids);

    /**
     * 删除模板使用人员范围信息
     * 
     * @param id 模板使用人员范围ID
     * @return 结果
     */
    public int deleteWeMsgTlpScopeById(Long id);


    /**
     * 批量保存模板使用人员范围
     * @param weMsgTlpScopes
     * @return
     */
    public int batchInsetWeMsgTlpScope(List<WeMsgTlpScope> weMsgTlpScopes);


    /**
     * 通过使用者id批量删除
     * @param useUserIds
     * @return
     */
    public int  batchRemoveWeMsgTlpScopesByMsgTlpIds(List<Long> useUserIds);
}
