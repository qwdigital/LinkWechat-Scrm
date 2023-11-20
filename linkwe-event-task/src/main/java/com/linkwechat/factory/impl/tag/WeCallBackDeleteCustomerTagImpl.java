package com.linkwechat.factory.impl.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.TagSynchEnum;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.WeTagGroup;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerTagVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeTagGroupService;
import com.linkwechat.service.IWeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerTagVo customerTagInfo = (WeBackCustomerTagVo) message;
        try {

            if(TagSynchEnum.TAG_TYPE.getType().equals(customerTagInfo.getTagType())){//删除标签
                weTagService.remove(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId, Arrays.asList(customerTagInfo.getId().split(","))));




            }else if(TagSynchEnum.GROUP_TAG_TYPE.getType().equals(customerTagInfo.getTagType())){//删除标签组
                weTagGroupService.remove(
                        new LambdaQueryWrapper<WeTagGroup>()
                                .in(WeTagGroup::getGroupId,Arrays.asList(customerTagInfo.getId().split(",")))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("deleteCustomerTag>>>>>>>>>param:{},ex:{}",customerTagInfo.getId(),e);
        }
    }
}
