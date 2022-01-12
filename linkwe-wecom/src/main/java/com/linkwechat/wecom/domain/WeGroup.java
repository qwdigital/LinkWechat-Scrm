package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:55
 */
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_group")
public class WeGroup extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "群聊id")
    private String chatId;

    @ApiModelProperty(value = "群名")
    private String groupName;

    @TableField(exist = false)
    private Long memberNum;

    @ApiModelProperty(value = "群公告")
    private String notice;

    @ApiModelProperty(value = "群主userId")
    private String owner;

    @ApiModelProperty(value = "0 - 正常;1 - 跟进人离职;2 - 离职继承中;3 - 离职继承完成")
    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @TableField(exist = false)
    private String groupLeaderName;


    @TableField(exist = false)
    private String groupLeader;

    @TableField(exist = false)
    @JsonFormat( pattern = "yyyy-MM-dd")
    private String beginTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endTime;

    /**员工id*/
    @TableField(exist = false)
    @ApiModelProperty(value = "员工id")
    private String userIds;

    /**
     * 群头像
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "群头像")
    private String avatar;


    /**
     * 标签名，使用逗号隔开
     */
    @TableField(exist = false)
    private String tags;


    /**
     * 标签id
     */
    @TableField(exist = false)
    private String tagId;


    /**
     * 标签IDS
     */
    @TableField(exist = false)
    private String tagIds;


    /**
     * 加入时间
     */
    private Date addTime;


    /**
     * 群管理员id
     */
    private String adminUserId;


    /**
     * 客户数量
     */
    @TableField(exist = false)
    private Integer customerNum;

    /**
     * 今日进群数
     */
    @TableField(exist = false)
    private Integer toDayMemberNum;




    @TableLogic
    private Integer delFlag=new Integer(0);
}
