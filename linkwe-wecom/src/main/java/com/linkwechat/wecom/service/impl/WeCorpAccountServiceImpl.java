package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.wecom.mapper.WeCorpAccountMapper;
import com.linkwechat.wecom.service.IWeCorpAccountService;
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



    public WeCorpAccountServiceImpl(){
        System.out.println("nihaoya");
    }


    /**
     * 获取有效的企业id
     *
     * @return 结果
     */
    @Override
    public WeCorpAccount findValidWeCorpAccount() {

        WeCorpAccount weCorpAccount = this.getOne(new LambdaQueryWrapper<WeCorpAccount>()
                .eq(WeCorpAccount::getDelFlag, Constants.NORMAL_CODE));

        return weCorpAccount!=null?weCorpAccount:WeCorpAccount.builder().build();
    }




    @Override
    public void startCustomerChurnNoticeSwitch(String status) {
        WeCorpAccount validWeCorpAccount = findValidWeCorpAccount();
        validWeCorpAccount.setCustomerChurnNoticeSwitch(status);
        this.updateById(validWeCorpAccount);
    }

    @Override
    public String getCustomerChurnNoticeSwitch() {
        WeCorpAccount validWeCorpAccount = this.findValidWeCorpAccount();
        String noticeSwitch = Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getCustomerChurnNoticeSwitch)
                .orElse(WeConstans.DEL_FOLLOW_USER_SWITCH_CLOSE);
        return noticeSwitch;
    }



}
