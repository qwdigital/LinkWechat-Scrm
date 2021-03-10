package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description
 * @date 2021/2/25 11:21
 **/
@Data
public class WePageStaticDataDto {

    /**
     * 更新时间
     */
    private String updateTime;

    private PageStaticData today;

    private PageStaticData week;

    private PageStaticData month;

    @Data
    public static class PageStaticData{
        //发起申请数
        private Integer newApplyCnt;
        //发起申请数差值
        private Integer newApplyCntDiff;
        //新增客户数
        private Integer newContactCnt;
        //新增客户数差值
        private Integer newContactCntDiff;
        //群新增人数
        private Integer newMemberCnt;
        //群新增人数差值
        private Integer newMemberCntDiff;
        //流失客户数
        private Long negativeFeedbackCnt;
        //流失客户数差值
        private Long negativeFeedbackCntDiff;
        //图表数据
        private List<WePageCountDto> dataList;
    }
}
