package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服客户场景分析
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfSceneAnalysisVo {

    @ApiModelProperty("访问客户总数")
    private int visitCnt;

    @ApiModelProperty("咨询客户总数")
    private int consultCnt;

    @ApiModelProperty("接待客户总数")
    private int receptionCnt;

    @ApiModelProperty("今日新增访问客户")
    private int tdVisitCnt;

    @ApiModelProperty("昨日新增访问客户")
    private int ydVisitCnt;

    @ApiModelProperty("今日新增咨询客户")
    private int tdConsultCnt;

    @ApiModelProperty("昨日新增咨询客户")
    private int ydConsultCnt;

    @ApiModelProperty("今日新增接待客户")
    private int tdReceptionCnt;

    @ApiModelProperty("昨日新增接待客户")
    private int ydReceptionCnt;

    public int getYdVisitCnt() {
        return this.tdVisitCnt - ydVisitCnt;
    }

    public int getYdConsultCnt() {
        return this.tdConsultCnt - ydConsultCnt;
    }

    public int getYdReceptionCnt() {
        return this.tdReceptionCnt - ydReceptionCnt;
    }
}
