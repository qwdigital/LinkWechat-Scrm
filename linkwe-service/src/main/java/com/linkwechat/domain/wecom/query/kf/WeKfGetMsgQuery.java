package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 客服消息接口入参
 * @date 2021/12/13 10:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfGetMsgQuery extends WeBaseQuery {

    /**
     * 上一次调用时返回的next_cursor
     */
    private String cursor;

    /**
     * 回调事件返回的token字段，10分钟内有效
     */
    private String token;

    /**
     * 期望请求的数据量，默认值和最大值都为1000
     */
    private String limit;

    private Integer voice_format = 0;

    private String open_kfid;
}
