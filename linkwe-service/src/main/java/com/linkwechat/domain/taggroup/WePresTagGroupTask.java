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
@TableName(value="we_pres_tag_group",autoResultMap = true)
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
     * 实际链接
     */
    private String tagRedirectUrl;

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


    /**
     * 触发客户数
     */
    @TableField(exist = false)
    private int touchWeCustomerNumber;


    /**
     * 进群客户数
     */
    @TableField(exist = false)
    private int joinGroupCustomerNumber;






}
