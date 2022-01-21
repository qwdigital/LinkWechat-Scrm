package com.linkwechat.wecom.factory.impl.party;

import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackDeptVo;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 删除部门事件
 * @date 2021/1/20 23:04
 **/
@Slf4j
@Component("delete_party")
public class WeCallBackDeletePartyImpl extends WeEventStrategy {
    @Autowired
    private IWeDepartmentService weDepartmentService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackDeptVo deptInfo = (WeBackDeptVo) message;
        try {
            weDepartmentService.removeById(deptInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
