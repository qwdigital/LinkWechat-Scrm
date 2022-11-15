package com.linkwechat.factory.impl.kf;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.WeKfMenu;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import com.linkwechat.domain.kf.query.WeKfMsgTaskQuery;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
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

    @Autowired
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
        if(ObjectUtil.equal(3, weKfSyncMsgVo.getOrigin())){
            //获取接待信息
            WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
            if(kfPoolInfo == null){
                WeKfStateVo kfState = weKfMsgService.getKfServiceState(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
                if(WeKfStatusEnum.isEqualType(WeKfStatusEnum.UNTREATED, kfState.getServiceState())){
                    sendMsgTask(weKfSyncMsgVo.getCorpId(),weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId(),sendTime.getTime());
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
            }else
                //当前接待为未开始
                if(WeKfStatusEnum.isEqualType(WeKfStatusEnum.STAR_OR_END, kfPoolInfo.getStatus())){
                    sendMsgTask(weKfSyncMsgVo.getCorpId(), weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId(),sendTime.getTime());
                    //会话转接
                    weKfPoolService.transServiceHandler(weKfSyncMsgVo.getCorpId(),weKfSyncMsgVo.getOpenKfId(), weKfSyncMsgVo.getExternalUserId());
                }
            //发送消息
            sendApiMessage(message);
        }
    }

    private void sendApiMessage(JSONObject message) {
        String openKfId = message.getString("open_kfid");
        String externalUserId = message.getString("external_userid");
        Integer origin = message.getInteger("origin");
        String msgType = message.getString("msgtype");
        String corpId = message.getString("corpId");
        WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(corpId, openKfId, externalUserId);
        //当前为智能助手并且为客户发送消息
        if(WeKfStatusEnum.isEqualType(WeKfStatusEnum.AI_RECEPTION, kfPoolInfo.getStatus())
                && ObjectUtil.equal(WeKfStatusEnum.SERVICER.getType(), origin)
                && ObjectUtil.equal(MessageType.TEXT.getMessageType(),msgType)){
            JSONObject content = message.getJSONObject(msgType);
            if(content.get("menu_id") != null){
                WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(corpId,openKfId);
                if(weKfInfo == null){
                    return;
                }
                List<WeKfMenu> menuList = Optional.ofNullable(weKfInfo).map(WeKfInfoVo::getWelcome)
                        .orElseGet(ArrayList::new).stream()
                        .map(WeKfWelcomeInfo::getMenuList)
                        .flatMap(Collection::stream)
                        .filter(item -> ObjectUtil.equal(item.getClickId(), content.getString("menu_id")))
                        .collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(menuList)){
                    log.info("转人工菜单>>>>>>>>menuList:{}",JSONObject.toJSONString(menuList));
                    WeKfMsgQuery query = new WeKfMsgQuery();
                    query.setCorpid(corpId);
                    query.setMsgtype(MessageType.TEXT.getMessageType());
                    query.setTouser(externalUserId);
                    query.setOpen_kfid(openKfId);
                    if(ObjectUtil.equal(menuList.get(0).getType(),"manual")){
                        JSONObject msgBody = new JSONObject();
                        msgBody.put("content", "正在转接人工, 请稍等......");
                        query.setContext(msgBody);
                        qwKfClient.sendSessionMsg(query);
                        //会话转接
                        weKfPoolService.transServiceHandler(corpId,openKfId, externalUserId,false);
                    }else {
                        String text = menuList.get(0).getContent();
                        JSONObject msgBody = new JSONObject();
                        msgBody.put("content", text);
                        query.setContext(msgBody);
                        qwKfClient.sendSessionMsg(query);
                    }
                }
            }else {
                String contentString = content.getString("content");
                if(contentString.contains("人工")){
                    //会话转接
                    weKfPoolService.transServiceHandler(corpId, openKfId, externalUserId,false);
                }
            }

        }
    }


    private void sendMsgTask(String corpId, String openKfId, String externalUserId, long sendTime){
        WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(corpId, openKfId);
        if(weKfInfo == null){
            return;
        }
        WeKfMsgTaskQuery query = new WeKfMsgTaskQuery();
        query.setCorpId(corpId);
        query.setOpenKfId(openKfId);
        query.setExternalUserId(externalUserId);
        query.setSendTime(sendTime);
        //发送超时任务
        if (ObjectUtil.equal(1, weKfInfo.getEndNotice())) {
            Integer timeOut = weKfInfo.getTimeOut();
            long offset = 0L;
            if (ObjectUtil.equal(1, weKfInfo.getTimeOutType())) {
                offset = timeOut * 60 * 1000;
            } else {
                offset = timeOut * 60 * 60 * 1000;
            }
            long finalOffset = offset;
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(),rabbitMQSettingConfig.getWeKfChatTimeOutMsgRk(),JSONObject.toJSONString(query), message -> {
                //注意这里时间可使用long类型,毫秒单位，设置header
                message.getMessageProperties().setHeader("x-delay", finalOffset);
                return message;
            });
        }

        //发送会话结束任务
        if (ObjectUtil.equal(1, weKfInfo.getEndNotice())) {
            Integer endNoticeTime = weKfInfo.getEndNoticeTime();
            long offset = 0L;
            if (ObjectUtil.equal(1, weKfInfo.getEndTimeType())) {
                offset = endNoticeTime * 60 * 1000;
            } else {
                offset = endNoticeTime * 60 * 60 * 1000;
            }

            long finalOffset = offset;
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeKfChatMsgDelayEx(),rabbitMQSettingConfig.getWeKfChatEndMsgRk(),JSONObject.toJSONString(query), message -> {
                //注意这里时间可使用long类型,毫秒单位，设置header
                message.getMessageProperties().setHeader("x-delay", finalOffset);
                return message;
            });
        }
    }
}
