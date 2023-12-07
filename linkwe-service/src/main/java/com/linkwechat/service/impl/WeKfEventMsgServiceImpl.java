package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeKfEventMsg;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncMsgVo;
import com.linkwechat.mapper.WeKfEventMsgMapper;
import com.linkwechat.service.IWeKfEventMsgService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服事件消息表(WeKfEventMsg)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
@Service
public class WeKfEventMsgServiceImpl extends ServiceImpl<WeKfEventMsgMapper, WeKfEventMsg> implements IWeKfEventMsgService {

    @Override
    public void saveEventMsg(List<JSONObject> weKfEventMsgList) {
        if (CollectionUtil.isEmpty(weKfEventMsgList)) {
            return;
        }
        List<WeKfEventMsg> weKfEventMsgs = weKfEventMsgList.stream().map(msgInfo -> {
            WeKfSyncMsgVo weKfSyncMsg = JSONObject.parseObject(msgInfo.toJSONString(), WeKfSyncMsgVo.class);
            WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(
                    msgInfo.getJSONObject(weKfSyncMsg.getMsgType()).toJSONString(), WeKfSyncEventMsgVo.class);
            weKfSyncEventMs.setMsgId(weKfSyncMsg.getMsgId());
            weKfSyncEventMs.setSendTime(new Date(weKfSyncMsg.getSendTime() * 1000));
            weKfSyncEventMs.setOrigin(weKfSyncMsg.getOrigin());
            WeKfEventMsg weKfMsg = new WeKfEventMsg();
            BeanUtil.copyProperties(weKfSyncEventMs, weKfMsg);
            weKfMsg.setCorpId(SecurityUtils.getCorpId());
            weKfMsg.setExternalUserid(weKfSyncEventMs.getExternalUserId());
            return weKfMsg;
        }).collect(Collectors.toList());
        saveBatch(weKfEventMsgs);
    }
}
