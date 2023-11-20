package com.linkwechat.factory.impl.tag;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.TagSynchEnum;
import com.linkwechat.domain.WeFlowerCustomerTagRel;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.WeTagGroup;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerTagVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeTagGroupService;
import com.linkwechat.service.IWeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.linkwechat.service.IWeFlowerCustomerTagRelService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;

    @Autowired
    private IWeTagService weTagService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerTagVo customerTagInfo = (WeBackCustomerTagVo) message;
        try {

            handlerTag(customerTagInfo);
//            if(TagSynchEnum.TAG_TYPE.getType().equals(customerTagInfo.getTagType())){//删除标签
//                weTagService.remove(new LambdaQueryWrapper<WeTag>()
//                        .in(WeTag::getTagId, Arrays.asList(customerTagInfo.getId().split(","))));
//            }else if(TagSynchEnum.GROUP_TAG_TYPE.getType().equals(customerTagInfo.getTagType())){//删除标签组
//                weTagGroupService.remove(
//                        new LambdaQueryWrapper<WeTagGroup>()
//                                .in(WeTagGroup::getGroupId,Arrays.asList(customerTagInfo.getId().split(",")))
//                );
//            }
        } catch (Exception e) {
            log.error("deleteCustomerTag>>>>>>>>>param:{},ex:{}",customerTagInfo.getId(),e);
        }
    }

    /**
     * 处理相关标签
     * @param customerTagInfo
     */
    @Transactional
    public void handlerTag(WeBackCustomerTagVo customerTagInfo){

        List<String> tagIds=new ArrayList<>();

        if(TagSynchEnum.TAG_TYPE.getType().equals(customerTagInfo.getTagType())){ //删除标签
            tagIds=Arrays.asList(customerTagInfo.getId().split(","));
            weTagService.remove(new LambdaQueryWrapper<WeTag>()
                    .in(WeTag::getTagId, Arrays.asList(customerTagInfo.getId().split(","))));
        }else if(TagSynchEnum.GROUP_TAG_TYPE.getType().equals(customerTagInfo.getTagType())){ //删除标签组

            weTagGroupService.remove(
                    new LambdaQueryWrapper<WeTagGroup>()
                            .in(WeTagGroup::getGroupId,Arrays.asList(customerTagInfo.getId().split(",")))
            );

            //标签组获取标签
            List<WeTag> weTags = weTagService.list(new LambdaQueryWrapper<WeTag>()
                    .in(WeTag::getGroupId, Arrays.asList(customerTagInfo.getId().split(","))));
            if(CollectionUtil.isNotEmpty(weTags)){

                tagIds=weTags.stream().map(WeTag::getTagId).collect(Collectors.toList());
                weTagService.remove(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId,tagIds));
            }

        }

        List<WeFlowerCustomerTagRel> tagRels = weFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                .in(WeFlowerCustomerTagRel::getTagId, tagIds));

        if(CollectionUtil.isNotEmpty(tagRels)){

            if(weFlowerCustomerTagRelService.removeByIds(tagRels.stream().map(WeFlowerCustomerTagRel::getId)
                    .collect(Collectors.toList()))){
                tagRels.stream().forEach(k->{

                    iWeCustomerService.updateWeCustomerTagIds(k.getUserId(),k.getExternalUserid());

                });

            }

        }






    }
}
