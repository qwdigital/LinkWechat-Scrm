package com.linkwechat.domain.wecom.query.customer.msg;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

/**
 * @author danmo
 * @description 获取群发记录入参
 * @date 2021/10/3 16:25
 **/
@Data
public class WeGroupMsgListQuery extends WeBaseQuery {
    /**
     * 群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群
     */
    private String chat_type;

    /**
     * 群发任务记录开始时间
     */
    private Long start_time;

    /**
     * 群发任务记录结束时间
     */
    private Long end_time;

    /**
     * 群发任务创建人企业账号id
     */
    private String creator;

    /**
     * 创建人类型。0：企业发表 1：个人发表 2：所有，包括个人创建以及企业创建，默认情况下为所有类型
     */
    private Integer filter_type = 2;

    /**
     * 返回的最大记录数，整型，最大值100，默认值50
     */
    private Integer limit = 100;

    /**
     * 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
     */
    private String cursor;
}
