package com.linkwechat.domain.customer.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.converter.BlackListConverter;
import com.linkwechat.converter.CustomerAddWayConverter;
import com.linkwechat.converter.CustomerTrackStateConverter;
import com.linkwechat.converter.CustomerTypeConverter;
import com.linkwechat.common.converter.SexConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(25)
public class WeCustomersVo {

    /**
     * Id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 头像
     */
    @ExcelIgnore
    private String avatar;

    /**
     * 客户名称
     */
    @ExcelProperty(value = "客户姓名", index = 0)
    @ColumnWidth(30)
    private String customerName;

    /**
     * 客户类型 1:微信用户，2:企业用户
     */
    @ExcelProperty(value = "客户类型", index = 1, converter = CustomerTypeConverter.class)
    private Integer customerType;

    /**
     * 客户id
     */
    @ExcelIgnore
    private String externalUserid;

    /**
     * 跟进人id
     */
    @ExcelProperty(value = "userid", index = 3)
    private String firstUserId;

    /**
     * 跟踪状态
     * <p>
     * 和we_strack_stage表中的stage_val保持一致
     * </p>
     */
    @ExcelProperty(value = "商机阶段", index = 9, converter = CustomerTrackStateConverter.class)
    private Integer trackState;

    /**
     * 添加方式
     *
     * @see com.linkwechat.common.enums.CustomerAddWay
     */
    @ExcelProperty(value = "添加方式", index = 10, converter = CustomerAddWayConverter.class)
    private Integer addMethod;

    /**
     * 添加时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "添加时间", index = 11)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstAddTime;

    /**
     * 生日
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "生日", index = 5)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 留存天数
     */
    @ExcelIgnore
    private Integer retentionDays;


    /**
     * 0-未知 1-男性 2-女性
     */
    @ExcelProperty(value = "性别", index = 2, converter = SexConverter.class)
    private Integer gender;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号", index = 13)
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱", index = 14)
    private String email;

    /**
     * 地址
     */
    @ExcelProperty(value = "详细地址", index = 17)
    private String address;

    /**
     * qq
     */
    @ExcelProperty(value = "QQ", index = 15)
    private String qq;

    /**
     * 职位
     */
    @ExcelProperty(value = "所属职位", index = 7)
    private String position;

    /**
     * 公司名称
     */
    @ExcelProperty(value = "所属企业", index = 6)
    private String corpName;

    //描述
    @ExcelIgnore
    private String otherDescr;

    /**
     * 跟进人名称
     */
    @ExcelProperty(value = "跟进员工", index = 8)
    private String userName;

    //个人标签id
    @ExcelIgnore
    private String personTagIds;

    //个人标签名
    @ExcelIgnore
    private String personTagNames;


    //查询标签id(企业标签id)
    @ExcelIgnore
    private String tagIds;

    /**
     * 标签名称，使用逗号隔开(企业标签名)
     */
    @ExcelProperty(value = "企业标签", index = 12)
    private String tagNames;

    /**
     * 外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来。
     */
    @ExcelProperty(value = "unionid", index = 4)
    @ColumnWidth(30)
    private String unionid;

    /**
     * 跟进时间或流失时间
     */
    @ExcelIgnore
    private Date trackTime;

    /**
     * 数据更新时间,也可表示客户流失时间
     */
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 地区
     */
    @ExcelProperty(value = "所在省市县", index = 16)
    private String area;


    /**
     * 0:加入黑名单;1:不加入黑名单;
     */
    @ExcelProperty(value = "是否加入黑名单", index = 18, converter = BlackListConverter.class)
    private Integer isJoinBlacklist;


    /**
     * 流失时间
     */
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lossTime;





}
