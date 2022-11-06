package com.linkwechat.domain.wecom.vo.msg;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 应用消息返回体
 * @date 2021/12/2 23:50
 **/
@ApiModel
@Data
public class WeAppMsgVo extends WeResultVo {

    @ApiModelProperty("不合法的userid，不区分大小写，统一转为小写")
    private String invalidUser;

    @ApiModelProperty("不合法的partyid")
    private String invalidParty;

    @ApiModelProperty("不合法的标签id")
    private String invalidTag;

    @ApiModelProperty("没有基础接口许可(包含已过期)的userid")
    private String unlicenseduser;

    @ApiModelProperty("消息id，用于撤回应用消息")
    private String msgId;

    @ApiModelProperty("仅消息类型为“按钮交互型”，“投票选择型”和“多项选择型”的模板卡片消息返回，应用可使用response_code调用更新模版卡片消息接口，24小时内有效，且只能使用一次")
    private String responseCode;

}
