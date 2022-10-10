package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("we_customer_track_record")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerTrackRecord extends BaseEntity {

    @TableId
    private Long id;

    //跟进时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date trackTime;

    //跟进动态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;

    //跟进标题
    private String trackTitle;

    //跟进内容(content变为trackContent)
    private String trackContent;

    //客户id
    private String externalUserid;

    //企业微信员工id（userid变为weUserId）
    private String weUserId;

}
