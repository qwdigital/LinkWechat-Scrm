package com.linkwechat.wecom.factory.impl.tag;

import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerTagVo;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeTagGroupService;
import com.linkwechat.wecom.service.IWeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 创建标签事件
 * @date 2021/1/21 1:18
 **/
@Slf4j
@Component("createCustomerTag")
public class WeCallBackCreateCustomerTagImpl extends WeEventStrategy {
    @Autowired
    private IWeTagGroupService weTagGroupService;
    @Autowired
    private IWeTagService weTagService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerTagVo customerTagInfo = (WeBackCustomerTagVo) message;
        try {
            switch (customerTagInfo.getTagType()){
                case tagGroup:
                    weTagGroupService.createTagGroup(customerTagInfo.getId());
                    break;
                case tag:
                    weTagService.creatTag(customerTagInfo.getId());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("createCustomerTag>>>>>>>>>param:{},ex:{}",customerTagInfo.getId(),e);
        }
    }
}
