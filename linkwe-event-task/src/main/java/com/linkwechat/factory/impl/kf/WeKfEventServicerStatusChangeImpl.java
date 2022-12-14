package com.linkwechat.factory.impl.kf;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.fegin.QwSysUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description 接待人员接待状态变更事件
 * @date 2022/1/20 22:10
 **/
@Slf4j
@Service("servicer_status_change")
public class WeKfEventServicerStatusChangeImpl extends WeKfEventStrategy {

    @Resource
    private QwSysUserClient qwSysUserClient;


    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服会话接待状态变更: msg:{}", msgStr);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msgStr, WeKfSyncEventMsgVo.class);
        SysUser sysUser = new SysUser();
        sysUser.setKfStatus(weKfSyncEventMs.getStatus());
        sysUser.setWeUserId(weKfSyncEventMs.getServicerUserId());
        qwSysUserClient.updateUserKfStatus(sysUser);
        //weKfServicerService.updateServicerStatus(weKfSyncEventMs.getCorpId(), openKfId,servicerUserId,status);
    }
}
