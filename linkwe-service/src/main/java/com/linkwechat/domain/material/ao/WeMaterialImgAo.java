package com.linkwechat.domain.material.ao;

import lombok.*;

/**
 * @Author 狗头军师
 * @Description 素材图片
 * @Date 2022/10/8 16:32
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeMaterialImgAo {
   private String materialUrl;
   private String materialName;
}
