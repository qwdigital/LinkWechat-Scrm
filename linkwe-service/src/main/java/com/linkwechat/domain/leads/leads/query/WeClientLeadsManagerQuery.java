package com.linkwechat.domain.leads.leads.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 线索
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/04 16:57
 */
@Data
public class WeClientLeadsManagerQuery {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 三级渠道
     */
    private Long threeLevelChannel;

    /**
     * 线索状态(0待分配，1跟进中，2以上门，3已退回，4线索转移)
     */
    @NotNull(message = "线索状态必填")
    private Integer leadsStatus;

    /**
     * 线索跟进中的子状态。从we_leads_pending_status表中取值
     */
    private String leadsPendingStatus;

    /**
     * 留电时间-开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leavePhoneTimeStart;

    /**
     * 留电时间-结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leavePhoneTimeEnd;

    /**
     * 咨询项目
     */
    private String consult;

    /**
     * 回收次数
     */
    private Integer recoveryTimes;

    /**
     * 末次回收原因
     */
    private String returnReason;

    /**
     * 所属公海
     */
    @NotNull(message = "所属公海必填")
    private Long seaId;

}
