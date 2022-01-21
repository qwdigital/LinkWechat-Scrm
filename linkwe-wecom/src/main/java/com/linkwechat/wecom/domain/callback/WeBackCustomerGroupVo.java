package com.linkwechat.wecom.domain.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客户群变更通知
 * @date 2021/11/20 13:14
 **/
@ApiModel
@Data
public class WeBackCustomerGroupVo extends WeBackBaseVo {

    @ApiModelProperty("群ID")
    private String ChatId;

    @ApiModelProperty("变更详情。add_member:成员入群 del_member:成员退群 change_owner:群主变更 change_name:群名变更 change_notice:群公告变更")
    private String UpdateDetail;

    @ApiModelProperty("当是成员入群时有值。表示成员的入群方式  0 - 由成员邀请入群（包括直接邀请入群和通过邀请链接入群） 3 - 通过扫描群二维码入群")
    private Integer JoinScene;

    @ApiModelProperty("当是成员退群时有值。表示成员的退群方式 0 - 自己退群 1 - 群主/群管理员移出")
    private Integer QuitScene;

    @ApiModelProperty("当是成员入群或退群时有值。表示成员变更数量")
    private Integer MemChangeCnt;
}
