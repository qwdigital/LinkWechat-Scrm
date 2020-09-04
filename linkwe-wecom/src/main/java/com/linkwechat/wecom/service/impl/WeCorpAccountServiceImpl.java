package com.linkwechat.wecom.service.impl;

import java.util.List;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeCorpAccountMapper;
import com.linkwechat.wecom.domain.WeCorpAccount;

/**
 * 企业id相关配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Service
public class WeCorpAccountServiceImpl implements IWeCorpAccountService {

    @Autowired
    private WeCorpAccountMapper weCorpAccountMapper;

    /**
     * 查询企业id相关配置
     * 
     * @param id 企业id相关配置ID
     * @return 企业id相关配置
     */
    @Override
    public WeCorpAccount selectWeCorpAccountById(Long id)
    {
        return weCorpAccountMapper.selectWeCorpAccountById(id);
    }

    /**
     * 查询企业id相关配置列表
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 企业id相关配置
     */
    @Override
    public List<WeCorpAccount> selectWeCorpAccountList(WeCorpAccount wxCorpAccount)
    {
        return weCorpAccountMapper.selectWeCorpAccountList(wxCorpAccount);
    }

    /**
     * 新增企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public int insertWeCorpAccount(WeCorpAccount wxCorpAccount)
    {
        wxCorpAccount.setCreateTime(DateUtils.getNowDate());
        return weCorpAccountMapper.insertWeCorpAccount(wxCorpAccount);
    }

    /**
     * 修改企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public int updateWeCorpAccount(WeCorpAccount wxCorpAccount)
    {
        wxCorpAccount.setUpdateTime(DateUtils.getNowDate());
        return weCorpAccountMapper.updateWeCorpAccount(wxCorpAccount);
    }

    /**
     * 批量删除企业id相关配置
     * 
     * @param ids 需要删除的企业id相关配置ID
     * @return 结果
     */
    @Override
    public int deleteWeCorpAccountByIds(Long[] ids)
    {
        return weCorpAccountMapper.deleteWeCorpAccountByIds(ids);
    }

    /**
     * 删除企业id相关配置信息
     * 
     * @param id 企业id相关配置ID
     * @return 结果
     */
    @Override
    public int deleteWeCorpAccountById(Long id)
    {
        return weCorpAccountMapper.deleteWeCorpAccountById(id);
    }



    /**
     * 获取有效的企业id
     *
     * @return 结果
     */
    @Override
    public WeCorpAccount findValidWeCorpAccount() {
        return weCorpAccountMapper.findValidWeCorpAccount();
    }


}
