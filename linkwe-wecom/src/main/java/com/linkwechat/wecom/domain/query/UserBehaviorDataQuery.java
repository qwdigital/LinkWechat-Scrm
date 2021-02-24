package com.linkwechat.wecom.domain.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 */
@Data
public class UserBehaviorDataQuery {

    @ApiModelProperty(value = "成员ID列表，最多100个")
    private List<String> userid;

    @ApiModelProperty(value = "部门ID列表，最多100个")
    private List<String> partyid;

    @ApiModelProperty(value = "数据起始时间")
    private Long start_time;

    @ApiModelProperty(value = "数据结束时间")
    private Long end_time;
}
