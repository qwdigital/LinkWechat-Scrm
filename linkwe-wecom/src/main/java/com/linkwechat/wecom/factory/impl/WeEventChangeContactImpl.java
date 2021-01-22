package com.linkwechat.wecom.factory.impl;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.factory.WeStrategyBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 企微变更事件通知处理类
 * @date 2020/11/9 17:17
 **/
@Service
@Slf4j
public class WeEventChangeContactImpl implements WeCallBackEventFactory {
    @Autowired
    private WeStrategyBeanFactory weStrategyBeanFactory;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        //新增: create_user 更新: update_user 删除:delete_user
        String changeType = message.getChangeType();
        weStrategyBeanFactory.getResource(changeType, message);
    }
}
