package com.linkwechat.wecom.domain.dto.moments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 朋友圈列表详情
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomentsListDetailParamDto{

    //朋友圈记录开始时间。
    private Long start_time;
    //朋友圈记录结束时间。
    private Long end_time;
    //朋友圈创建人的userid
    private String creator;
    //朋友圈类型。0：企业发表 1：个人发表 2：所有，包括个人创建以及企业创建，默认情况下为所有类型
    private Integer filter_type;
    //用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
    private String cursor;
    //返回的最大记录数，整型，最大值100，默认值100，超过最大值时取默认值
    private Integer limit;

}
