package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

/**
 * 活动轨迹相关
 */
@Data
@TableName("we_customer_trajectory")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerTrajectory {
    @TableId
    private String id;
    //轨迹类型(1:信息动态;2:社交动态;3:活动规则;4:待办动态)
    private Integer trajectoryType;
    //外部联系人id
    private String externalUserid;
    //文案内容
    private String content;
    //处理日期
    private Date createDate;

    //处理开始时间
    private Time startTime;

    //处理结束时间
    private Time endTime;



    //0:正常;1:删除;2:完成
    private String status;

    //当前员工的id
    private String userId;


    //当前应用的id
    private String agentId;


    //创建时间
    private Date createTime;



}
