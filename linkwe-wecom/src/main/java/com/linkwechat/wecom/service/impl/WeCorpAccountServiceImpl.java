package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.wecom.mapper.WeCorpAccountMapper;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 企业id相关配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Service
public class WeCorpAccountServiceImpl extends ServiceImpl<WeCorpAccountMapper,WeCorpAccount> implements IWeCorpAccountService {




    @Autowired
    private IWeAccessTokenService iWeAccessTokenService;

    @Autowired
    private RuoYiConfig ruoYiConfig;


    /**
     * 修改企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public void updateWeCorpAccount(WeCorpAccount wxCorpAccount)
    {

        if(this.updateById(wxCorpAccount)){

            iWeAccessTokenService.removeToken(wxCorpAccount);

        }

    }



    /**
     * 获取有效的企业id
     *
     * @return 结果
     */
    @Override
    public WeCorpAccount findValidWeCorpAccount() {

        return ruoYiConfig.isStartTenant()? WeCorpAccount.builder().build():this.getOne(new LambdaQueryWrapper<WeCorpAccount>()
                .eq(WeCorpAccount::getDelFlag,Constants.NORMAL_CODE)
                .eq(WeCorpAccount::getStatus,Constants.NORMAL_CODE));
    }


    /**
     * 启用有效的企业微信账号
     * @param corpId
     */
    @Override
    public int startVailWeCorpAccount(String corpId) {

        int returnCode = this.baseMapper.startVailWeCorpAccount(corpId);

        if(Constants.SERVICE_RETURN_SUCCESS_CODE<returnCode){

            iWeAccessTokenService.removeToken(WeCorpAccount.builder().build());

        }


        return returnCode;
    }

    @Override
    public void startCustomerChurnNoticeSwitch(String status) {
        WeCorpAccount validWeCorpAccount = findValidWeCorpAccount();
        validWeCorpAccount.setCustomerChurnNoticeSwitch(status);
        this.updateWeCorpAccount(validWeCorpAccount);
    }

    @Override
    public String getCustomerChurnNoticeSwitch() {
        WeCorpAccount validWeCorpAccount = this.findValidWeCorpAccount();
        String noticeSwitch = Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getCustomerChurnNoticeSwitch)
                .orElse(WeConstans.DEL_FOLLOW_USER_SWITCH_CLOSE);
        return noticeSwitch;
    }



}
