package com.linkwechat.wecom.factory.impl.user;

import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackUserVo;
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
    public void eventHandle(WeBackBaseVo message) {
        WeBackUserVo userInfo = (WeBackUserVo) message;
        try {
            weUserService.deleteUserNoToWeCom(userInfo.getUserID());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
