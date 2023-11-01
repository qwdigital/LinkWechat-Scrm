package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.vo.WeCommunityWeComeMsgVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.tag.vo.WeTagVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeCommunityNewGroupService;
import com.linkwechat.service.IWeTagService;
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

    @Autowired
    private IWeTagService iWeTagService;

    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("新客拉群欢迎语语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();


        List<WeCommunityNewGroup> weCommunityNewGroups = weCommunityNewGroupService.list(new LambdaQueryWrapper<WeCommunityNewGroup>()
                .eq(WeCommunityNewGroup::getEmplCodeState, query.getState()));



        if (CollectionUtil.isNotEmpty(weCommunityNewGroups)) {
            WeCommunityNewGroup weCommunityNewGroup = weCommunityNewGroups.stream().findFirst().get();


            WeMessageTemplate textAtt = new WeMessageTemplate();
            textAtt.setMsgType(MessageType.TEXT.getMessageType());
            textAtt.setContent(weCommunityNewGroup.getWelcomeMsg());
            templates.add(textAtt);

            WeMessageTemplate linkTpl = new WeMessageTemplate();
            linkTpl.setMsgType(MessageType.LINK.getMessageType());
            linkTpl.setTitle(weCommunityNewGroup.getLinkTitle());
            linkTpl.setPicUrl(weCommunityNewGroup.getLinkCoverUrl());
            linkTpl.setDescription(weCommunityNewGroup.getLinkDesc());
            linkTpl.setLinkUrl(weCommunityNewGroup.getCommunityNewGroupUrl());
            templates.add(linkTpl);


            //设置标签
            if(StringUtils.isNotEmpty(weCommunityNewGroup.getTagList())){
                List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId, weCommunityNewGroup.getTagList().split(",")));
                if(CollectionUtil.isNotEmpty(weTags)){
                    List<WeTagVo> weTagVos=new ArrayList<>();
                    weTags.stream().forEach(weTag -> {
                        weTagVos.add(WeTagVo.builder()
                                .tagId(weTag.getTagId())
                                .tagName(weTag.getName())
                                .build());

                    });
                    makeCustomerTag(query.getExternalUserID(), query.getUserID(),weTagVos);
                }
            }




        }

        sendWelcomeMsg(query, templates);

    }
}
