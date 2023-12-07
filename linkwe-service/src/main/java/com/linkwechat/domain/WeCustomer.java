package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


/**
 * 微信客户
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer")
public class WeCustomer extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 外部联系人的userid
     */
    private String externalUserid;

    /**
     * 外部联系人名称
     */
    private String customerName;

    /**
     * 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
     */
    private Integer customerType;

    /**
     * 外部联系人头像
     */
    private String avatar;

    /**
     * 外部联系人性别 0-未知 1-男性 2-女性
     */
    private Integer gender;

    /**
     * 外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来。
     */
    private String unionid;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 客户企业名称
     */
    private String corpName;

    /**
     * 客户职位
     */
    private String position;

    /**
     * 是否开启会话存档 0：关闭 1：开启
     */
    private Integer isOpenChat;

    /**
     * 开通会话存档时间
     */
    private Date openChatTime;

    /**
     * 添加人id
     */
    private String addUserId;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
     */
    private Integer trackState;

    /**
     * 跟进内容
     */
    private String trackContent;


    /**
     * 跟进时间
     */
    private Date trackTime;


    /**
     * 手机号
     */
    private String phone;


    /**
     * 邮箱
     */
    private String email;


    /**
     * QQ号
     */
    private String qq;


    /**
     * 其他描述
     */
    private String otherDescr;


    /**
     * 地址
     */
    private String address;


    /**
     * 当前接替人
     */
    private String takeoverUserId;


    /**
     *添加方式
     * @see com.linkwechat.common.enums.CustomerAddWay
     */
    private Integer addMethod;


    /**
     * 渠道,当前用户通过哪个活码添加
     */
    private String state;



    /** 备注名 */
    private String remarkName;



    /**省id*/
    private Integer provinceId;

    /**市id*/
    private Integer cityId;

    /**区id*/
    private Integer areaId;


    /** 标签ids冗余字段，方便快速查询*/
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private String tagIds;


    /**当前添加客户添加人是否离职 1:是; 0:否*/
    private Integer addUserLeave;

    /**
     * 0：正常;1:删除;
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 省/市/区
     */
    private String area;


    /**
     * 客户姓名
     */
    private String customerFullName;

    /**
     * 0:加入黑名单;1:不加入黑名单;
     */
    private Integer isJoinBlacklist;


    /**
     * 流失时间
     */
    private Date lossTime;



}
