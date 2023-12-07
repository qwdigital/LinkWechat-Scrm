package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WxUser;
import com.linkwechat.domain.wecom.vo.weixin.WxAuthUserInfoVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WxUserMapper;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信用户表(WxUser)
 *
 * @author danmo
 * @since 2022-07-01 13:42:38
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements IWxUserService {


    @Async
    @Override
    public void saveOrUpdate(WxAuthUserInfoVo wxAuthUserInfoVo) {
        WxUser wxUser = new WxUser();
        wxUser.setAvatar(wxAuthUserInfoVo.getHeadImgUrl());
        wxUser.setCity(wxAuthUserInfoVo.getCity());
        wxUser.setCountry(wxAuthUserInfoVo.getCountry());
        wxUser.setNickName(wxAuthUserInfoVo.getNickName());
        wxUser.setSex(wxAuthUserInfoVo.getSex());
        wxUser.setUnionId(wxAuthUserInfoVo.getUnionId());
        wxUser.setOpenId(wxAuthUserInfoVo.getOpenId());
        wxUser.setPrivilege(wxAuthUserInfoVo.getPrivilege().toJSONString());
        wxUser.setProvince(wxAuthUserInfoVo.getProvince());
        saveOrUpdate(wxUser,new LambdaQueryWrapper<WxUser>()
                .eq(WxUser::getOpenId,wxAuthUserInfoVo.getOpenId())
                .eq(WxUser::getUnionId,wxAuthUserInfoVo.getUnionId())
                .eq(WxUser::getDelFlag,0));
    }

    @Override
    public WxUser getCustomerInfo(String openId, String unionId) {
        List<WxUser> wxUserList = list(new LambdaQueryWrapper<WxUser>()
                .eq(WxUser::getOpenId,openId)
                .eq(StringUtils.isNotEmpty(unionId),WxUser::getUnionId,unionId)
                .eq(WxUser::getDelFlag, Constants.COMMON_STATE));

        if(CollectionUtil.isNotEmpty(wxUserList)){
            return wxUserList.stream().findFirst().get();
        }
        return new WxUser();
    }
}
