package com.linkwechat.wecom.domain.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 标签成员变更通知
 * @date 2021/11/20 13:14
 **/
@ApiModel
@Data
public class WeBackUserTagVo extends WeBackBaseVo{

    @ApiModelProperty("标签Id")
    private String TagId;

    @ApiModelProperty("标签中新增的成员userid列表，用逗号分隔")
    private String AddUserItems;

    @ApiModelProperty("标签中删除的成员userid列表，用逗号分隔")
    private String DelUserItems;

    @ApiModelProperty("标签中新增的部门id列表，用逗号分隔")
    private String AddPartyItems;

    @ApiModelProperty("标签中删除的部门id列表，用逗号分隔")
    private String DelPartyItems;
}
