package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.corp.query.WeCorpAccountQuery;
import com.linkwechat.domain.corp.vo.WeCorpAccountVo;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业id相关配置(WeCorpAccount)
 *
 * @author danmo
 * @since 2022-03-08 19:01:14
 */
public interface IWeCorpAccountService extends IService<WeCorpAccount> {

    List<WeCorpAccount> getAllCorpAccountInfo();

    WeCorpAccount  getCorpAccountByCorpId(String corpId);

    void startCustomerChurnNoticeSwitch(String status);

    String getCustomerChurnNoticeSwitch();
}
