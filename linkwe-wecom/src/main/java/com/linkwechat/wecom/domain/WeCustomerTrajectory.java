package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.util.Date;

/**
 * 活动轨迹相关
 */
@Data
@TableName("we_customer_trajectory")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerTrajectory extends BaseEntity {
    @TableId
    private String id;
    //轨迹类型(1:信息动态;2:社交动态;3:跟进动态;4:待办动态)
    private Integer trajectoryType;
    //外部联系人id
    private String externalUserid;
    //文案内容
    private String content;
    //处理日期
    private Date createDate;

    //处理开始时间
    private String startTime;

    //处理结束时间
    private String endTime;


    //文案子内容
    private String contentSub;



    //0:正常;1:删除;2:完成
    private String status;

    //当前员工的id
    private String userId;


    //当前应用的id
    private String agentId;


    //1:跟踪中;2:待跟进;3:已拒绝;4:已成交;
    private Integer trackState;

    //跟进内容
    @TableField(exist = false)
    private String trackContent;

    //跟进时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date trackTime;

    //是否来自同步:1是;0否
    private  Boolean isSynch;

    //标题
    private String title;

    @TableField(exist = false)
    private String userName;
}
