package com.linkwechat.wecom.factory.impl.tag;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeTagGroupService;
import com.linkwechat.wecom.service.IWeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 企业客户标签删除事件
 * @date 2021/1/21 1:18
 **/
@Slf4j
@Component("deleteCustomerTag")
public class WeCallBackDeleteCustomerTagImpl extends WeEventStrategy {
    @Autowired
    private IWeTagGroupService weTagGroupService;
    @Autowired
    private IWeTagService weTagService;
    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            switch (message.getTagType()){
                case tagGroup:
                    weTagGroupService.deleteTagGroup(message.getTagId());
                    break;
                case tag:
                    weTagService.deleteTag(message.getTagId());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("deleteCustomerTag>>>>>>>>>param:{},ex:{}",message.getId(),e);
        }
    }
}
