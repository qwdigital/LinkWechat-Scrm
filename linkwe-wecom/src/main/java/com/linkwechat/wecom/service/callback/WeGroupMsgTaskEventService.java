package com.linkwechat.wecom.service.callback;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.domain.query.WeTaskEventQuery;
import com.linkwechat.wecom.service.IWeTaskFissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 群发回调通知处理
 * @date 2021/11/21 19:57
 **/
@Service
@Slf4j
public class WeGroupMsgTaskEventService implements ApplicationListener<WeTaskEventQuery> {

    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    @Override
    @Async
    public void onApplicationEvent(WeTaskEventQuery weTaskEventQuery) {
        log.info("群发回调通知处理：weTaskEventQuery:{}", JSONObject.toJSONString(weTaskEventQuery));
        Long businessId = weTaskEventQuery.getBusinessId();
        Integer taskSource = weTaskEventQuery.getTaskSource();
        Integer resultCode = weTaskEventQuery.getResultCode();
        if(ObjectUtil.equal(1,taskSource)){
            WeTaskFission fission = new WeTaskFission();
            fission.setId(businessId);
            fission.setFissStatus(resultCode);
            weTaskFissionService.updateById(fission);
        }
    }
}
