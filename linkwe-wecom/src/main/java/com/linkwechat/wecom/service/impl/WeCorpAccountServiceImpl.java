package com.linkwechat.wecom.service.impl;

import java.util.List;
import java.util.Optional;

import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeCorpAccountMapper;
import com.linkwechat.wecom.domain.WeCorpAccount;
import org.springframework.transaction.annotation.Transactional;

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


    @Autowired
    private IWeAccessTokenService iWeAccessTokenService;

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

        int returnCode = weCorpAccountMapper.updateWeCorpAccount(wxCorpAccount);
        if(Constants.SERVICE_RETURN_SUCCESS_CODE<returnCode){


            iWeAccessTokenService.removeToken();

        }

        return returnCode;
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


    /**
     * 启用有效的企业微信账号
     * @param corpId
     */
    @Override
    public int startVailWeCorpAccount(String corpId) {

        int returnCode = weCorpAccountMapper.startVailWeCorpAccount(corpId);

        if(Constants.SERVICE_RETURN_SUCCESS_CODE<returnCode){


            iWeAccessTokenService.removeToken();

        }


        return returnCode;
    }

    @Override
    public int startCustomerChurnNoticeSwitch(String status) {
        WeCorpAccount validWeCorpAccount = findValidWeCorpAccount();
        validWeCorpAccount.setCustomerChurnNoticeSwitch(status);
        return weCorpAccountMapper.updateWeCorpAccount(validWeCorpAccount);
    }

    @Override
    public String getCustomerChurnNoticeSwitch() {
        WeCorpAccount validWeCorpAccount = weCorpAccountMapper.findValidWeCorpAccount();
        String noticeSwitch = Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getCustomerChurnNoticeSwitch)
                .orElse(WeConstans.DEL_FOLLOW_USER_SWITCH_CLOSE);
        return noticeSwitch;
    }


}
