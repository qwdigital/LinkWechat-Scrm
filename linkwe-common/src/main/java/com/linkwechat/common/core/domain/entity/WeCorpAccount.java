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
@TableName("we_corp_account")
public class WeCorpAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id;

    /***********************************************
     *******************企业配置start****************
     **********************************************/

    /** 企业ID */
    @NotBlank(message = "企业ID不能为空")
    private String corpId;


    /** 企业名称 */
    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    /***********************************************
     *******************企业配置end****************
     **********************************************/



    /***********************************************
     *******************通讯录配置start**************
     **********************************************/

    /** 通讯录Secret */
    @NotBlank(message = "通讯录Secret不可为空")
    private String corpSecret;

    /***********************************************
     *******************通讯录配置end****************
     **********************************************/



    /***********************************************
     *******************客户联系配置start*************
     **********************************************/


     @NotBlank(message = "客户联系Secret不可为空")
     private String contactSecret;


    /***********************************************
     *******************客户联系配置end***************
     **********************************************/



    /***********************************************
     *******************应用配置start****************
     **********************************************/
         /**消息应用ID*/
        @NotBlank(message = "消息应用ID不可为空")
        private String agentId;

        /**消息应用Secret*/
        @NotBlank(message = "消息应用Secret不可为空")
        private String agentSecret;


        /**会话私钥Secret*/
        private String chatSecret;


        /**会话存档密钥*/
        private String financePrivateKey;

    /***********************************************
     *******************应用配置end****************
     **********************************************/


    /***********************************************
     *******************接收事件服务器start***********
     **********************************************/

     /**回调url*/
     //private String backOffUrl;

     /**回调token*/
     private String token;

     /**回调EncodingAESKey*/
     private String encodingAesKey;


    /***********************************************
     *******************接收事件服务器end*************
     **********************************************/


    /***********************************************
     *******************H5跳转start******************
     **********************************************/

     /**JS SDK 身份校验url*/
     private String authorizeUrl;


     /**群sop和老客标签建群，跳转链接*/
     private String sopTagRedirectUrl;


     /**客户公海，跳转链接*/
     private  String seasRedirectUrl;



    /***********************************************
     *******************H5跳转end*******************
     **********************************************/



        /**客服密钥*/
        private String kfSecret;

        /** 删除标志（0代表存在 1代表删除） */
        private Integer delFlag;


        /**客户流失通知开关 0:关闭 1:开启*/
        private String customerChurnNoticeSwitch = Constants.NORMAL_CODE;






}
