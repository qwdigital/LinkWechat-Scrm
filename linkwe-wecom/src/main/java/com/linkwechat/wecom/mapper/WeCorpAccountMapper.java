package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import org.apache.ibatis.annotations.Param;

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
    @SqlParser(filter = true)
    public WeCorpAccount selectWeCorpAccountById(Long id);

    /**
     * 查询企业id相关配置列表
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 企业id相关配置集合
     */
    @SqlParser(filter = true)
    public List<WeCorpAccount> selectWeCorpAccountList(WeCorpAccount wxCorpAccount);

    /**
     * 新增企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @SqlParser(filter = true)
    public int insertWeCorpAccount(WeCorpAccount wxCorpAccount);

    /**
     * 修改企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @SqlParser(filter = true)
    public int updateWeCorpAccount(WeCorpAccount wxCorpAccount);



    /**
     * 获取有效cropid
     * @return
     */
    @SqlParser(filter = true)
    public WeCorpAccount findValidWeCorpAccount();


    /**
     * 启用有效的企业微信账号
     * @param corpId
     * @return
     */
    @SqlParser(filter = true)
    public int startVailWeCorpAccount(@Param("corpId") String corpId);


    /**
     * 根据企业账号获取企业相关配置
     * @param corpAccount
     * @return
     */
    @SqlParser(filter = true)
    WeCorpAccount findWeCorpByAccount(@Param("corpAccount") String corpAccount);
}
