package com.linkwechat.factory.impl.party;

import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackDeptVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.fegin.QwSysDeptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author danmo
 * @description 修改部门事件
 * @date 2021/1/20 23:00
 **/
@Slf4j
@Component("update_party")
public class WeCallBackUpdatePartyImpl extends WeEventStrategy {

    @Resource
    private QwSysDeptClient qwSysDeptClient;
    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackDeptVo deptInfo = (WeBackDeptVo) message;
        SysDeptQuery query = new SysDeptQuery();
        query.setDeptIds(Collections.singletonList(Long.parseLong(deptInfo.getId())));
        qwSysDeptClient.updateDept(query);
    }
}
