package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 企业id相关配置(WeCorpAccount)
 *
 * @author danmo
 * @since 2022-03-08 19:01:14
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_corp_account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCorpAccount extends BaseEntity {

    private static final long serialVersionUID = 1L; //1

    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /***********************************************
     *******************企业配置start****************
     **********************************************/

    /**
     * 企业ID
     */
    @NotBlank(message = "企业ID不能为空")
    @TableField("corp_id")
    private String corpId;


    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    @TableField("company_name")
    private String companyName;

    /***********************************************
     *******************企业配置end****************
     **********************************************/


    /***********************************************
     *******************通讯录配置start**************
     **********************************************/

    /**
     * 通讯录Secret
     */
    @NotBlank(message = "通讯录Secret不可为空")
    @TableField("corp_secret")
    private String corpSecret;

    /**
     * 直播Secret
     */
    @TableField("live_secret")
    private String liveSecret;

    /***********************************************
     *******************通讯录配置end****************
     **********************************************/


    /***********************************************
     *******************客户联系配置start*************
     **********************************************/


    @NotBlank(message = "客户联系Secret不可为空")
    @TableField("contact_secret")
    private String contactSecret;


    /***********************************************
     *******************客户联系配置end***************
     **********************************************/


    /***********************************************
     *******************应用配置start****************
     **********************************************/
    /**
     * 消息应用ID
     */
    @NotBlank(message = "消息应用ID不可为空")
    @TableField("agent_id")
    private String agentId;

    /**
     * 消息应用Secret
     */
    @NotBlank(message = "消息应用Secret不可为空")
    @TableField("agent_secret")
    private String agentSecret;


    /**
     * 会话私钥Secret
     */
    @TableField("chat_secret")
    private String chatSecret;


    /**
     * 会话存档密钥
     */
    @TableField("finance_private_key")
    private String financePrivateKey;

    /***********************************************
     *******************应用配置end****************
     **********************************************/


    /***********************************************
     *******************接收事件服务器start***********
     **********************************************/

    /**回调url*/
    //private String backOffUrl;

    /**
     * 回调token
     */
    @TableField("token")
    private String token;

    /**
     * 回调EncodingAESKey
     */
    @TableField("encoding_aes_key")
    private String encodingAesKey;


    /***********************************************
     *******************接收事件服务器end*************
     **********************************************/


    /***********************************************
     *******************微信客服start****************
     **********************************************/

    /**
     * 客服密钥
     */
    @TableField("kf_secret")
    private String kfSecret;


    /***********************************************
     *******************微信客服end******************
     **********************************************/

    /***********************************************
     *******************微信支付start****************
     **********************************************/

    /**
     * 商户名称
     */
    @TableField("mer_chant_name")
    private String merChantName;

    /**
     * 商户号
     */
    @TableField("mer_chant_number")
    private String merChantNumber;

    /**
     * 商户密钥
     */
    @TableField("mer_chant_secret")
    private String merChantSecret;


    /**
     * API证书文件p12
     */
    @TableField("cert_p12_url")
    private String certP12Url;


    /***********************************************
     *******************微信支付start****************
     **********************************************/

    /**
     * 微信公众号ID
     */
    @TableField("wx_app_id")
    private String wxAppId;

    /**
     * 微信公众号密钥
     */
    @TableField("wx_secret")
    private String wxSecret;

    /**
     * 企业logo
     */
    @TableField("logo_url")
    private String logoUrl;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    private Integer delFlag;


    /**
     * 客户流失通知开关 0:关闭 1:开启
     */
    @TableField("customer_churn_notice_switch")
    private String customerChurnNoticeSwitch;


    /***********************************************
     *******************对外收款start****************
     **********************************************/
    /**
     * 对外收款
     */
    @TableField("bill_secret")
    private String billSecret;

    /***********************************************
     *******************对外收款end****************
     **********************************************/

    @ApiModelProperty("微信小程序ID")
    @TableField("mini_app_id")
    private String miniAppId;

    @ApiModelProperty("微信小程序密钥")
    @TableField("mini_secret")
    private String miniSecret;

    @ApiModelProperty("微信小程序原始ID")
    @TableField("wx_applet_original_id")
    private String wxAppletOriginalId;

    /**
     * 获客助手剩余可用量
     */
    private long customerLinkMargin;


    /**
     * 获客助手总量
     */
    private long customerLinkTotal;


    /***********************************************
     *******************商城start****************
     **********************************************/

    /**
     * 商城小程序app_id
     */
    @ApiModelProperty(value = "商城小程序app_id")
    @TableField("shop_app_id")
    private String shopAppId;

    /**
     * 商城小程序秘钥
     */
    @ApiModelProperty(value = "商城小程序秘钥")
    @TableField("shop_secret")
    private String shopSecret;

    /**
     * 商城小程序消息推送token（令牌）
     */
    @ApiModelProperty(value = "商城小程序消息推送token（令牌）")
    @TableField("shop_ma_token")
    private String shopMaToken;

    /**
     * 商城小程序消息推送秘钥
     */
    @ApiModelProperty(value = "商城小程序消息推送秘钥")
    @TableField("shop_ma_encodingaeskey")
    private String shopMaEncodingaeskey;

    /**
     * 商城小程序消息推送-服务器地址
     */
    @ApiModelProperty(value = "商城小程序消息推送-服务器地址")
    @TableField("shop_ma_api")
    private String shopMaApi;

    /***********************************************
     *******************商城end****************
     **********************************************/
}
