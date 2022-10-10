package com.linkwechat.domain.envelopes;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@Builder
@TableName("we_red_envelopes_record")
@ApiModel("客户红包发放记录")
@AllArgsConstructor
@NoArgsConstructor
public class WeRedEnvelopesRecord extends BaseEntity {
    @TableId
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("员工id")
    @TableField(value ="we_user_id" )
    private String userId;

    @ApiModelProperty("群id")
    private String chatId;
    @ApiModelProperty("红包个数")
    private Integer redEnvelopeNum;

    @ApiModelProperty("红包名")
    private String redEnvelopeName;

    @ApiModelProperty("红包领取人:1:好友客户;2:群成员")
    private Integer receiveType;
    @ApiModelProperty("红包金额")
    private int redEnvelopeMoney;
    @ApiModelProperty("1: 普通红包2:拼手气红包")
    private Integer redEnvelopeType;
    @ApiModelProperty("发送状态:1:待领取;2:已领取;3:发放失败;4:退款中;5:已退款")
    private Integer sendState;
    @ApiModelProperty("交易订单号")
    private String orderNo;

    @ApiModelProperty("0:正常;1:删除;")
    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    private String groupName;

    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty("红包领取出错的原因")
    private String errCodeDes;

    //红包来源1:根红包(如果发送给客户或者群择为1);2:群成员接受
    private Integer source;

    //群客户领取订单号
    private String receiveOrderNo;
    //红包发布自:1:个人发送；2:模版发送
    private Integer fromType;

    //领取人公众号id
    private String openId;

    private String receiveName;

    private String avatar;

    private String errCode;

    private String reason;



}

