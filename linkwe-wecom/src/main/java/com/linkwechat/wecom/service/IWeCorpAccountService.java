package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;

/**
 * 企业id相关配置Service接口
 *
 * @author ruoyi
 * @date 2020-08-24
 */
public interface IWeCorpAccountService extends IService<WeCorpAccount> {
    /**
     * 获取有效的企业id
     *
     * @return 结果
     */
    WeCorpAccount findValidWeCorpAccount();

    /**
     * 客户流失通知开关
     *
     * @param status 开关状态
     * @return
     */
    void startCustomerChurnNoticeSwitch(String status);

    /**
     * 客户流失通知开关查询
     */
    String getCustomerChurnNoticeSwitch();

    /**
     * 通过企业id查询配置信息
     *
     * @param corpId 企业id
     * @return
     */
    WeCorpAccount getCorpAccountByCorpId(String corpId);
}
