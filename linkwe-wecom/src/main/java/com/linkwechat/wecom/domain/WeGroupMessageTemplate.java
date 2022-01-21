package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 群发消息模板对象 we_group_message_template
 *
 * @author ruoyi
 * @date 2021-10-27
 */
@ApiModel
@Data
@TableName("we_group_message_template")
public class WeGroupMessageTemplate extends BaseEntity {
    /**
     * 主键id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群发任务的类，1：表示发送给客户，2：表示发送给客户群
     */
    @ApiModelProperty("群发任务的类，1：表示发送给客户，2：表示发送给客户群")
    @Excel(name = "群发任务的类，1：表示发送给客户，2：表示发送给客户群")
    private Integer chatType;

    /**
     * 群发内容
     */
    @ApiModelProperty("群发内容")
    @Excel(name = "群发内容")
    private String content;

    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 条件筛选
     */
    @ApiModelProperty("条件筛选")
    @Excel(name = "条件筛选")
    private String filterCondition;

    /**
     * 是否定时任务 0 立即发送 1 定时发送
     */
    @ApiModelProperty("是否定时任务 0 立即发送 1 定时发送")
    @Excel(name = "是否定时任务 0 立即发送 1 定时发送")
    private Integer isTask;

    /**
     * 状态 -1：失败  0：未执行 1：完成 2：取消
     */
    @ApiModelProperty("状态 -1：失败  0：未执行 1：完成 2：取消")
    @Excel(name = "是否执行 -1：失败  0：未执行 1：完成 2：取消")
    private Integer status;

    /**
     * 来源 0 群发 1 其他
     */
    @ApiModelProperty("来源 0 群发 1-任务宝 2-标签建群")
    private Integer source;

    /**
     * 业务id
     */
    @ApiModelProperty("业务id")
    private Long businessId;

    /**
     * 刷新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("刷新时间")
    private Date refreshTime;
}
