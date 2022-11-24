package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * unionid与external_userid关联表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/26 17:13
 */
@ApiModel
@Data
public class WeUnionidExternalUseridRelation {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "微信客户的openid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "微信客户的unionid")
    @TableField("unionid")
    private String unionid;

    @ApiModelProperty(value = "该授权企业的外部联系人ID")
    @TableField("external_userid")
    private String externalUserid;

    @ApiModelProperty(value = "临时外部联系人ID")
    @TableField("pending_id")
    private String pendingId;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


    @ApiModelProperty(value = "删除标识 0 未删除 1 已删除")
    @TableField("del_flag")
    private Integer delFlag;

}
