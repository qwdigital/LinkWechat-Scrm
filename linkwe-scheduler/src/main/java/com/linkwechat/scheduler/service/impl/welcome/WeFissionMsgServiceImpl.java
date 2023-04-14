package com.linkwechat.scheduler.service.impl.welcome;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.WeTaskFissionRecord;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务宝欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeFissionMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {

//    @Autowired
//    private IWeTaskFissionService weTaskFissionService;
//
//    @Autowired
//    private IWeTaskFissionRecordService weTaskFissionRecordService;

    @Override
    public void msgHandle(WeBackCustomerVo query) {

//        log.info("任务宝列表欢迎语消息 query：{}", JSONObject.toJSONString(query));
//        List<WeMessageTemplate> templates = new ArrayList<>();
//
//        String fissionRecordId = query.getState().substring(WelcomeMsgTypeEnum.FISSION_PREFIX.getType().length());
//        WeMessageTemplate textAtt = new WeMessageTemplate();
//        textAtt.setMsgType(MessageType.TEXT.getMessageType());
//        WeTaskFissionRecord weTaskFissionRecord = weTaskFissionRecordService.getById(Long.valueOf(fissionRecordId));
//        if (weTaskFissionRecord != null) {
//            WeTaskFission weTaskFission = weTaskFissionService.selectWeTaskFissionById(weTaskFissionRecord.getTaskFissionId());
//            if (weTaskFission != null) {
//                textAtt.setContent(weTaskFission.getWelcomeMsg());
//            }
//        }
//        if (StringUtils.isEmpty(textAtt.getContent())) {
//            //如果租户配置了欢迎语则返回租户欢迎语,如果租户没配置欢迎语则使用系统全局默认的欢迎语
//            textAtt.setContent(welcomeMsgDefault);
//        }
//        templates.add(textAtt);
//
//        sendWelcomeMsg(query, templates);
    }
}
