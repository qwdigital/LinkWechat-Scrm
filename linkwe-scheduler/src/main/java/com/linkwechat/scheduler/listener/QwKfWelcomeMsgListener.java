package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfOriginEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.kf.WeKfMenu;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfMsgVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfMsgService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客服欢迎语
 * @date 2022/1/20 23:05
 **/
@Slf4j
@Component
public class QwKfWelcomeMsgListener {

    @Resource
    private QwKfClient qwKfClient;

    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.kf-welcome-msg:Qu_KfWelcomeMsg}")
    public void subscribe(String msg, Channel channel, Message message) throws IOException {

        log.info("客服欢迎语消息监听: msg:{}", msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msg, WeKfSyncEventMsgVo.class);
        if (weKfSyncEventMs.getWelcomeCode() == null) {
            return;
        }
        WeKfMsgQuery weKfMsgQuery = new WeKfMsgQuery();
        weKfMsgQuery.setCode(weKfSyncEventMs.getWelcomeCode());
        weKfMsgQuery.setCorpid(weKfSyncEventMs.getCorpId());
        WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getOpenKfId());
        if (weKfInfo == null) {
            return;
        }
        Integer receptionType = weKfInfo.getReceptionType();
        List<WeKfWelcomeInfo> welcomeInfos = weKfInfo.getWelcome();
        if (ObjectUtil.equal(1, receptionType)) {
            //人工欢迎语
            Integer splitTime = weKfInfo.getSplitTime();
            //判断是否分时段
            if (ObjectUtil.equal(1, splitTime)) {
                JSONObject msgBody = new JSONObject();
                if(CollectionUtil.isNotEmpty(welcomeInfos)){
                    msgBody.put("content", welcomeInfos.get(0).getContent());
                }else {
                    msgBody.put("content", "默认欢迎语");
                }
                weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                weKfMsgQuery.setContext(msgBody);
            } else {
                for (WeKfWelcomeInfo welcomeInfo : welcomeInfos) {
                    String workCycle = welcomeInfo.getWorkCycle();
                    int week = DateUtil.thisDayOfWeek() - 1 == 0 ? 7 : DateUtil.thisDayOfWeek() - 1;
                    String beginTime = welcomeInfo.getBeginTime();
                    String endTime = welcomeInfo.getEndTime();
                    if(ObjectUtil.equal("00:00",endTime)){
                        endTime = "24:00";
                    }
                    if (welcomeInfo.getType().equals(1)) {
                        //文本欢迎语
                        if (workCycle.contains(String.valueOf(week)) && DateUtil.isIn(new Date(), DateUtil.parseTimeToday(beginTime)
                                , DateUtil.parseTimeToday(endTime))) {
                            JSONObject msgBody = new JSONObject();
                            msgBody.put("content", welcomeInfo.getContent());
                            weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                            weKfMsgQuery.setContext(msgBody);
                        }
                    }/* else if (welcomeInfo.getType().equals(2)) {
                    //菜单欢迎语
                    JSONObject msgmenuObj = new JSONObject();
                    msgmenuObj.put("head_content", welcomeInfo.getContent());
                    List<WeKfMenu> kfMenuList = welcomeInfo.getMenuList();
                    List<JSONObject> menuList = getMenuList(kfMenuList);
                    msgmenuObj.put("list", menuList);
                    weKfMsgQuery.put("msgtype", WeKfMsgTypeEnum.MSGMENU.getType());
                    weKfMsgQuery.put(WeKfMsgTypeEnum.MSGMENU.getType(), msgmenuObj);
                }*/
                }
            }
        } else {
            //机器人发送菜单欢迎语
            for (WeKfWelcomeInfo welcomeInfo : welcomeInfos) {
                if (CollectionUtil.isNotEmpty(welcomeInfo.getMenuList())) {
                    JSONObject msgmenuObj = new JSONObject();
                    msgmenuObj.put("head_content", welcomeInfo.getContent());
                    List<WeKfMenu> kfMenuList = welcomeInfo.getMenuList();
                    List<JSONObject> menuList = getMenuList(kfMenuList);
                    msgmenuObj.put("list", menuList);
                    weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.MSGMENU.getType());
                    weKfMsgQuery.setContext(msgmenuObj);
                } else {
                    JSONObject msgBody = new JSONObject();
                    msgBody.put("content", welcomeInfo.getContent());
                    weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
                    weKfMsgQuery.setContext(msgBody);
                }
            }
        }
        if(Objects.isNull(weKfMsgQuery.getContext())){
            return;
        }
        WeKfMsgVo weKfMsgVo = qwKfClient.sendMsgOnEvent(weKfMsgQuery).getData();
        if(Objects.nonNull(weKfMsgVo) && StringUtils.isNotEmpty(weKfMsgVo.getMsgId())){
            WeKfMsg weKfMsg = new WeKfMsg();
            weKfMsg.setMsgId(weKfMsgVo.getMsgId());
            weKfMsg.setMsgType(weKfMsgQuery.getMsgtype());
            weKfMsg.setOpenKfId(weKfSyncEventMs.getOpenKfId());
            weKfMsg.setExternalUserid(weKfSyncEventMs.getExternalUserId());
            weKfMsg.setContent(weKfMsgQuery.getContext().toJSONString());
            weKfMsg.setOrigin(WeKfOriginEnum.SERVICER_WELCOME.getType());
            weKfMsg.setCorpId(weKfSyncEventMs.getCorpId());
            weKfMsg.setSendTime(new Date());
            weKfMsgService.save(weKfMsg);
        }
    }

    private List<JSONObject> getMenuList(List<WeKfMenu> kfMenuList) {
        List<JSONObject> menuList = kfMenuList.stream().map(menuMsg -> {
            JSONObject obj = new JSONObject();
            if (ObjectUtil.equal("manual", menuMsg.getType())) {
                obj.put("type", "click");
            } else {
                obj.put("type", menuMsg.getType());
            }
            if (ObjectUtil.equal("click", menuMsg.getType()) || ObjectUtil.equal("manual", menuMsg.getType())) {
                JSONObject msgObj = new JSONObject();
                msgObj.put("id", menuMsg.getClickId());
                msgObj.put("content", menuMsg.getName());
                obj.put("click", msgObj);
            } else if (ObjectUtil.equal("view", menuMsg.getType())) {
                JSONObject msgObj = new JSONObject();
                msgObj.put("url", menuMsg.getUrl());
                msgObj.put("content", menuMsg.getName());
                obj.put("view", msgObj);
            } else if (ObjectUtil.equal("miniprogram", menuMsg.getType())) {
                JSONObject msgObj = new JSONObject();
                msgObj.put("appid", menuMsg.getAppId());
                msgObj.put("pagepath", menuMsg.getUrl());
                msgObj.put("content", menuMsg.getName());
                obj.put("miniprogram", msgObj);
            }
            return obj;
        }).collect(Collectors.toList());
        return menuList;
    }
}
