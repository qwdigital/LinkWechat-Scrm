package com.linkwechat.wecom.service.impl;

import java.util.List;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.service.IWxCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WxCorpAccountMapper;
import com.linkwechat.wecom.domain.WxCorpAccount;

/**
 * 企业id相关配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Service
public class WxCorpAccountServiceImpl implements IWxCorpAccountService
{
    @Autowired
    private WxCorpAccountMapper wxCorpAccountMapper;

    /**
     * 查询企业id相关配置
     * 
     * @param id 企业id相关配置ID
     * @return 企业id相关配置
     */
    @Override
    public WxCorpAccount selectWxCorpAccountById(Long id)
    {
        return wxCorpAccountMapper.selectWxCorpAccountById(id);
    }

    /**
     * 查询企业id相关配置列表
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 企业id相关配置
     */
    @Override
    public List<WxCorpAccount> selectWxCorpAccountList(WxCorpAccount wxCorpAccount)
    {
        return wxCorpAccountMapper.selectWxCorpAccountList(wxCorpAccount);
    }

    /**
     * 新增企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public int insertWxCorpAccount(WxCorpAccount wxCorpAccount)
    {
        wxCorpAccount.setCreateTime(DateUtils.getNowDate());
        return wxCorpAccountMapper.insertWxCorpAccount(wxCorpAccount);
    }

    /**
     * 修改企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public int updateWxCorpAccount(WxCorpAccount wxCorpAccount)
    {
        wxCorpAccount.setUpdateTime(DateUtils.getNowDate());
        return wxCorpAccountMapper.updateWxCorpAccount(wxCorpAccount);
    }

    /**
     * 批量删除企业id相关配置
     * 
     * @param ids 需要删除的企业id相关配置ID
     * @return 结果
     */
    @Override
    public int deleteWxCorpAccountByIds(Long[] ids)
    {
        return wxCorpAccountMapper.deleteWxCorpAccountByIds(ids);
    }

    /**
     * 删除企业id相关配置信息
     * 
     * @param id 企业id相关配置ID
     * @return 结果
     */
    @Override
    public int deleteWxCorpAccountById(Long id)
    {
        return wxCorpAccountMapper.deleteWxCorpAccountById(id);
    }
}
