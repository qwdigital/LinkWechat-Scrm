package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客户分析
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeSessionArchiveAnalysisVo {

    @ApiModelProperty("开启会话存档员工")
    private Integer openUserCnt;

    @ApiModelProperty("未开启会话存档员工")
    private Integer notOpenUserCnt;

    @ApiModelProperty("已同意会话存档客户")
    private Integer openCustomerCnt;

    @ApiModelProperty("未同意会话存档客户")
    private Integer notOpenCustomerCnt;
}
