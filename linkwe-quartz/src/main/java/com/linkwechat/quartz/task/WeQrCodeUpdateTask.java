package com.linkwechat.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.dto.WeContactWayDto;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.domain.query.qr.WeQrCodeEventQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrScopePartyVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrScopeUserVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrScopeVo;
import com.linkwechat.wecom.service.IWeQrCodeService;
import com.linkwechat.wecom.service.IWeQrScopeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 活码使用范围修改任务
 * @date 2021/11/13 23:02
 **/
@Slf4j
@Component("weQrCodeUpdateTask")
public class WeQrCodeUpdateTask {

    @Autowired
    private IWeQrScopeService weQrScopeService;

    @Autowired
    private IWeQrCodeService weQrCodeService;

    @Autowired
    private WeExternalContactClient externalContactClient;

    public void qrCodeUpdateTask(String qrCodeId) {
        List<WeQrScopeVo> weQrScopeList = weQrScopeService.getWeQrScopeByTime(DateUtil.formatDateTime(new Date()),qrCodeId);
        log.info("活码使用范围修改任务 weQrScopeList {}", JSONObject.toJSONString(weQrScopeList));
        if (CollectionUtil.isNotEmpty(weQrScopeList)) {
            Map<Long, List<WeQrScopeVo>> qrCodeMap = weQrScopeList.stream().collect(Collectors.groupingBy(WeQrScopeVo::getQrId));
            qrCodeMap.forEach((qrId,scopeList) ->{
                WeQrCode weQrCode = weQrCodeService.getById(qrId);
                WeQrScopeVo weCustomizeQrScope = scopeList.stream()
                        .filter(item -> ObjectUtil.equal(1, item.getType())).findFirst().orElse(null);
                if(weCustomizeQrScope != null){
                    extracted(weCustomizeQrScope,weQrCode.getConfigId());
                }else {
                    WeQrScopeVo weDefaultQrScope = scopeList.stream()
                            .filter(item -> ObjectUtil.equal(0, item.getType())).findFirst().orElse(null);
                    extracted(weDefaultQrScope,weQrCode.getConfigId());
                }
            });
        }
    }

    private void extracted(WeQrScopeVo weQrScope, String configId) {
        log.info("extracted ->>>>>>>>>>weQrScope:{}",JSONObject.toJSONString(weQrScope));
        WeExternalContactDto query = new WeExternalContactDto();
        query.setConfig_id(configId);
        WeContactWayDto contactWay = externalContactClient.getContactWay(query);
        if (contactWay != null && ObjectUtil.equal(0, contactWay.getErrcode())) {
            String userIds = "";
            String partys = "";
            String qrUserIds = "";
            String qrPartys = "";
            if (CollectionUtil.isNotEmpty(contactWay.getContactWay().getUser())) {
                userIds = String.join(",", contactWay.getContactWay().getUser());
            }
            if (CollectionUtil.isNotEmpty(contactWay.getContactWay().getParty())) {
                partys = contactWay.getContactWay().getParty().stream().map(String::valueOf).collect(Collectors.joining(","));
            }
            if (CollectionUtil.isNotEmpty(weQrScope.getWeQrUserList())) {
                qrUserIds = weQrScope.getWeQrUserList().stream().map(WeQrScopeUserVo::getUserId).collect(Collectors.joining(","));
            }
            if (CollectionUtil.isNotEmpty(weQrScope.getWeQrPartyList())) {
                qrPartys = weQrScope.getWeQrPartyList().stream().map(WeQrScopePartyVo::getParty).collect(Collectors.joining(","));
            }

            if (!ObjectUtil.equal(qrUserIds, userIds) || !ObjectUtil.equal(qrPartys, partys)) {
                WeExternalContactDto.WeContactWay weContactWay = new WeExternalContactDto.WeContactWay();
                weContactWay.setConfig_id(configId);
                if (StringUtils.isNotEmpty(qrUserIds)) {
                    weContactWay.setUser(qrUserIds.split(","));
                }
                if (StringUtils.isNotEmpty(qrPartys)) {
                    Long[] qrPartyList = Arrays.stream(qrPartys.split(","))
                            .map(s -> Long.parseLong(s.trim())).toArray(Long[]::new);
                    weContactWay.setParty(qrPartyList);
                }
                externalContactClient.updateContactWay(weContactWay);
            }
        }
    }


    @Service
    public static class RefreshQrCodeHandle implements ApplicationListener<WeQrCodeEventQuery>{
        @Autowired
        private WeQrCodeUpdateTask weQrCodeUpdateTask;

        @Async
        @Override
        public void onApplicationEvent(WeQrCodeEventQuery weQrCodeEventQuery) {
            weQrCodeUpdateTask.qrCodeUpdateTask(weQrCodeEventQuery.getQrId());
        }
    }
}
