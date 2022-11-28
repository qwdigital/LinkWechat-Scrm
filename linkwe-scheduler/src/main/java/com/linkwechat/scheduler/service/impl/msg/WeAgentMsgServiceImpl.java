package com.linkwechat.scheduler.service.impl.msg;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeAgentMsg;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.scheduler.service.AbstractAppMsgService;
import com.linkwechat.service.IWeAgentMsgService;
import com.linkwechat.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * @author danmo
 * @description 应用消息
 * @date 2022/4/14 23:45
 **/
@Service("WeAgentMsgService")
@Slf4j
public class WeAgentMsgServiceImpl extends AbstractAppMsgService {

    private Long callBackId;

    @Autowired
    private IWeAgentMsgService weAgentMsgService;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Override
    protected WeAppMsgQuery getWeAppMsg(QwAppMsgBody appMsgBody) {
        callBackId = appMsgBody.getCallBackId();
        WeAgentMsg weAgentMsg = weAgentMsgService.getById(appMsgBody.getCallBackId());
        if (Objects.isNull(weAgentMsg)) {
            throw new WeComException("未找到对应消息数据");
        }
        WeAppMsgQuery query = new WeAppMsgQuery();
        query.setAgentid(String.valueOf(weAgentMsg.getAgentId()));
        query.setCorpid(appMsgBody.getCorpId());
        if (CollectionUtil.isNotEmpty(appMsgBody.getCorpUserIds())) {
            query.setTouser(String.join("|", appMsgBody.getCorpUserIds()));
        } else if (CollectionUtil.isNotEmpty(appMsgBody.getDeptIds())) {
            query.setToparty(String.join("|", appMsgBody.getDeptIds()));
        } else if (CollectionUtil.isNotEmpty(appMsgBody.getTagIds())) {
            query.setTotag(String.join("|", appMsgBody.getTagIds()));
        }
        WeMessageTemplate messageTemplates = appMsgBody.getMessageTemplates();
        if (MessageType.TEXT.getMessageType().equals(messageTemplates.getMsgType())) {
            query.setMsgtype(MessageType.TEXT.getMessageType());
            query.setText(WeAppMsgQuery.Text.builder().content(messageTemplates.getContent()).build());
        } else if (MessageType.IMAGE.getMessageType().equals(messageTemplates.getMsgType())) {
            query.setMsgtype(MessageType.IMAGE.getMessageType());
            WeAppMsgQuery.Image image = new WeAppMsgQuery.Image();
            String picUrl = messageTemplates.getPicUrl();
            WeMediaVo weMediaVo = weMaterialService.uploadTemporaryMaterial(picUrl, MessageType.IMAGE.getMessageType(), FileUtil.getName(messageTemplates.getPicUrl()));
            image.setMedia_id(weMediaVo.getMediaId());
            query.setImage(image);
        } else if (ObjectUtil.equal(MessageType.NEWS.getMessageType(), messageTemplates.getMsgType())) {
            query.setMsgtype(MessageType.NEWS.getMessageType());
            WeAppMsgQuery.News news = new WeAppMsgQuery.News();
            WeAppMsgQuery.Article article = new WeAppMsgQuery.Article();
            article.setTitle(appMsgBody.getMessageTemplates().getTitle());
            article.setDescription(appMsgBody.getMessageTemplates().getDescription());
            article.setUrl(appMsgBody.getMessageTemplates().getLinkUrl());
            article.setPicurl(appMsgBody.getMessageTemplates().getPicUrl());
            if (StringUtils.isNotEmpty(appMsgBody.getMessageTemplates().getAppId())) {
                article.setAppid(appMsgBody.getMessageTemplates().getAppId());
                article.setPagepath(appMsgBody.getMessageTemplates().getLinkUrl());
            }
            news.setArticles(Collections.singletonList(article));
            query.setNews(news);
        } else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplates.getMsgType())) {
            query.setMsgtype(MessageType.VIDEO.getMessageType());
            WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplates.getFileUrl()
                    , MessageType.VIDEO.getMessageType()
                    , FileUtil.getName(messageTemplates.getFileUrl()));
            WeAppMsgQuery.Video video = new WeAppMsgQuery.Video();
            video.setMedia_id(weMedia.getMediaId());
            video.setDescription(appMsgBody.getMessageTemplates().getDescription());
            video.setTitle(appMsgBody.getMessageTemplates().getTitle());
            query.setVideo(video);
        } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplates.getMsgType())) {
            query.setMsgtype(MessageType.FILE.getMessageType());
            WeMediaVo weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplates.getFileUrl()
                    , MessageType.FILE.getMessageType()
                    , FileUtil.getName(messageTemplates.getFileUrl()));
            WeAppMsgQuery.File file = new WeAppMsgQuery.File();
            file.setMedia_id(weMedia.getMediaId());
            query.setFile(file);
        }
        return query;
    }


    @Override
    protected void callBackResult(WeAppMsgVo appMsgVo) {
        log.info(">>>>>>【任务发送结果】 callBackId:{} ,appMsgVo:{}", callBackId, JSONObject.toJSONString(appMsgVo));
        WeAgentMsg weAgentMsg = new WeAgentMsg();
        weAgentMsg.setId(callBackId);
        weAgentMsg.setInvalidUser(appMsgVo.getInvalidUser());
        weAgentMsg.setInvalidParty(appMsgVo.getInvalidParty());
        weAgentMsg.setInvalidTag(appMsgVo.getInvalidTag());
        weAgentMsg.setUnlicensedUser(appMsgVo.getUnlicenseduser());
        weAgentMsg.setMsgId(appMsgVo.getMsgId());
        weAgentMsg.setResponseCode(appMsgVo.getResponseCode());
        weAgentMsg.setSendTime(new Date());
        if (appMsgVo.getErrMsg() != null && !Objects.equals(0,appMsgVo.getErrCode())) {
            weAgentMsg.setStatus(3);
        } else {
            weAgentMsg.setStatus(2);
        }
        weAgentMsgService.updateById(weAgentMsg);
    }

}
