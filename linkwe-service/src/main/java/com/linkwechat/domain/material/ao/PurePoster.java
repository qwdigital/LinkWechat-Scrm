package com.linkwechat.domain.material.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 单纯根据图片生成海报
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/04 16:53
 */
@Data
public class PurePoster {

    /**
     * 背景图片
     */
    @ApiModelProperty(value = "背景图片")
    private String backgroundImgPath;

    @ApiModelProperty(value = "海报背景宽度")
    private Integer width;

    @ApiModelProperty(value = "海报背景高度")
    private Integer height;
    /**
     * 海报组件数组
     */
    private List<WePosterSubassembly> posterSubassemblyList;

}
