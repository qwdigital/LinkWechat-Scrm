package com.linkwechat.factory.impl.party;

import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackDeptVo;
import com.linkwechat.factory.WeEventStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 创建部门事件
 * @date 2021/1/20 22:54
 **/
@Slf4j
@Component("create_party")
public class WeCallBackCreatePartyImpl extends WeEventStrategy {

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackDeptVo deptInfo = (WeBackDeptVo) message;
    }
}
