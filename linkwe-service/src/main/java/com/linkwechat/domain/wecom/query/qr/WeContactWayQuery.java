package com.linkwechat.domain.wecom.query.qr;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @Description 活码入参
 * @date 2021/12/2 16:11
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeContactWayQuery extends WeBaseQuery {
    /**
     * 联系方式的配置id
     */
    private String config_id;

    /**
     * 「联系我」创建起始时间戳, 默认为90天前
     */
    private Long start_time;

    /**
     * 「联系我」创建结束时间戳, 默认为当前时间
     */
    private Long end_time;

    /**
     * 分页查询使用的游标，为上次请求返回的 next_cursor
     */
    private String cursor;

    /**
     * 每次查询的分页大小，默认为100条，最多支持1000条
     */
    private Integer limit;
}
