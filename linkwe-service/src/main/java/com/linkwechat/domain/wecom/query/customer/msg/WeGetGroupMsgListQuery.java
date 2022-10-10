package com.linkwechat.domain.wecom.query.customer.msg;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

/**
 * @author danmo
 * @description 群发成员发送任务列表入参
 * @date 2021/10/19 16:49
 **/
@Data
public class WeGetGroupMsgListQuery extends WeBaseQuery {

    /**
     * 群发消息的id
     */
    private String msgid;

    /**
     * 发送成员userid
     */
    private String userid;

    /**
     * 返回的最大记录数，整型，最大值1000，默认值500
     */
    private Integer limit;

    /**
     * 用于分页查询的游标
     */
    private String cursor;
}
