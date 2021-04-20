package com.linkwechat.wecom.factory.impl.customergroup;

import com.linkwechat.common.enums.CallbackEventUpdateDetail;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import com.linkwechat.wecom.service.IWeGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户群变更事件
 * @date 2021/1/20 0:39
 **/
@Slf4j
@Component("update")
public class WeCallBackUpdateGroupImpl extends WeEventStrategy {
    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupCodeActualService groupCodeActualService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            weGroupService.updateWeGroup(message.getChatId());
            String updateDetail = message.getUpdateDetail();
            if (updateDetail.equals(CallbackEventUpdateDetail.ADD_MEMBER.getType())) {
                // 添加成员，该群的实际群活码扫码次数需要加1
                groupCodeActualService.updateScanTimesByChatId(message.getChatId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update>>>>>>>>>param:{},ex:{}",message.getChatId(),e);
        }
    }
}
