package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.common.utils.uuid.IdUtils;
import com.linkwechat.domain.WeChatContactMsg;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.service.IWeChatContactMsgService;
import com.linkwechat.service.IWeCorpAccountService;
import com.rabbitmq.client.Channel;
import com.tencent.wework.Finance;
import com.tencent.wework.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * @author danmo
 * @description 会话存档消息监听
 * @date 2022/5/6 15:39
 **/
@Slf4j
@Component
public class QwChatMsgAuditListener {

    private long sdk;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Resource
    private QwFileClient qwFileClient;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.chat-msg-audit:Qu_ChatMsgAudit}")
    public void subscribe(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info("会话存档消息监听：msg:{}",msg);

            msgContextHandle(JSONObject.parseObject(msg));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("会话存档消息监听-消息处理失败 msg:{},error:{}",msg,e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
        }
    }


    private void msgContextHandle(JSONObject msgData){
        WeChatContactMsg weChatContactMsg = new WeChatContactMsg();
        weChatContactMsg.setMsgId(msgData.getString("msgid"));
        weChatContactMsg.setFromId(msgData.getString("from"));
        weChatContactMsg.setToList(CollectionUtil.join(msgData.getJSONArray("tolist"), ","));
        weChatContactMsg.setAction(msgData.getString("action"));
        weChatContactMsg.setRoomId(msgData.getString("roomid"));
        weChatContactMsg.setMsgType(msgData.getString("msgtype"));
        weChatContactMsg.setMsgTime(new Date(msgData.getLong("msgtime") == null? msgData.getLong("time") : msgData.getLong("msgtime")));
        weChatContactMsg.setSeq(msgData.getLong("seq"));
        if(msgData.getString("user") != null){
            weChatContactMsg.setFromId(msgData.getString("user"));
            weChatContactMsg.setToList(msgData.getString("user"));
        }

        String msgType = msgData.getString("msgtype");
        if (StringUtils.isNotEmpty(msgType)) {
            getSwitchType(msgData, msgType);
        }


        String objectString = msgData.getString(msgData.getString("msgtype"));
        if(StringUtils.isNotEmpty(objectString)){
            weChatContactMsg.setContact(objectString);
        }else if("external_redpacket".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(msgData.getString("redpacket"));
        }else if("docmsg".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(msgData.getString("doc"));
        }else if("markdown".equals(weChatContactMsg.getMsgType())
                || "news".equals(weChatContactMsg.getMsgType())){
            weChatContactMsg.setContact(msgData.getString("info"));
        }
        if(!weChatContactMsg.getMsgId().contains("external")){
            weChatContactMsg.setIsExternal(1);
        }


        LambdaQueryWrapper<WeChatContactMsg> wrapper = new LambdaQueryWrapper<WeChatContactMsg>()
                .eq(WeChatContactMsg::getMsgId, weChatContactMsg.getMsgId())
                .eq(WeChatContactMsg::getFromId, weChatContactMsg.getFromId());
        weChatContactMsgService.saveOrUpdate(weChatContactMsg,wrapper);
    }



    private void getSwitchType(JSONObject realJsonData, String msgType) {
        switch (msgType) {
            case "image":
                setMediaImageData(realJsonData, msgType);
                break;
            case "voice":
                setMediaVoiceData(realJsonData, msgType);
                break;
            case "video":
                setMediaVideoData(realJsonData, msgType);
                break;
            case "emotion":
                setMediaEmotionData(realJsonData, msgType);
                break;
            case "file":
                setMediaFileData(realJsonData, msgType);
                break;
            case "mixed":
                setMediaMixedData(realJsonData, msgType);
                break;
            case "meeting_voice_call":
                setMediaMeetingVoiceCallData(realJsonData, msgType);
                break;
            case "voip_doc_share":
                setVoipDocShareData(realJsonData, msgType);
                break;
            default:
                break;
        }
    }

    private void setVoipDocShareData(JSONObject realJsonData, String msgType) {
        JSONObject voipDocShare = realJsonData.getJSONObject(msgType);
        String fileName = voipDocShare.getString("filename");
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaMeetingVoiceCallData(JSONObject realJsonData, String msgType) {
        JSONObject meetingVoiceCall = realJsonData.getJSONObject(msgType);
        String fileName = realJsonData.getString("voiceid") + ".amr";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaMixedData(JSONObject realJsonData, String msgType) {
        JSONObject mixedData = realJsonData.getJSONObject(msgType);
        JSONArray items = mixedData.getJSONArray("item");
        items.stream().map(item -> (JSONObject) item).forEach(item -> {
            getSwitchType(item, item.getString("type"));
        });
    }

    private void setMediaFileData(JSONObject realJsonData, String msgType) {
        JSONObject emotionData = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        String filename = emotionData.getString("filename");
        //String fileext = emotionData.getString("fileext");
        //String fileName = filename+"."+fileext;
        getPath(realJsonData, msgType, filename);
    }


    private void setMediaImageData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".jpg";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaVoiceData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".amr";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaVideoData(JSONObject realJsonData, String msgType) {
        String fileName = IdUtils.simpleUUID() + ".mp4";
        getPath(realJsonData, msgType, fileName);
    }

    private void setMediaEmotionData(JSONObject realJsonData, String msgType) {
        String fileName = "";
        JSONObject emotionData = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                .orElse(realJsonData.getJSONObject("content"));
        Integer type = emotionData.getInteger("type");
        switch (type) {
            case 1:
                fileName = IdUtils.simpleUUID() + ".gif";
                break;
            case 2:
                fileName = IdUtils.simpleUUID() + ".png";
                break;
            default:
                break;
        }
        getPath(realJsonData, msgType, fileName);
    }

    private void getPath(JSONObject realJsonData, String msgType, String fileName) {
        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(null);

        if(null != corpAccount){

            FinanceService financeService = new FinanceService(corpAccount.getCorpId(), corpAccount.getChatSecret(), corpAccount.getFinancePrivateKey(),
                    linkWeChatConfig.getFincaceProxyConfig().getProxy(),
                    linkWeChatConfig.getFincaceProxyConfig().getPaswd()
            );

            String filePath = getFilePath(msgType);
            JSONObject data = Optional.ofNullable(realJsonData.getJSONObject(msgType))
                    .orElse(realJsonData.getJSONObject("content"));
            String sdkfileid = data.getString("sdkfileid");
            try {
                financeService.getMediaData(sdkfileid,filePath, fileName);
                File file = new File(filePath, fileName);
                MockMultipartFile multipartFile = new MockMultipartFile(FileUtil.getPrefix(fileName), fileName,null, new FileInputStream(file));
                FileEntity fileEntity = qwFileClient.upload(multipartFile).getData();
                data.put("attachment", fileEntity.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (realJsonData.containsKey("content")) {
                realJsonData.put("content", data);
            } else {
                realJsonData.put(msgType, data);
            }

        }


    }

    private String getFilePath(String msgType) {
        return LinkWeChatConfig.getProfile() + "/" + msgType + "/" + DateUtils.getDate();
    }


}
