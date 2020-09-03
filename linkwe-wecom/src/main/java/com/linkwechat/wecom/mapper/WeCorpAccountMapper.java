package com.linkwechat.wecom.mapper;

import java.util.List;
import com.linkwechat.wecom.domain.WeCorpAccount;

/**
 * 企业id相关配置Mapper接口
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
public interface WeCorpAccountMapper
{
    /**
     * 查询企业id相关配置
     * 
     * @param id 企业id相关配置ID
     * @return 企业id相关配置
     */
    public WeCorpAccount selectWeCorpAccountById(Long id);

    /**
     * 查询企业id相关配置列表
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 企业id相关配置集合
     */
    public List<WeCorpAccount> selectWeCorpAccountList(WeCorpAccount wxCorpAccount);

    /**
     * 新增企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    public int insertWeCorpAccount(WeCorpAccount wxCorpAccount);

    /**
     * 修改企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    public int updateWeCorpAccount(WeCorpAccount wxCorpAccount);

    /**
     * 删除企业id相关配置
     * 
     * @param id 企业id相关配置ID
     * @return 结果
     */
    public int deleteWeCorpAccountById(Long id);

    /**
     * 批量删除企业id相关配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeCorpAccountByIds(Long[] ids);


    /**
     * 获取有效cropid
     * @return
     */
    public WeCorpAccount findValidWeCorpAccount();
}
