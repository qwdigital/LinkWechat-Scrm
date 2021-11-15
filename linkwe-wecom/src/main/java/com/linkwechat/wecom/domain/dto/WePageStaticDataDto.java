package com.linkwechat.wecom.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description
 * @date 2021/2/25 11:21
 **/
@ApiModel
@Data
public class WePageStaticDataDto {

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("今日数据")
    private PageStaticData today;

    @ApiModelProperty("本周数据")
    private PageStaticData week;

    @ApiModelProperty("本月数据")
    private PageStaticData month;

    @ApiModel
    @Data
    public static class PageStaticData{
        //发起申请数
        @ApiModelProperty("发起申请数")
        private Integer newApplyCnt;
        //发起申请数差值
        @ApiModelProperty("发起申请数差值")
        private Integer newApplyCntDiff;
        //新增客户数
        @ApiModelProperty("新增客户数")
        private Integer newContactCnt;
        //新增客户数差值
        @ApiModelProperty("新增客户数差值")
        private Integer newContactCntDiff;

        @ApiModelProperty("新增客户群数")
        private Integer newChatCnt;

        @ApiModelProperty("新增客户群数差值")
        private Integer newChatCntDiff;
        //群新增人数
        @ApiModelProperty("群新增人数")
        private Integer newMemberCnt;
        //群新增人数差值
        @ApiModelProperty("群新增人数差值")
        private Integer newMemberCntDiff;
        //流失客户数
        @ApiModelProperty("流失客户数")
        private Long negativeFeedbackCnt;
        //流失客户数差值
        @ApiModelProperty("流失客户数差值")
        private Long negativeFeedbackCntDiff;
        //图表数据
        @ApiModelProperty("图表数据")
        private List<WePageCountDto> dataList;
    }
}
