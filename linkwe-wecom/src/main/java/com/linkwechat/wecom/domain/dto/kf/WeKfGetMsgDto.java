package com.linkwechat.wecom.domain.dto.kf;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @description 客服消息
 * @date 2021/10/9 15:32
 **/
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeKfGetMsgDto extends WeResultDto {

    @ApiModelProperty("上一次调用时返回的next_cursor")
    private String cursor;

    @ApiModelProperty("回调事件返回的token字段，10分钟内有效")
    private String token;

    @ApiModelProperty("期望请求的数据量，默认值和最大值都为1000")
    private String limit;

    @ApiModelProperty("下次调用带上该值则从该key值往后拉，用于增量拉取")
    private String nextCursor;

    @ApiModelProperty("是否还有更多数据。0-否；1-是")
    private String hasMore;

    @ApiModelProperty("消息列表")
    private List<JSONObject> msgList;
}
