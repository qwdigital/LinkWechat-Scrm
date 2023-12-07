package com.linkwechat.factory.impl.customergroup;

import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerGroupVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeGroupService;
import com.linkwechat.service.IWeSopExecuteTargetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户群解散事件
 * @date 2021/1/20 1:07
 **/
@Slf4j
@Component("dismiss")
public class WeCallBackDismissImpl extends WeEventStrategy {

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeSopExecuteTargetService iWeSopExecuteTargetService;


    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerGroupVo customerGroupInfo = (WeBackCustomerGroupVo) message;
        try {
            weGroupService.deleteWeGroup(customerGroupInfo.getChatId());
            //相关sop设置为异常结束
            iWeSopExecuteTargetService.sopExceptionEnd(customerGroupInfo.getChatId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("dismiss>>>>>>>>>param:{},ex:{}",customerGroupInfo.getChatId(),e);
        }finally {
            SecurityContextHolder.remove();
        }
    }
}
