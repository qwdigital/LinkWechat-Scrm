package com.linkwechat.wecom.domain.dto.tag;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.WeTagGroup;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 标签组
 * @author: HaoN
 * @create: 2020-10-17 20:02
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeCropGroupTagDto {
    private String group_id;
    private String group_name;
    private Integer order;
    private Boolean deleted;
    private Long create_time;
    private List<WeCropTagDto> tag;

    /**
     * 获取新增的实体
     * @param weTagGroup
     * @return
     */
    public static WeCropGroupTagDto transformAddTag(WeTagGroup weTagGroup){
        WeCropGroupTagDto weCropGroupTagDto=WeCropGroupTagDto.builder()
//                .group_id(weTagGroup.getGroupId())
                .group_name(weTagGroup.getGourpName())
                .build();
        List<WeTag> newAddWeTag = weTagGroup.getAddWeTags();
        if(CollectionUtil.isNotEmpty(newAddWeTag)){
            //新增的标签
//            List<WeTag> newAddWeTag
//                    = weTags.stream().filter(v -> StringUtils.isEmpty(v.getTagId())).collect(Collectors.toList());


            if(CollectionUtil.isNotEmpty(newAddWeTag)){
                List<WeCropTagDto> weTagDtos=new ArrayList<>();

                newAddWeTag.stream().forEach(weTag -> {

                    weTagDtos.add(WeCropTagDto.builder()
                            .id(weTag.getTagId())
                            .name(weTag.getName())
                            .build());
                });

                weCropGroupTagDto.setTag(weTagDtos);
            }



        }



        return weCropGroupTagDto;
    }
}
