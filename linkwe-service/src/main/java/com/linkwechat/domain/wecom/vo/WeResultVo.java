package com.linkwechat.domain.wecom.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @Description 通用返回值
 * @date 2021/12/2 17:02
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeResultVo {
    /**
     * 接口返回错误码
     */
    private Integer errCode;

    /**
     * 接口返回错误信息
     */
    private String errMsg;

    /**
     * 分页游标，用于查询下一个分页的数据，无更多数据时不返回
     */
    private String nextCursor;
}
