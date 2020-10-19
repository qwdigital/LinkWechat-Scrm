package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @description: 标签组
 * @author: HaoN
 * @create: 2020-10-17 11:02
 **/
@Data
public class WeCropGroupTagDto111 extends WeResultDto{



    private List<WeCropGroupTagDto111> tag_group;


//    public  static WeCropGroupTagDto.WeCropGroupTag createWeCropGroupTag(WeTagGroup weTagGroup){
//        WeCropGroupTag weCropGroupTag=new WeCropGroupTag();
//        if(null != weTagGroup){
//            weCropGroupTag.setGroup_id(weTagGroup.getId().toString());
//            weCropGroupTag.setGroup_name(weTagGroup.getGourpName());
//
//            List<WeTag> weTags = weTagGroup.getWeTags();
//            if(CollectionUtil.isNotEmpty(weTags)){
//                List<WeCropTag> weCropTags=new ArrayList<>();
//                weTags.stream().forEach(k->{
//                    WeCropTag weCropTag = new WeCropTag();
//                    weCropTag.setName(k.getName());
//                    weCropTags.add(
//                            weCropTag
//                    );
//                });
//                weCropGroupTag.setTag(weCropTags);
//            }
//
//
//        }
//
//        return weCropGroupTag;
//    }


}
