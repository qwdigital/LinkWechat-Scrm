package com.linkwechat.domain.strategic.crowd.vo;

import com.alibaba.fastjson.JSONArray;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeStrategicCrowdDetailVo {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("分组ID")
    private Long groupId;

    @ApiModelProperty(value = "状态 1、待计算 2、计算中 3、计算完成 4、计算失败")
    private Integer status;

    @ApiModelProperty(value = "人群数量")
    private Integer crowdNum;


    @ApiModelProperty("更新方式 1：手动 2：自动")
    private Integer type = 1;

    @ApiModelProperty("筛选条件")
    private List<WeStrategicCrowdSwipe> swipe;

    public void setStr2Swipe(String swipe) {
        this.swipe =  JSONArray.parseArray(swipe,WeStrategicCrowdSwipe.class);
    }
}
