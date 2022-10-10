package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客群分析
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeGroupAnalysisVo {

    @ApiModelProperty("客群总数")
    private int totalCnt;

    @ApiModelProperty("今日新增客群数")
    private int tdGroupAddCnt;

    @ApiModelProperty("昨日新增客群数")
    private int ydGroupAddCnt;

    @ApiModelProperty("今日客群解散数")
    private int tdGroupDissolveCnt;

    @ApiModelProperty("昨客群解散数")
    private int ydGroupDissolveCnt;

    @ApiModelProperty("客群成员总数")
    private int memberTotalCnt;

    @ApiModelProperty("含员工数")
    private int memberUserCnt;

    @ApiModelProperty("今日新增客群成员")
    private int tdMemberAddCnt;

    @ApiModelProperty("昨日新增客群成员")
    private int ydMemberAddCnt;

    @ApiModelProperty("今日流失客群成员")
    private int tdMemberQuitCnt;

    @ApiModelProperty("昨日流失客群成员")
    private int ydMemberQuitCnt;

    public int getYdGroupAddCnt() {
        return this.tdGroupAddCnt - ydGroupAddCnt;
    }

    public int getYdGroupDissolveCnt() {
        return this.tdGroupDissolveCnt - ydGroupDissolveCnt;
    }

    public int getYdMemberAddCnt() {
        return this.tdMemberAddCnt - ydMemberAddCnt;
    }

    public int getYdMemberQuitCnt() {
        return this.tdMemberQuitCnt - ydMemberQuitCnt;
    }
}
