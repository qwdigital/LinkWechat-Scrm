package com.linkwechat.domain.taggroup;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("we_pres_tag_group")
public class WePresTagGroupTask extends BaseEntity {


    /**
     * 主键
     */
    @TableId
    private Long id;


    /**
     * 任务名
     */
    private String taskName;


    /**
     * 加群引导语
     */
    private String welcomeMsg;


    /**
     * 实际群id，多个实用逗号隔开
     */
    private String chatIdList;


    /**
     * 当群满了后，是否自动新建群。0-否；1-是。 默认为0
     */
    private Integer autoCreateRoom;


    /**
     * 自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符
     */
    private String roomBaseName;


    /**
     * 自动建群的群起始序号，当auto_create_room为1时有效
     */
    private Integer roomBaseId;


    /**
     * 群活码企微信的configId
     */
    private String groupCodeConfigId;

    /**
     * 群活码渠道标识
     */
    private String groupCodeState;


    /**
     * 群活码图片地址
     */
    private String groupCodeUrl;




    /**
     * 链接标题
     */
    private String linkTitle;

    /**
     * 链接描述
     */
    private String linkDesc;

    /**
     * 链接封面
     */
    private String linkCoverUrl;



    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private Integer delFlag;



    /**
     * 发送范围 0: 全部客户 1：部分客户
     */
    @ApiModelProperty("发送范围 0: 全部客户 1：部分客户")
    private Integer sendScope;

    /**
     * 是否全部发送 true全部发送,false非全部发送
     */
    private Boolean isAll=true;


    /**
     * 客户查询条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeCustomersQuery weCustomersQuery;


    /**
     * 指定接收消息的成员及对应客户列表
     */
    @TableField(exist = false)
    private List<WeAddGroupMessageQuery.SenderInfo> senderList;


    /**
     * 群名，多个使用逗号隔开
     */
    @TableField(exist = false)
    private String groupNames;



//    @Data
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class SenderInfo {
//
//        @ApiModelProperty("成员id")
//        private String userId;
//
//        @ApiModelProperty("客户列表")
//        private List<String> customerList;
//    }







//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 主键ID
//     */
//    @ApiModelProperty("id")
//    @TableId(type = IdType.AUTO)
//    private Long taskId;
//
//    /**
//     * 任务名称
//     */
//    @ApiModelProperty("任务名")
//    @NotNull(message = "任务名称不能为空")
//    private String taskName;
//
//    /**
//     * 加群引导语
//     */
//    @ApiModelProperty("加群引导语")
//    @Size(max = 64, message = "引导与不能超过64字符")
//    @NotBlank(message = "引导语不能为空")
//    private String welcomeMsg;
//
//    /**
//     * 发送方式 0: 企业群发 1：个人群发
//     */
//    @ApiModelProperty("加群引导语")
//    @NotNull(message = "发送方式不能为空")
//    private Integer sendType;
//
//    /**
//     * 群活码id
//     */
//    @ApiModelProperty("群活码id")
//    @NotNull(message = "活码不能为空")
//    private Long groupCodeId;
//
//    /**
//     * 发送范围 0: 全部客户 1：部分客户
//     */
//    @ApiModelProperty("发送范围 0: 全部客户 1：部分客户")
//    private Integer sendScope;
//
//    /**
//     * 发送性别 0: 全部 1： 男 2： 女 3：未知
//     */
//    @ApiModelProperty("发送性别 0: 全部 1： 男 2： 女 3：未知")
//    private Integer sendGender;
//
//    /**
//     * 目标客户被添加起始时间
//     */
//    @ApiModelProperty("目标客户被添加起始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
//    private Date cusBeginTime;
//
//    /**
//     * 目标客户被添加结束时间
//     */
//    @ApiModelProperty("目标客户被添加结束时间")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
//    private Date cusEndTime;
//
//    /**
//     * 群发消息id
//     */
//    @ApiModelProperty(value = "消息模板id", hidden = true)
//    private Long messageTemplateId;
//
//    /**
//     * 逻辑删除字段
//     */
//    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
//    private Integer delFlag = 0;
//
//    /**
//     * 客户标签
//     */
//    @ApiModelProperty("客户标签id列表")
//    @TableField(exist = false)
//    private List<String> tagList;
//
//    /**
//     * 选择员工
//     */
//    @ApiModelProperty("员工id列表")
//    @TableField(exist = false)
//    private List<String> scopeList;
//
//    /**
//     * 群活码链接
//     */
//    @ApiModelProperty("群活码链接")
//    @TableField(exist = false)
//    private String codeUrl;
//
//    /**
//     * 当前群总人数
//     */
//    @ApiModelProperty("当前群总人数")
//    @TableField(exist = false)
//    private Integer totalNumber;
//
//
//    /**
//     * 客户id，如果全部客户的时候不用传
//     */
//    @TableField(exist = false)
//    private List<String> externalUserIds;
}
