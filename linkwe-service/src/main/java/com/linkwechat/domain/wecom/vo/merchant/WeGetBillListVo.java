package com.linkwechat.domain.wecom.vo.merchant;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * 对外收款记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 11:38
 */
@Data
public class WeGetBillListVo extends WeResultVo {

    /**
     * 对外收款记录
     */
    private List<Bill> billList;

    /**
     * 收款记录
     */
    @Data
    public static class Bill {

        /**
         * 交易单号
         */
        private String transactionId;

        /**
         * 交易状态。1：已完成 3：已完成有退款
         */
        private Integer tradeState;

        /**
         * 交易时间
         */
        private Long payTime;

        /**
         * 商户单号
         */
        private String outTradeNo;

        /**
         * 付款人的userid
         */
        private String externalUserid;

        /**
         * 收款总金额，单位为分
         */
        private Integer totalFee;

        /**
         * 收款人企业内账号userid
         */
        private String payeeUserid;

        /**
         * 收款方式。0：在聊天中收款 1：收款码收款 2：在直播间收款 3：用产品图册收款
         */
        private Integer paymentType;

        /**
         * 收款商户号id
         */
        private String mchId;

        /**
         * 收款备注
         */
        private String remark;

        /**
         * 商品信息详情列表,商品信息结构参考Commodity
         */
        private List<Commodity> commodityList;

        /**
         * 退款总金额
         */
        private Integer totalRefundFee;

        /**
         * 退款单据详情列表，退款单据详情参考refund
         */
        private List<Refund> refundList;

        /**
         * 联系人信息，详情参考Payer，如创建收款项目时设置为不需要联系地址，则该字段为空，第三方不可获取
         */
        private Contact contactInfo;
    }

    /**
     * 商品信息详情(commodity)
     */
    @Data
    public static class Commodity {
        /**
         * 商品描述
         */
        private String description;

        /**
         * 商品数量
         */
        private Integer amount;
    }

    /**
     *
     */
    @Data
    public static class Refund {

        /**
         * 退款单号
         */
        private String outRefundNo;

        /**
         * 退款发起人ID
         */
        private String refundUserid;

        /**
         * 退款备注
         */
        private String refundComment;

        /**
         * 退款发起时间
         */
        private Long refundReqtime;

        /**
         * 退款状态。0:已申请退款；1:退款处理中；2:退款成功；3:退款关闭；4:退款异常；5:审批中；6:审批失败；7:审批取消
         */
        private Integer refundStatus;

        /**
         * 退款金额
         */
        private Integer refundFee;
    }

    /**
     * 联系人信息
     */
    @Data
    public static class Contact {

        /**
         * 联系人姓名
         */
        private String name;

        /**
         * 联系人手机号
         */
        private String phone;

        /**
         * 联系地址
         */
        private String address;
    }


}
