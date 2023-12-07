package com.linkwechat.domain.moments.entity;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/06 17:26
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
@TableName(value="we_moments_task",autoResultMap = true)
public class WeMomentsTask extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 朋友圈类型:0:企业动态;1:个人动态
     */
    private Integer type;

    /**
     * 发送方式: 0企微群发,2成员群发,
     */
    private Integer sendType;

    /**
     * 是否是在lw平台发布的:1:是;0:否;
     */
    private Integer isLwPush;

    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    private Integer scopeType;

    /**
     * 朋友圈可见客户数
     */
    private Integer customerNum;

    /**
     * 部门id集合
     */
    private String deptIds;

    /**
     * 岗位id集合
     */
    private String postIds;

    /**
     * 员工id集合
     */
    private String userIds;

    /**
     * 客户标签
     */
    private String customerTag;

    /**
     * 朋友圈文本内容
     */
    private String content;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 结束时间
     */
    private LocalDateTime executeEndTime;

    /**
     * 点赞标签
     */
    private String likeTagIds;

    /**
     * 评论标签
     */
    private String commentTagIds;

    /**
     * 任务状态：1未开始，2进行中，3已结束
     */
    private Integer status;

    /**
     * 删除标识 0:正常 1:删除
     */
    private Integer delFlag;

    /**
     * 企微创建时间
     */
    private Date establishTime;


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


}
