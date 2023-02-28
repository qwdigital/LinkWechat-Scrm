package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.domain.wecom.query.user.WeAddUserQuery;
import com.linkwechat.domain.wecom.query.user.WeLeaveUserQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeCorpQrVo;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.user.WeLeaveUserVo;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.service.IQwUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 微信token相关接口
 * @author: HaoN
 * @create: 2020-08-26 14:43
 **/
@Slf4j
@Service
public class QwUserServiceImpl implements IQwUserService {

    @Autowired
    private WeUserClient weUserClient;

    @Override
    public WeLoginUserVo getLoginUser(WeUserQuery query) {
        return weUserClient.getLoginUser(query);
    }

    @Override
    public WeResultVo addUser(WeAddUserQuery query) {
        return weUserClient.addUser(query);
    }

    @Override
    public WeResultVo updateUser(WeAddUserQuery query) {
        return weUserClient.updateUser(query);
    }

    @Override
    public WeUserDetailVo getUserInfo(WeUserQuery query) {
        return weUserClient.getUserInfo(query);
    }

    @Override
    public WeResultVo delUser(WeUserQuery query) {
        return weUserClient.delUser(query);
    }

    @Override
    public WeResultVo batchDelUser(WeUserQuery query) {
        return weUserClient.batchDelUser(query);
    }

    @Override
    public WeUserListVo getSimpleList(WeUserListQuery query) {
        return weUserClient.getSimpleList(query);
    }

    @Override
    public WeUserListVo getList(WeUserListQuery query) {
        return weUserClient.getUserList(query);
    }

    @Override
    public WeUserDetailVo getUserSensitiveInfo(WeUserQuery query) {
        return weUserClient.getUserSensitiveInfo(query);
    }

    @Override
    public WeCorpQrVo getJoinQrcode(WeCorpQrQuery query) {
        return weUserClient.getJoinQrcode(query);
    }

    @Override
    public WeLeaveUserVo getUnassignedList(WeLeaveUserQuery query) {
        return weUserClient.getUnassignedList(query);
    }
}
