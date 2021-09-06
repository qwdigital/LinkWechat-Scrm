package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/8/14 9:32
 */
@ApiModel
@Data
public class WeChatContactSensitiveMsgVO {
    @ApiModelProperty("发送人")
    private String fromId;
    @ApiModelProperty("发送内容")
    private String content;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date msgtime;
    @ApiModelProperty("通知发送状态")
    private String status;
    @ApiModelProperty("匹配的关键词")
    private String patternWords;
}
