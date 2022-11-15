package com.linkwechat.domain.kf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服欢迎语
 * @date 2022/1/18 21:57
 **/
@ApiModel
@Data
public class WeKfWelcomeInfo {

    @ApiModelProperty("工作周期")
    private String workCycle;

    @ApiModelProperty("开始时间")
    private String beginTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("类型 1-文本 2-菜单")
    private Integer type;

    @ApiModelProperty("欢迎语")
    private String content;

    @ApiModelProperty("菜单列表")
    private List<WeKfMenu> menuList;


}
