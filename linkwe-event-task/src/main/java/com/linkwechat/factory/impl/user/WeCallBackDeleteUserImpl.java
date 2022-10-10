package com.linkwechat.factory.impl.user;

import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackUserVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.fegin.QwSysUserClient;
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
    private QwSysUserClient qwSysUserClient;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackUserVo userInfo = (WeBackUserVo) message;
        try {

            qwSysUserClient.callBackRemove(userInfo.getToUserName(),
                    userInfo.getUserID().split(",")
            );

        } catch (Exception e) {
            log.error("deleteUser>>>>>>>>>param:{},ex:{}",((WeBackUserVo) message).getUserID(),e);
        }
    }


}
