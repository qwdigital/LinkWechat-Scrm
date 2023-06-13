package com.linkwechat.domain.qirule.vo;

import com.linkwechat.domain.WeQiRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会话质检列表出参
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/

@ApiModel
@Data
public class WeQiRuleListVo extends WeQiRule {
    /**
     * 督导人员
     */
    @ApiModelProperty(value = "督导人员")
    private List<WeQiRuleUserVo> manageUserInfo;

    @ApiModelProperty("质检规则范围")
    private List<WeQiRuleUserVo> qiRuleScope;
}
