package com.linkwechat.factory.impl.kf;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.service.IWeKfServicerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 接待人员接待状态变更事件
 * @date 2022/1/20 22:10
 **/
@Slf4j
@Service("servicer_status_change")
public class WeKfEventServicerStatusChangeImpl extends WeKfEventStrategy {

    @Autowired
    private IWeKfServicerService weKfServicerService;
    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服会话接待状态变更: msg:{}", msgStr);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msgStr, WeKfSyncEventMsgVo.class);
        String servicerUserId = weKfSyncEventMs.getServicerUserId();
        Integer status = weKfSyncEventMs.getStatus();
        String openKfId = weKfSyncEventMs.getOpenKfId();

        weKfServicerService.updateServicerStatus(weKfSyncEventMs.getCorpId(), openKfId,servicerUserId,status);
    }
}
