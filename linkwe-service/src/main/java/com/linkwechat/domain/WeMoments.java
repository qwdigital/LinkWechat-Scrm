package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.config.mybatis.GenericTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 朋友圈(WeMoments)
 *
 * @author danmo
 * @since 2023-04-18 18:41:02
 */
@ApiModel
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@TableName("we_moments")
public class WeMoments extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 异步任务id，24小时有效
     */
    @ApiModelProperty(value = "异步任务id，24小时有效")
    @TableField("job_id")
    private String jobId;


    /**
     * 朋友圈id
     */
    @ApiModelProperty(value = "朋友圈id")
    @TableField("moment_id")
    private String momentId;


    /**
     * 可见类型:0-部分可见 1-公开
     */
    @ApiModelProperty(value = "可见类型:0-部分可见 1-公开")
    @TableField("scope_type")
    private Integer scopeType;


    /**
     * 内容类型
     */
    @ApiModelProperty(value = "内容类型")
    @TableField("content_type")
    private String contentType;


    /**
     * 朋友圈类型:0:企业动态;1:个人动态
     */
    @ApiModelProperty(value = "朋友圈类型:0:企业动态;1:个人动态")
    @TableField("type")
    private Integer type;


    /**
     * 客户标签，多个使用逗号隔开
     */
    @ApiModelProperty(value = "客户标签，多个使用逗号隔开")
    @TableField("customer_tag")
    private String customerTag;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField("creator")
    private String creator;


    /**
     * 添加人,多个使用逗号隔开
     */
    @ApiModelProperty(value = "添加人,多个使用逗号隔开")
    @TableField("add_user")
    private String addUser;

    /**
     * 发送员工名称,使用逗号隔开
     */
    @ApiModelProperty(value = "发送员工名称,使用逗号隔开")
    @TableField(exist = false)
    private String addUserName;

    /**
     * 未发送员工，使用逗号隔开
     */
    @ApiModelProperty(value = "未发送员工，使用逗号隔开")
    @TableField("no_add_user")
    private String noAddUser;

    /**
     * 未发送员工名称，使用逗号隔开
     */
    @ApiModelProperty(value = "未发送员工名称，使用逗号隔开")
    @TableField(exist = false)
    private String noAddUserName;

    /**
     * 朋友圈部分内容
     */
    @ApiModelProperty(value = "朋友圈部分内容")
    @TableField("content")
    private String content;


    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    @TableField(value = "other_content", typeHandler = GenericTypeHandler.class)
    private List<OtherContent> otherContent;


    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    @TableField("push_time")
    private Date pushTime;


    /**
     * 是否是在lw平台发布的:1:是;0:否;
     */
    @ApiModelProperty(value = "是否是在lw平台发布的:1:是;0:否;")
    @TableField("is_lw_push")
    private Integer isLwPush;


    /**
     * 真实素材类型
     */
    @ApiModelProperty(value = "真实素材类型")
    @TableField("real_type")
    private Integer realType;


    /**
     * 素材中心Id
     */
    @ApiModelProperty(value = "素材中心Id")
    @TableField("material_id")
    private Long materialId;


    /**
     * 任务状态：1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成
     */
    @ApiModelProperty(value = "任务状态：1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成")
    @TableField("status")
    private Integer status;

    private Integer delFlag;
    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数")
    @TableField(exist = false)
    private Integer commentNum;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    @TableField(exist = false)
    private Integer pointNum;

    @ApiModelProperty(value = "评论员工名")
    @TableField(exist = false)
    private String momentUserName;

    @ApiModelProperty(value = "点赞员工名")
    @TableField(exist = false)
    private String pointUserName;

    @ApiModelProperty(value = "评论客户名")
    @TableField(exist = false)
    private String momentCustomerName;

    @ApiModelProperty(value = "点赞客户名")
    @TableField(exist = false)
    private String pointCustomerName;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherContent {
        //附件类型：1:image 2:video 3:link  4:text
        private String annexType;

        //资源url
        private String annexUrl;

        //如视频封面,图文标题,文字内容
        private String other;

        //文章标题
        private String title;
    }
}
