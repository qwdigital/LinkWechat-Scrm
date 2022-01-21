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
import java.util.List;

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

    private Integer trajectoryType;
    //外部联系人id
    private String externalUserid;
    //文案内容
    private String content;

    @TableField(exist = false)
    private String trackContent;




    //0:正常;1:删除;2:完成
    private String status;

    //当前员工的id
    private String userId;





    //1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;


    //跟进时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date trackTime;

    //是否来自同步:1是;0否
    private  Boolean isSynch;

    //标题
    private String title;

    @TableField(exist = false)
    private String userName;


    @TableField(exist = false)
    private List<TrajectRel> trajectRelList;

    @Data
    @Builder
    public static class TrajectRel{
        private String userId;
        private String customerId;
    }
}
