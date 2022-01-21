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
 * @description 成员更新事件
 * @date 2021/1/20 22:28
 **/
@Slf4j
@Component("update_user")
public class WeCallBackUpdateUserImpl extends WeEventStrategy {
    @Autowired
    private IWeUserService weUserService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackUserVo userInfo = (WeBackUserVo) message;
        try {
            weUserService.update2Data(setWeUserData(userInfo));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
