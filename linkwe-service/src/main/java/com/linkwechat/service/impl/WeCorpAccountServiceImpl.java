package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.corp.query.WeCorpAccountQuery;
import com.linkwechat.domain.corp.vo.WeCorpAccountVo;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.fegin.QwCorpClient;
import com.linkwechat.mapper.WeCorpAccountMapper;
import com.linkwechat.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 企业id相关配置(WeCorpAccount)
 *
 * @author danmo
 * @since 2022-03-08 19:01:14
 */
@Service
public class WeCorpAccountServiceImpl extends ServiceImpl<WeCorpAccountMapper, WeCorpAccount> implements IWeCorpAccountService {

    @Override
    public List<WeCorpAccount> getAllCorpAccountInfo() {
        return list(new LambdaQueryWrapper<WeCorpAccount>().eq(WeCorpAccount::getDelFlag,0));
    }

    @Override
    public WeCorpAccount getCorpAccountByCorpId(String corpId) {
        return getOne(new LambdaQueryWrapper<WeCorpAccount>()
                .eq(StringUtils.isNotEmpty(corpId),WeCorpAccount::getCorpId,corpId)
                .eq(WeCorpAccount::getDelFlag, Constants.COMMON_STATE).last("limit 1"));
    }

    @Override
    public void startCustomerChurnNoticeSwitch(String status) {
        WeCorpAccount validWeCorpAccount = getCorpAccountByCorpId(SecurityUtils.getCorpId());
        validWeCorpAccount.setCustomerChurnNoticeSwitch(status);
        this.updateById(validWeCorpAccount);
    }

    @Override
    public String getCustomerChurnNoticeSwitch() {
        WeCorpAccount validWeCorpAccount = this.getCorpAccountByCorpId(SecurityUtils.getCorpId());
        String noticeSwitch = Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getCustomerChurnNoticeSwitch)
                .orElse(WeConstans.DEL_FOLLOW_USER_SWITCH_CLOSE);
        return noticeSwitch;
    }

}
