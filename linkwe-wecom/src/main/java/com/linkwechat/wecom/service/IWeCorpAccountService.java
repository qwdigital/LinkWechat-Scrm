package com.linkwechat.wecom.service;

import java.util.List;
import com.linkwechat.wecom.domain.WeCorpAccount;
import org.apache.ibatis.annotations.Param;

/**
 * 企业id相关配置Service接口
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
public interface IWeCorpAccountService
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
     * 获取有效的企业id
     *
     * @return 结果
     */
    public WeCorpAccount findValidWeCorpAccount();


    /**
     * 启用有效的企业微信账号
     * @param corpId
     */
    public int startVailWeCorpAccount(String corpId);

    /**
     * 客户流失通知开关
     * @param status 开关状态
     * @return
     */
    public int startCustomerChurnNoticeSwitch(String status);

    /**
     * 客户流失通知开关查询
     */
    public String getCustomerChurnNoticeSwitch();
}
