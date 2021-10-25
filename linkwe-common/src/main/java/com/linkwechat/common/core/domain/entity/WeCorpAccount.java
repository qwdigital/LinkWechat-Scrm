package com.linkwechat.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 企业id相关配置对象 wx_corp_account
 *
 * @author ruoyi
 * @date 2020-08-24
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("企业id配置相关实体")
@TableName("we_corp_account")
public class WeCorpAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    @ApiModelProperty("主键")
    private Long id= SnowFlakeUtil.nextId();


    /*****************************************
     *******************企业配置****************
     ******************************************/


    /** 企业ID */
    @ApiModelProperty("企业corpId")
    @NotBlank(message = "企业ID不能为空")
    private String corpId;


    /** 企业名称 */
    @ApiModelProperty("企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String companyName;



    /*****************************************
     *******************通讯录配置****************
     ******************************************/

    /** 通讯录 Secret */
    @ApiModelProperty("企业为corpSecret")
    @NotBlank(message = "应用的密钥凭证不能为空")
    private String corpSecret;


    /*****************************************
     *******************客户联系配置****************
     ******************************************/

    @ApiModelProperty("客户联系配置")
    @NotBlank(message = "外部联系人密钥凭证不能为空")
    private String contactSecret;



    /*****************************************
     *******************应用配置****************
     ******************************************/

    @ApiModelProperty("应用id")
    private String agentId;

    @ApiModelProperty("应用密钥")
    private String agentSecret;


    @ApiModelProperty("会话存档私钥")
    private String financePrivateKey;


    @ApiModelProperty("会话存档密钥")
    private String chatSecret;



    /*****************************************
     *******************接受事件服务器****************
     ******************************************/


    /**回调URL*/
    private String backOffUrl;

    /**回调token*/
    private String token;

    /**回调EncodingAESKey*/
    private String encodingAesKey;











    /** 帐号状态（0正常 1停用) */
    @ApiModelProperty("帐号状态（0正常 1停用)")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @ApiModelProperty("删除标志（0代表存在 1代表删除)")
    private Integer delFlag;






    @ApiModelProperty("服务商密钥")
    private String providerSecret;



    @ApiModelProperty("企业微信扫码登陆回调地址")
    private String wxQrLoginRedirectUri;

    @ApiModelProperty("客户流失通知开关 0:关闭 1:开启")
    private String customerChurnNoticeSwitch = Constants.NORMAL_CODE;



    @ApiModelProperty("企业管理员账号")
    private String corpAccount;
}
