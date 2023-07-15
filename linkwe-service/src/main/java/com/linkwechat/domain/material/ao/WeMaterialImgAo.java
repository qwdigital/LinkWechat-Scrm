package com.linkwechat.domain.material.ao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 图片像素大小
     */
    private Long pixelSize;

    /**
     * 图片内存大小
     */
    private Long memorySize;


}
