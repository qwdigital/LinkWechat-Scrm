package com.linkwechat.domain.moments.dto;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取客户朋友圈发表后的可见客户列表
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/12 10:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomentsSendResultDTO extends WeBaseQuery {

    /**
     * 朋友圈Id
     */
    private String moment_id;

    /**
     * 企业发表成员userid
     */
    private String userid;

    /**
     * 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
     * 再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空
     */
    private String cursor;

    /**
     * 返回的最大记录数，整型，最大值5000，默认值3000，超过最大值时取默认值
     */
    private String limit;


}
