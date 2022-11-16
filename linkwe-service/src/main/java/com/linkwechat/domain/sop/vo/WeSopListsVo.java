package com.linkwechat.domain.sop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSopListsVo {
    //sop主键
    private Long sopBaseId;

    // sop业务类(1:新客sop;2:活动节日sop;3:客户转化sop;4:新群培育sop;5:周期营销sop;6:特定宣发sop)
    private Integer businessType;
    //sop名称
    private String sopName;
    //执行成员,多个使用逗号隔开
    private String executeUser;
    //推送所需天数
    private int pushNeedDayNumber;
    //推送所需次数
    private int pushNeedUserNumber;
    //创建人
    private String createBy;
    //sop状态
    private Integer sopState;
    //SOP推送方式(1:企业微信发送;2:手动发送)
    private Integer sendType;
    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createTime;
}
