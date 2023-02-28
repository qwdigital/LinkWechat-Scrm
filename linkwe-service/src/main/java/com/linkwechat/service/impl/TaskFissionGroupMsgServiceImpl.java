package com.linkwechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.service.AbstractGroupMsgService;
import com.linkwechat.service.IWeTaskFissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sxw
 * @description 裂变任务回调状态
 * @date 2022/4/14 23:45
 **/
@Service("TaskFissionGroupMsgService")
@Slf4j
public class TaskFissionGroupMsgServiceImpl extends AbstractGroupMsgService {

    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    @Override
    public void callBackHandler(JSONObject query) {
        String businessId = query.getString("businessId");
        Integer status = query.getInteger("status");
        WeTaskFission fission = new WeTaskFission();
        fission.setId(Long.valueOf(businessId));
        if(status != null && status == 1){
            fission.setFissStatus(1);
        }else {
            fission.setFissStatus(-1);
        }
        weTaskFissionService.updateById(fission);
    }
}
