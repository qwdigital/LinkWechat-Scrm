package com.linkwechat.framework.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.linkwechat.wecom.service.IWeChatContactMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * @author danmo
 * @description 会话存档订阅者
 * @date 2021/7/27 23:11
 **/
@Slf4j
@Component
public class ChatMsgListener implements MessageListener {

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        JSONObject jsonObject = JSONObject.parseObject(new String(message.getBody()));
        log.info(">> 订阅消息,会话存档消息：{}", jsonObject.toJSONString());

        Threads.SINGLE_THREAD_POOL.submit(() -> msgContextHandle(jsonObject));
    }


    private void msgContextHandle(JSONObject jsonObject){
        WeChatContactMsg weChatContactMsg = new WeChatContactMsg();
        weChatContactMsg.setMsgId(jsonObject.getString("msgid"));
        weChatContactMsg.setFromId(jsonObject.getString("from"));
        weChatContactMsg.setToList(CollectionUtil.join(jsonObject.getJSONArray("tolist"), ","));
        weChatContactMsg.setAction(jsonObject.getString("action"));
        weChatContactMsg.setRoomId(jsonObject.getString("roomid"));
        weChatContactMsg.setMsgType(jsonObject.getString("msgtype"));
        weChatContactMsg.setMsgTime(new Date(jsonObject.getLong("msgtime")));
        weChatContactMsg.setSeq(jsonObject.getLong("seq"));
        String objectString = jsonObject.getString(jsonObject.getString("msgtype"));
        if(StringUtils.isNotEmpty(objectString)){
            weChatContactMsg.setContact(objectString);
        }else if("external_redpacket".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(jsonObject.getString("redpacket"));
        }else if("docmsg".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(jsonObject.getString("doc"));
        }else if("markdown".equals(weChatContactMsg.getMsgType())
                || "news".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(jsonObject.getString("info"));
        }
        if(!weChatContactMsg.getMsgId().contains("external")){
            weChatContactMsg.setIsExternal(1);
        }
        LambdaQueryWrapper<WeChatContactMsg> wrapper = new LambdaQueryWrapper<WeChatContactMsg>()
                .eq(WeChatContactMsg::getMsgId, weChatContactMsg.getMsgId())
                .eq(WeChatContactMsg::getFromId, weChatContactMsg.getFromId());
        if(!weChatContactMsgService.update(weChatContactMsg,wrapper)){
            weChatContactMsgService.save(weChatContactMsg);
        }
    }
}
