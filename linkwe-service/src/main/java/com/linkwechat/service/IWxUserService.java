package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WxUser;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;

/**
 * 微信用户表(WxUser)
 *
 * @author danmo
 * @since 2022-07-01 13:42:38
 */
public interface IWxUserService extends IService<WxUser> {

    void saveOrUpdate(WxAuthUserInfoVo wxAuthUserInfoVo);

    WxUser getCustomerInfo(String openId, String unionId);
}
