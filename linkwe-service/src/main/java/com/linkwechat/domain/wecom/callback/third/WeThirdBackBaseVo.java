package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 回调验证XML对象
 * @date 2021/11/19 18:39
 **/
@ApiModel
@Data
public class WeThirdBackBaseVo {

    @ApiModelProperty("第三方应用的SuiteId")
    private String SuiteId;

    @ApiModelProperty("事件类型")
    private String InfoType;

    @ApiModelProperty("消息创建时间 （整型）")
    private Long TimeStamp;

    @ApiModelProperty("变更类型")
    private String ChangeType;

    @ApiModelProperty("授权企业的CorpID")
    private String AuthCorpId;

    @ApiModelProperty("构造授权链接指定的state参数")
    private String State;
}
