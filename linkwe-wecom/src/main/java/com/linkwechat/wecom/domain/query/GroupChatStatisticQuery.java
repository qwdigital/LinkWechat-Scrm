package com.linkwechat.wecom.domain.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author danmo
 */
@Data
public class GroupChatStatisticQuery {
    @ApiModelProperty(value = "起始日期的时间戳")
    private Long day_begin_time;

    @ApiModelProperty(value = "结束日期的时间戳")
    private Long day_end_time;

    @ApiModelProperty(value = "群主过滤")
    private OwnerFilter ownerFilter;

    @Data
    public static class OwnerFilter{
        @ApiModelProperty(value = "群主ID列表")
        private List<String> userid_list;
    }
}
