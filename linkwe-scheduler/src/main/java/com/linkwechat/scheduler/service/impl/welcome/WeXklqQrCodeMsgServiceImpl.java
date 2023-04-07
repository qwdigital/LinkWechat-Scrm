package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.domain.community.vo.WeCommunityWeComeMsgVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeCommunityNewGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 新客拉群欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeXklqQrCodeMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {

    @Autowired
    private IWeCommunityNewGroupService weCommunityNewGroupService;

    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("新客拉群欢迎语语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();

        WeCommunityWeComeMsgVo welcomeMsgByState = weCommunityNewGroupService.getWelcomeMsgByState(query.getState());
        if (welcomeMsgByState != null) {
            WeMessageTemplate textAtt = new WeMessageTemplate();
            textAtt.setMsgType(MessageType.TEXT.getMessageType());
            textAtt.setContent(welcomeMsgByState.getWelcomeMsg());
            templates.add(textAtt);
            WeMessageTemplate imageAtt = new WeMessageTemplate();
            imageAtt.setMsgType(MessageType.IMAGE.getMessageType());
            imageAtt.setPicUrl(welcomeMsgByState.getCodeUrl());
            templates.add(imageAtt);
            makeCustomerTag(query.getExternalUserID(), query.getUserID(), welcomeMsgByState.getTagList());
        }

        sendWelcomeMsg(query, templates);

    }
}
