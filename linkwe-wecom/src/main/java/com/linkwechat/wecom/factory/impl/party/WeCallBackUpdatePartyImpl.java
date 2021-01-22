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
 * @description 修改部门事件
 * @date 2021/1/20 23:00
 **/
@Slf4j
@Component("update_party")
public class WeCallBackUpdatePartyImpl extends WeEventStrategy {
    @Autowired
    private IWeDepartmentService weDepartmentService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            weDepartmentService.updateById(setWeDepartMent(message));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
