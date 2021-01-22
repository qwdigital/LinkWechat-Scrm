package com.linkwechat.wecom.factory.impl.party;

import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 创建部门事件
 * @date 2021/1/20 22:54
 **/
@Slf4j
@Component("create_party")
public class WeCallBackCreatePartyImpl extends WeEventStrategy {
    @Autowired
    private IWeDepartmentService weDepartmentService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            weDepartmentService.insertWeDepartmentNoToWeCom(setWeDepartMent(message));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
