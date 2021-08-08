package com.linkwechat.wecom.domain.dto.tag;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.wecom.domain.WeTag;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 企业标签
 * @author: HaoN
 * @create: 2020-10-17 20:03
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeCropTagDto {
    private String id;
    private String name;
    private String order;
    private Boolean deleted;
    private Long create_time;

    public static List<WeCropTagDto> transFormto(List<WeTag> weTags){

        List<WeCropTagDto> weCropDelDtoList=new ArrayList();
        if(CollectionUtil.isNotEmpty(weTags)){

            weTags.stream().forEach(k->{
                weCropDelDtoList.add(
                        WeCropTagDto.builder()
                                .name(k.getName())
                                .id(k.getTagId())
                                .build()
                );

            });

        }
        return weCropDelDtoList;

    }
}
