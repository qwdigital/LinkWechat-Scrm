package com.linkwechat.domain.groupchat.vo;

import com.linkwechat.domain.WeGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @description 客户群聊列表出参
 * @date 2022/4/2 14:43
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkGroupChatVo extends WeGroup {

    @ApiModelProperty("群主名称")
    private String ownerName;

    @ApiModelProperty("群成员列表")
    private List<WeGroupMemberVo> memberList;
}
