package com.linkwechat.domain.envelopes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 红包详情
 */
@ApiModel
@Data
@Builder
public class H5RedEnvelopesDetailDto {
    //机构名称
    @ApiModelProperty("机构名称")
    private String corpName;
    //企业logo
    @ApiModelProperty("企业logo")
    private String logo;
    //红包类
    @ApiModelProperty("红包类型")
    private Integer redEnvelopesType;
    //当前收取金额
    @ApiModelProperty("当前收取金额")
    private int currentAcceptMoney;
    //红包总额
    @ApiModelProperty("红包总额")
    private int totalMoney;
    //已领取金额
    @ApiModelProperty("已领取金额")
    private int accpectMoney;

    @ApiModelProperty("红包个数")
    private Integer redEnvelopeNum;

    //接受人
    @ApiModelProperty("接受人信息")
    private List<AccpestCustomer> accpestCustomerList;

    //已领取个数
    @ApiModelProperty("已领取个数")
    private int acceptNum;

    //未领取个数
    @ApiModelProperty("未领取个数")
    private int noAcceptNum;

    @ApiModel
    @Data
    public static class AccpestCustomer {
        //头像
        @ApiModelProperty("头像")
        private String avatar;
        //领取人姓名
        @ApiModelProperty("领取人姓名")
        private String customerName;
        //领取时间
        @ApiModelProperty("领取时间")
         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date accpectTime;
        //领取金额
        @ApiModelProperty("领取金额")
        private int accpectMoney;
    }

}

