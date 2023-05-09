package com.linkwechat.domain.groupchat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 客群成员
 * @date 2022/4/3 9:57
 **/
@ApiModel
@Data
public class WeGroupMemberVo {
    /**
     * 群成员id
     */
    @ApiModelProperty(value = "群成员id")
    private String userId;


    /**
     * 外部联系人在微信开放平台的唯一身份标识
     */
    @ApiModelProperty(value = "外部联系人在微信开放平台的唯一身份标识")
    private String unionId;


    /**
     * 加群时间
     */
    @ApiModelProperty(value = "加群时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date joinTime;


    /**
     * 加入方式
     */
    @ApiModelProperty(value = "加入方式")
    private Integer joinScene;


    /**
     * 成员类型:1 - 企业成员;2 - 外部联系人
     */
    @ApiModelProperty(value = "成员类型:1 - 企业成员;2 - 外部联系人")
    private Integer type;


    /**
     * 在群里的昵称
     */
    @ApiModelProperty(value = "在群里的昵称")
    private String groupNickName;


    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String name;


    /**
     * 邀请人userId
     */
    @ApiModelProperty(value = "邀请人userId")
    private String invitorUserId;

    /**
     * 邀请人userId
     */
    @ApiModelProperty(value = "邀请人名称")
    private String invitorUserName;

    @ApiModelProperty("群聊ID")
    private String chatId;

    @ApiModelProperty("头像")
    private String avatar;

}
