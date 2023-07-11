package com.linkwechat.domain.leads.leads.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.sea.query.VisibleRange;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 线索批量分配
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/07 13:39
 */
@Data
public class WeClientLeadsAllocationParam {

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
     * 线索状态(0待分配，1跟进中，2已上门，3已退回，4线索转移)
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


    //==========以上为搜索条件==========


    /**
     * 线索是否全选（全选true，不全选false)
     */
    private boolean all = false;

    /**
     * 公海ID,all为true时，必填
     */
    private Long seaId;

    /**
     * 线索Id集合,all为false时，必填
     */
    private List<Long> leadsIds;

    /**
     * 全选时未被选中的线索Id
     */
    private List<Long> unLeadsIds;

    /**
     * 部门，包含企微的部门Id
     */
    private VisibleRange.DeptRange depts;

    /**
     * 岗位，包含岗位名称
     */
    private VisibleRange.PostRange posts;

    /**
     * 用户，企微的用户id
     */
    private VisibleRange.UserRange weUserIds;

    /**
     * 分配类型：0平均分配 1根据上门转化
     *
     * @see com.linkwechat.common.enums.LeadsAllocationTypeEnum
     */
    private Integer allocationType;

    /**
     * 本次勾选的线索量，如（60%）,只填数字60
     */
    private String first;

    /**
     * 转化率大于多少的成员（如50%），只填数字50
     */
    private String second;

}
