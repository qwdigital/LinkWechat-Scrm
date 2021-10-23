package com.linkwechat.wecom.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 群发成员发送任务列表入参
 * @date 2021/10/19 16:49
 **/
@ApiModel
@Data
public class WeGetGroupMsgListQuery {

    @ApiModelProperty("群发消息的id")
    private String msgid;

    @ApiModelProperty("发送成员userid")
    private String userid;

    @ApiModelProperty("返回的最大记录数，整型，最大值1000，默认值500")
    private Integer limit;

    @ApiModelProperty("用于分页查询的游标")
    private String cursor;
}
