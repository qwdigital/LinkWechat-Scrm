package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class WeAiMsgVo {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;


    /**
     * 会话ID
     */
    @ApiModelProperty(value = "会话ID")
    private String sessionId;


    /**
     * AI对话ID
     */
    @ApiModelProperty(value = "AI对话ID")
    private String msgId;


    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private Long userId;


    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    private String role;


    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;


    /**
     * 结果ID
     */
    @ApiModelProperty(value = "结果ID")
    private String requestId;


    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "发送时间")
    private Date sendTime;


    /**
     * 收藏 0-未收藏 1-收藏
     */
    @ApiModelProperty(value = "收藏 0-未收藏 1-收藏")
    private Integer collection;
}
