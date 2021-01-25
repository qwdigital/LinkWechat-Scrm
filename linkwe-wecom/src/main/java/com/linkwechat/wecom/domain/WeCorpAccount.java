package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;

/**
 * 企业id相关配置对象 wx_corp_account
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("企业id配置相关实体")
public class WeCorpAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /** 企业名称 */
    @ApiModelProperty("企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    /** 企业ID */
    @ApiModelProperty("企业ID")
    @NotBlank(message = "企业ID不能为空")
    private String corpId;

    /** 应用的密钥凭证 */
    @ApiModelProperty("应用的密钥凭证")
    @NotBlank(message = "应用的密钥凭证不能为空")
    private String corpSecret;

    /** 帐号状态（0正常 1停用) */
    @ApiModelProperty("帐号状态（0正常 1停用)")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty("删除标志（0代表存在 2代表删除)")
    private String delFlag=new String("0");


    @ApiModelProperty("外部联系人密钥")
    @NotBlank(message = "外部联系人密钥凭证不能为空")
    private String contactSecret;

    @ApiModelProperty("应用id")
    private String agentId;

    @ApiModelProperty("应用密钥")
    private String agentSecret;

    @ApiModelProperty("服务商密钥")
    private String providerSecret;

    @ApiModelProperty("会话存档密钥")
    private String chatSecret;


    @ApiModelProperty("企业微信扫码登陆回调地址")
    private String wxQrLoginRedirectUri;

    @ApiModelProperty("客户流失通知开关 0:关闭 1:开启")
    private String customerChurnNoticeSwitch = new String("0");
}
