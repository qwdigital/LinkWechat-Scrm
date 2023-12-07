package com.linkwechat.domain.groupchat.vo;

import com.linkwechat.domain.WeGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author danmo
 * @description 客户群聊列表出参
 * @date 2022/4/2 14:43
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkGroupChatListVo extends WeGroup {

    @ApiModelProperty("管理员名称")
    private String groupLeaderName;


    @ApiModelProperty("群成员数量")
    private int memberNum;

    /**
     * 客户数量
     */
    @ApiModelProperty("客户数量")
    private int customerNum;

    /**
     * 今日进群数
     */
    @ApiModelProperty("今日进群数")
    private int toDayMemberNum;


    /**
     * 今日退群数
     */
    private int toDayExitMemberNum;

    /**
     * 标签名，使用逗号隔开
     */
    @ApiModelProperty("标签名，使用逗号隔开")
    private String tags;


    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private String tagId;


    /**
     * 标签IDS
     */
    private String tagIds;


    /**
     * 群创建时间
     */
    private Date addTime;

    /**
     * 成员id
     */
    private String memberId;


}
