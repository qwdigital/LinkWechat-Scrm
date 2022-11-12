package com.linkwechat.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sxw
 * @description 群发消息发送发送业务员
 * @date 2022/4/14 19:03
 **/
@Service
@Slf4j
public abstract class AbstractGroupMsgSendTaskService {

    @Autowired
    IWeMaterialService weMaterialService;

    /**
     * 具体业务处理消息
     * @param query
     * @return
     */
    public abstract void sendGroupMsg(WeAddGroupMessageQuery query);


    protected void getMediaId(List<WeMessageTemplate> messageTemplates) {
        Optional.ofNullable(messageTemplates).orElseGet(ArrayList::new).forEach(messageTemplate -> {
            if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getPicUrl()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getPicUrl()));
                messageTemplate.setMediaId(weMedia.getMediaId());
            } else if (ObjectUtil.equal(MessageType.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getPicUrl()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getPicUrl()));
                messageTemplate.setMediaId(weMedia.getMediaId());
            } else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplate.getMsgType())) {
//                WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
//                        , MessageType.IMAGE.getMessageType()
//                        , FileUtil.getName(messageTemplate.getMediaId()));
//                messageTemplate.setMediaId(weMedia.getMediaId());
            } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplate.getMsgType())) {
//                WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
//                        , MessageType.IMAGE.getMessageType()
//                        , FileUtil.getName(messageTemplate.getMediaId()));
//                messageTemplate.setMediaId(weMedia.getMediaId());
            }
        });
    }

}
