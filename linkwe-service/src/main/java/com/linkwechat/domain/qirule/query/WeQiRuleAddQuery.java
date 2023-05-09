package com.linkwechat.domain.qirule.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 会话质检新增入参
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/
@ApiModel
@Data
public class WeQiRuleAddQuery {

    @ApiModelProperty(value = "活码Id", hidden = true)
    private Long qiId;

    @ApiModelProperty("名称")
    @NotEmpty(message = "规则名称不能为空")
    private String name;

    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间")
    @NotNull(message = "超时时间不能为空")
    private Integer timeOut;


    /**
     * 会话类型 1-全部 2-客户会话 3-客群会话
     */
    @ApiModelProperty(value = "会话类型 1-全部 2-客户会话 3-客群会话")
    private Integer chatType;

    /**
     * 督导人员（逗号相隔）
     */
    @ApiModelProperty(value = "督导人员（逗号相隔）")
    private String manageUser;


    @ApiModelProperty("员工列表")
    private List<WeQiUserInfoQuery> qiUserInfos;
}
