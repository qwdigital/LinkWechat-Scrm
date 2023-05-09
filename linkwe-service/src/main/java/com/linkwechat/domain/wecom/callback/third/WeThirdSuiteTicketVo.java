package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 推送suite_ticket
 * @date 2022/3/7 22:00
 **/
@ApiModel
@Data
public class WeThirdSuiteTicketVo extends WeThirdBackBaseVo{

    @ApiModelProperty("Ticket内容，最长为512字节")
    private String SuiteTicket;
}
