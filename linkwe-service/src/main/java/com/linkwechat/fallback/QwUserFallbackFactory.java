package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.wecom.query.WeCorpQrQuery;
import com.linkwechat.domain.wecom.query.user.WeLeaveUserQuery;
import com.linkwechat.domain.wecom.query.user.WeUserListQuery;
import com.linkwechat.domain.wecom.query.user.WeUserQuery;
import com.linkwechat.domain.wecom.vo.WeCorpQrVo;
import com.linkwechat.domain.wecom.vo.user.WeLeaveUserVo;
import com.linkwechat.domain.wecom.vo.user.WeLoginUserVo;
import com.linkwechat.domain.wecom.vo.user.WeUserDetailVo;
import com.linkwechat.domain.wecom.vo.user.WeUserListVo;
import com.linkwechat.fegin.QwUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author leejoker
 * @date 2022/3/29 22:58
 */
@Component
@Slf4j
public class QwUserFallbackFactory implements QwUserClient {
    @Override
    public AjaxResult<WeLoginUserVo> getLoginUser(WeUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUserListVo> getSimpleList(WeUserListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUserDetailVo> getUserInfo(WeUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUserDetailVo> getUserSensitiveInfo(WeUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUserListVo> getList(WeUserListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCorpQrVo> getJoinQrcode(WeCorpQrQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLeaveUserVo> getUnassignedList(WeLeaveUserQuery query) {
        return null;
    }

    @Override
    public AjaxResult<SysUser> listByQuery(SysUser sysUser) {
        return null;
    }
}
