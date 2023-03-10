package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 拉新业务欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeLxQrCodeMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {


    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("红包卡券拉新欢迎语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();


        sendWelcomeMsg(query, templates);

    }
}
