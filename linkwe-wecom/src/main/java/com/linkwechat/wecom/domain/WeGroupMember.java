package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:56
 */
@ApiModel
@Data
@TableName("we_group_member")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupMember extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "群id")
    private String chatId;

    @ApiModelProperty(value = "入群时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinTime;

    /**入群方式: 1 - 由成员邀请入群（直接邀请入群) ;   2 - 由成员邀请入群（通过邀请链接入群）;  3 - 通过扫描群二维码入群 */
    @ApiModelProperty(value = "入群方式: 1 - 由成员邀请入群（直接邀请入群) ;   2 - 由成员邀请入群（通过邀请链接入群）;  3 - 通过扫描群二维码入群")
    private Integer joinScene;

    @ApiModelProperty(value = "成员类型:1 - 企业成员;2 - 外部联系人")
    private Integer type;

    @ApiModelProperty("群成员id")
    private String userId;

    @ApiModelProperty("外部联系人在微信开放平台的唯一身份标识")
    private String unionId;

    @ApiModelProperty("在群里的昵称")
    private String groupNickName;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("邀请人userId")
    private String invitorUserId;


    @TableLogic
    private Integer delFlag=new Integer(0);
}
