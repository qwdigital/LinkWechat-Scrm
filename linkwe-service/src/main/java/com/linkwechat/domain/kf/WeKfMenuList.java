package com.linkwechat.domain.kf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服菜单
 * @date 2022/1/18 21:57
 **/
@ApiModel
@Data
public class WeKfMenuList {

    @ApiModelProperty("欢迎语")
    private String headContent;

    @ApiModelProperty("菜单列表")
    private List<WeKfMenu> list;

    
}
