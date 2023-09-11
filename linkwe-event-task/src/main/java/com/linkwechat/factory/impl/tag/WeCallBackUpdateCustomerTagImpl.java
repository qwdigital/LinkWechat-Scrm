package com.linkwechat.factory.impl.tag;

import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerTagVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeTagGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 企业客户标签变更事件
 * @date 2021/1/21 1:21
 **/
@Slf4j
@Component("updateCustomerTag")
public class WeCallBackUpdateCustomerTagImpl extends WeEventStrategy {

    @Autowired
    private IWeTagGroupService weTagGroupService;



    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerTagVo customerTagInfo = (WeBackCustomerTagVo) message;
        try {
            weTagGroupService.synchWeGroupAndTag(customerTagInfo.getId(),customerTagInfo.getTagType());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateCustomerTag>>>>>>>>>param:{},ex:{}",customerTagInfo.getId(),e);
        }
    }
}
