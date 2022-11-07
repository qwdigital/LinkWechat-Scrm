package com.linkwechat.domain.groupchat.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author danmo
 * @description 客群入参
 * @date 2022/4/2 13:47
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupChatQuery{

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "群聊Id")
    private String chatId;

    @ApiModelProperty(value = "群名")
    private String groupName;

    @ApiModelProperty(value = "群主userId")
    private String userIds;

    @ApiModelProperty(value = "标签ID")
    private String tagIds;

    /** 开始时间 */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /** 结束时间 */
    @ApiModelProperty("结束时间")
    private String endTime;


    //个人数据:false 全部数据(相对于角色定义的数据权限):true
    private boolean dataScope=false;

    //群主
    private String groupLeaderName;

    //标签名,多个使用逗号隔开
    private String tags;

    //群人数上限
    private Integer groupMemberUp;
    //群人数下限
    private Integer groupMemberDown;
}
