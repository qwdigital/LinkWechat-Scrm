package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 
 * @TableName we_customer_link_count
 */
@TableName(value ="we_customer_link_count")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerLinkCount extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 客户external_userid
     */
    private String externalUserid;

    /**
     * 获客链接id
     */
    private String linkId;

    /**
     * 通过获客链接添加此客户的跟进人userid
     */
    private String weUserId;

    /**
     * 会话状态，0-客户未发消息 1-客户已发送消息
     */
    private Integer chatStatus;

    /**
     * 客户相关添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date addTime;

    /**
     * 用于区分客户具体是通过哪个获客链接进行添加，用户可在获客链接后拼接customer_channel=自定义字符串，字符串不超过64字节，超过会被截断。通过点击带有customer_channel参数的链接获取到的客户，调用获客信息接口或获取客户详情接口时，返回的state参数即为链接后拼接自定义字符串
     */
    private String state;


    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户类型 1:微信用户，2:企业用户
     */
    private Integer customerType;


    /**
     * 0-未知 1-男性 2-女性
     */
    private Integer gender;


    /**
     * 客户头像
     */
    private String avatar;

    /**
     * 添加人名称
     */
    private String userName;


    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;

}