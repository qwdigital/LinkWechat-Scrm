package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.fegin.QwAppMsgClient;
import com.linkwechat.fegin.QwDeptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author leejoker
 * @date 2022/3/29 23:00
 */
@Component
@Slf4j
public class QwAppMsgFallbackFactory implements QwAppMsgClient {

    @Override
    public AjaxResult<WeAppMsgVo> sendAppMsg(WeAppMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> recallAppMsg(WeRecallMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> recallAgentAppMsg(WeRecallMsgQuery query) {
        return null;
    }
}
