package com.linkwechat.domain.envelopes;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("we_red_envelopes_limit")
@ApiModel("红包限制")
@AllArgsConstructor
@NoArgsConstructor
public class WeRedEnvelopesLimit extends BaseEntity {
    @TableId
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("单日付款总额")
    private int singleDayPay;
    @ApiModelProperty("单日客户收红包次数")
    private int singleCustomerReceiveNum;
    @ApiModelProperty("单日每客户收红包总额")
    private int singleCustomerReceiveMoney;
}
