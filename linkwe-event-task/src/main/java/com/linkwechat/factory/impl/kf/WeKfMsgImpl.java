package com.linkwechat.factory.impl.kf;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeKfOriginEnum;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.WeKfMenu;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import com.linkwechat.domain.kf.query.WeKfMsgTaskQuery;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfMsgVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfStateVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfMsgService;
import com.linkwechat.service.IWeKfPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客服消息实现类
 * @date 2022/1/20 22:10
 **/
@Slf4j
@Service("kf_msg")
public class WeKfMsgImpl extends WeKfEventStrategy {

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Resource
    private QwKfClient qwKfClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服会话消息处理: msg:{}", msgStr);
        WeKfSyncMsgVo weKfSyncMsgVo = JSONObject.parseObject(msgStr, WeKfSyncMsgVo.class);
        Date sendTime = new Date(weKfSyncMsgVo.getSendTime() * 1000);

        //客户开口
        if (ObjectUtil.equal(WeKfOriginEnum.CUSTOMER_SEND.getType(), weKfSyncMsgVo.getOrigin())) {
            //获取接待信息
            WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
            if (kfPoolInfo == null) {
                WeKfStateVo kfState = weKfMsgService.getKfServiceState(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
                if (WeKfStatusEnum.isEqualType(WeKfStatusEnum.UNTREATED, kfState.getServiceState())) {
                    WeKfPool weKfPool = new WeKfPool();
                    weKfPool.setCorpId(weKfSyncMsgVo.getCorpId());
                    weKfPool.setOpenKfId(weKfSyncMsgVo.getOpenKfId());
                    weKfPool.setExternalUserId(weKfSyncMsgVo.getExternalUserId());
                    weKfPool.setEnterTime(sendTime);
                    weKfPool.setStatus(kfState.getServiceState());
                    weKfPoolService.saveKfPoolInfo(weKfPool);
                    //会话转接
                    weKfPoolService.transServiceHandler(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
                }
            }
            //当前接待为未开始
            else if (WeKfStatusEnum.isEqualType(WeKfStatusEnum.STAR_OR_END, kfPoolInfo.getStatus())) {
                //会话转接
                weKfPoolService.transServiceHandler(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
            }
            //发送消息
            sendApiMessage(message);
        }
        ThreadUtil.execute(() -> sendMsgTask(message));
    }

    private void sendMsgTask(JSONObject message) {
        try {
            WeKfSyncMsgVo weKfSyncMsgVo = JSONObject.parseObject(message.toJSONString(), WeKfSyncMsgVo.class);
            WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
            WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId());
            if(Objects.isNull(kfPoolInfo) || Objects.isNull(weKfInfo)){
                return;
            }
            WeKfMsgTaskQuery query = new WeKfMsgTaskQuery();
            query.setCorpId(kfPoolInfo.getCorpId());
            query.setOpenKfId(kfPoolInfo.getOpenKfId());
            query.setExternalUserId(kfPoolInfo.getExternalUserId());
            query.setPoolId(kfPoolInfo.getId());
            query.setSendTime(DateUtil.date(weKfSyncMsgVo.getSendTime() * 1000).toString());
            if (Objects.equals(WeKfOriginEnum.CUSTOMER_SEND.getType(), weKfSyncMsgVo.getOrigin())) {
                if (ObjectUtil.equal(1, weKfInfo.getKfTimeOutNotice())) {
                    log.info("客服提醒任务提醒--------------");
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatKfTimeOutMsgRk(), JSONObject.toJSONString(query), msg -> {
                        Integer kfTimeOut = weKfInfo.getKfTimeOut();
                        long offset = 0L;
                        if (ObjectUtil.equal(1, weKfInfo.getKfTimeOutType())) {
                            offset = kfTimeOut * 60 * 1000;
                        } else {
                            offset = kfTimeOut * 60 * 60 * 1000;
                        }
                        //注意这里时间可使用long类型,毫秒单位，设置header
                        msg.getMessageProperties().setHeader("x-delay", offset);
                        return msg;
                    });
                }
            } else if (Objects.equals(WeKfOriginEnum.SERVICER_SEND.getType(), weKfSyncMsgVo.getOrigin())) {

                if (ObjectUtil.equal(1, weKfInfo.getTimeOutNotice())) {
                    log.info("客户超时任务提醒--------------");
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatTimeOutMsgRk(), JSONObject.toJSONString(query), msg -> {
                        Integer timeOut = weKfInfo.getTimeOut();
                        long offset = 0L;
                        if (ObjectUtil.equal(1, weKfInfo.getTimeOutType())) {
                            offset = timeOut * 60 * 1000;
                        } else {
                            offset = timeOut * 60 * 60 * 1000;
                        }
                        //注意这里时间可使用long类型,毫秒单位，设置header
                        msg.getMessageProperties().setHeader("x-delay", offset);
                        return msg;
                    });
                }
                //结束
                if (ObjectUtil.equal(1, weKfInfo.getEndNotice())) {
                    log.info("客户结束任务提醒--------------");
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(), rabbitMQSettingConfig.getWeKfChatEndMsgRk(), JSONObject.toJSONString(query), msg -> {
                        Integer endNoticeTime = weKfInfo.getEndNoticeTime();
                        long offset = 0L;
                        if (ObjectUtil.equal(1, weKfInfo.getEndTimeType())) {
                            offset = endNoticeTime * 60 * 1000;
                        } else {
                            offset = endNoticeTime * 60 * 60 * 1000;
                        }
                        //注意这里时间可使用long类型,毫秒单位，设置header
                        msg.getMessageProperties().setHeader("x-delay", offset);
                        return msg;
                    });
                }
            }
        } catch (Exception e) {
            log.info("发送客服消息任务MQ失败：msgInfo：{}", message.toJSONString());
        }
    }

    /**
     * 机器人会话
     *
     * @param message
     */
    private void sendApiMessage(JSONObject message) {
        String openKfId = message.getString("open_kfid");
        String externalUserId = message.getString("external_userid");
        Integer origin = message.getInteger("origin");
        String msgType = message.getString("msgtype");
        String corpId = message.getString("corpId");
        WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(corpId, openKfId, externalUserId);
        if (Objects.isNull(kfPoolInfo)) {
            return;
        }
        //当前为智能助手并且为客户发送消息
        if (WeKfStatusEnum.isEqualType(WeKfStatusEnum.AI_RECEPTION, kfPoolInfo.getStatus())
                && ObjectUtil.equal(WeKfOriginEnum.CUSTOMER_SEND.getType(), origin)
                && ObjectUtil.equal(MessageType.TEXT.getMessageType(), msgType)) {
            JSONObject content = message.getJSONObject(msgType);
            if (content.get("menu_id") != null) {
                WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(corpId, openKfId);
                if (weKfInfo == null) {
                    return;
                }
                List<WeKfMenu> menuList = Optional.ofNullable(weKfInfo).map(WeKfInfoVo::getWelcome)
                        .orElseGet(ArrayList::new).stream()
                        .map(WeKfWelcomeInfo::getMenuList)
                        .flatMap(Collection::stream)
                        .filter(item -> ObjectUtil.isNotEmpty(item) &&  ObjectUtil.equal(item.getClickId(), content.getString("menu_id")))
                        .collect(Collectors.toList());
                WeKfMsgQuery query = getWeKfMsgQuery(openKfId, externalUserId, corpId);
                if (CollectionUtil.isNotEmpty(menuList)) {
                    log.info("转人工菜单>>>>>>>>menuList:{}", JSONObject.toJSONString(menuList));
                    if (ObjectUtil.equal(menuList.get(0).getType(), "manual")) {
                        sendAIMsg(query, true);
                    } else {
                        String text = menuList.get(0).getContent();
                        JSONObject msgBody = new JSONObject();
                        msgBody.put("content", text);
                        query.setContext(msgBody);
                        sendAIMsg(query, false);
                    }
                }
            } else {
                String contentString = content.getString("content");
                if (contentString.contains("人工")) {
                    WeKfMsgQuery query = getWeKfMsgQuery(openKfId, externalUserId, corpId);
                    sendAIMsg(query, true);
                }
            }
        }
    }

    private WeKfMsgQuery getWeKfMsgQuery(String openKfId, String externalUserId, String corpId) {
        WeKfMsgQuery query = new WeKfMsgQuery();
        query.setCorpid(corpId);
        query.setMsgtype(MessageType.TEXT.getMessageType());
        query.setTouser(externalUserId);
        query.setOpen_kfid(openKfId);
        return query;
    }

    private void sendAIMsg(WeKfMsgQuery query, Boolean isTransServicer) {
        if (isTransServicer) {
            JSONObject msgBody = new JSONObject();
            msgBody.put("content", "正在转接人工, 请稍等......");
            query.setContext(msgBody);
        }
        WeKfMsgVo weKfMsgVo = qwKfClient.sendSessionMsg(query).getData();
        if (Objects.nonNull(weKfMsgVo) && StringUtils.isNotEmpty(weKfMsgVo.getMsgId())) {
            WeKfMsg weKfMsg = new WeKfMsg();
            weKfMsg.setMsgId(weKfMsgVo.getMsgId());
            weKfMsg.setMsgType(query.getMsgtype());
            weKfMsg.setOpenKfId(query.getOpen_kfid());
            weKfMsg.setExternalUserid(query.getTouser());
            weKfMsg.setContent(query.getContext().toJSONString());
            weKfMsg.setOrigin(WeKfOriginEnum.SERVICER_AI.getType());
            weKfMsg.setCorpId(SecurityUtils.getCorpId());
            weKfMsg.setSendTime(new Date());
            weKfMsgService.save(weKfMsg);
        }
        //会话转接
        if(isTransServicer){
            weKfPoolService.transServiceHandler(query.getCorpid(), query.getOpen_kfid(), query.getTouser(), false);
        }
    }
}
