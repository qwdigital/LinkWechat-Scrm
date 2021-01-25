package com.linkwechat.wecom.factory.impl.user;

import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 删除成员事件
 * @date 2021/1/20 22:44
 **/
@Slf4j
@Component("delete_user")
public class WeCallBackDeleteUserImpl extends WeEventStrategy {
    @Autowired
    private IWeUserService weUserService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            weUserService.deleteUserNoToWeCom(message.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
