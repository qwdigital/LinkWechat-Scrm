package com.linkwechat.domain.wecom.query.living;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author danmo
 * @description 直播接口入参
 * @date 2022/10/10 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeLivingQuery extends WeBaseQuery {

    /**
     * 直播id，仅允许取消预约状态下的直播id
     */
    @ApiModelProperty("直播id，仅允许取消预约状态下的直播id")
    private String livingid;

    /**
     * 上一次调用时返回的next_key
     */
    @ApiModelProperty("上一次调用时返回的next_key，初次调用可以填0")
    private String next_key = "0";
}
