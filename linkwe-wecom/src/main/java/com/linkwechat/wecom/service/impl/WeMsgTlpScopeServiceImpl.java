package com.linkwechat.wecom.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeMsgTlpScopeMapper;
import com.linkwechat.wecom.domain.WeMsgTlpScope;
import com.linkwechat.wecom.service.IWeMsgTlpScopeService;

/**
 * 模板使用人员范围Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeMsgTlpScopeServiceImpl implements IWeMsgTlpScopeService 
{
    @Autowired
    private WeMsgTlpScopeMapper weMsgTlpScopeMapper;

    /**
     * 查询模板使用人员范围
     * 
     * @param id 模板使用人员范围ID
     * @return 模板使用人员范围
     */
    @Override
    public WeMsgTlpScope selectWeMsgTlpScopeById(Long id)
    {
        return weMsgTlpScopeMapper.selectWeMsgTlpScopeById(id);
    }

    /**
     * 查询模板使用人员范围列表
     * 
     * @param weMsgTlpScope 模板使用人员范围
     * @return 模板使用人员范围
     */
    @Override
    public List<WeMsgTlpScope> selectWeMsgTlpScopeList(WeMsgTlpScope weMsgTlpScope)
    {
        return weMsgTlpScopeMapper.selectWeMsgTlpScopeList(weMsgTlpScope);
    }

    /**
     * 新增模板使用人员范围
     * 
     * @param weMsgTlpScope 模板使用人员范围
     * @return 结果
     */
    @Override
    public int insertWeMsgTlpScope(WeMsgTlpScope weMsgTlpScope)
    {
        return weMsgTlpScopeMapper.insertWeMsgTlpScope(weMsgTlpScope);
    }

    /**
     * 修改模板使用人员范围
     * 
     * @param weMsgTlpScope 模板使用人员范围
     * @return 结果
     */
    @Override
    public int updateWeMsgTlpScope(WeMsgTlpScope weMsgTlpScope)
    {
        return weMsgTlpScopeMapper.updateWeMsgTlpScope(weMsgTlpScope);
    }

    /**
     * 批量删除模板使用人员范围
     * 
     * @param ids 需要删除的模板使用人员范围ID
     * @return 结果
     */
    @Override
    public int deleteWeMsgTlpScopeByIds(Long[] ids)
    {
        return weMsgTlpScopeMapper.deleteWeMsgTlpScopeByIds(ids);
    }

    /**
     * 删除模板使用人员范围信息
     * 
     * @param id 模板使用人员范围ID
     * @return 结果
     */
    @Override
    public int deleteWeMsgTlpScopeById(Long id)
    {
        return weMsgTlpScopeMapper.deleteWeMsgTlpScopeById(id);
    }


    /**
     * 批量保存模板使用人员范围
     * @param weMsgTlpScopes
     * @return
     */
    @Override
    public int batchInsetWeMsgTlpScope(List<WeMsgTlpScope> weMsgTlpScopes) {

        return weMsgTlpScopeMapper.batchInsetWeMsgTlpScope(weMsgTlpScopes);
    }


    /**
     * 通过使用者id，批量删除
     * @param useUserIds
     * @return
     */
    @Override
    public int batchRemoveWeMsgTlpScopesByMsgTlpIds(List<Long> useUserIds) {
        return weMsgTlpScopeMapper.batchRemoveWeMsgTlpScopesByMsgTlpIds(useUserIds);
    }
}
