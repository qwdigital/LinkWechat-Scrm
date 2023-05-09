package com.linkwechat.domain.qirule.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会话质检详情出参
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/

@ApiModel
@Data
public class WeQiRuleDetailVo {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;


    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String name;


    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间")
    private Integer timeOut;


    /**
     * 会话类型 1-全部 2-客户会话 3-客群会话
     */
    @ApiModelProperty(value = "会话类型 1-全部 2-客户会话 3-客群会话")
    private Integer chatType;

    @ApiModelProperty(hidden = true)
    private String manageUser;
    /**
     * 督导人员
     */
    @ApiModelProperty(value = "督导人员")
    private List<WeQiRuleUserVo> manageUserInfo;

    @ApiModelProperty("质检规则范围")
    private List<WeQiRuleScopeVo> qiRuleScope;
}
