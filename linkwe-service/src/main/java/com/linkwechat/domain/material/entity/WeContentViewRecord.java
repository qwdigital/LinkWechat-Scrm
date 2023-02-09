package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@TableName("we_content_view_record")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查看人记录")
public class WeContentViewRecord extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /**
     * 会话Id
     */
    private Long talkId;

    /**
     * 素材Id
     */
    private Long contentId;

    /**
     * 查看人
     */
    private String viewBy;

    /**
     * 查看人openId
     */
    private String viewOpenid;

    /**
     * 查看人unionid
     */
    private String viewUnionid;

    /**
     * 查看时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date viewTime;

    /**
     * 查看耗时
     */
    private Long viewWatchTime;

    /**
     * 是否企业客户 0否1是  （当前的用户是不是我企业的客户）
     */
    private Integer isCustomer;

    /**
     * 素材类型(1素材，2企业话术，3客服话术)
     */
    private Integer resourceType;

    /**
     * 外部联系人Id
     */
    private String externalUserId;

    /**
     * 外部联系人姓名
     */
    private String externalUserName;

    /**
     * 外部联系人头像
     */
    private String externalAvatar;

    /**
     * 是否授权 0否1是
     */
    private Integer isAuth;


    @TableField(exist = false)
    private Integer viewTotalNum;

    @TableField(exist = false)
    private String unionid;

}
