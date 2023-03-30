package com.linkwechat.domain.storecode.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 门店活码
 */
@Data
@TableName("we_store_code")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeStoreCode extends BaseEntity {
    //主键
    @TableId
    @ExcelIgnore
    private Long id;
    //门店名称
    @ExcelProperty("门店名称")
    private String storeName;
    //省id
    @ExcelIgnore
    private Integer provinceId;
    //市id
    @ExcelIgnore
    private Integer cityId;
    //区id
    @ExcelIgnore
    private Integer areaId;
    //省市区
    @ExcelProperty("所属地区")
    private String area;
    //详细地址
    @ExcelProperty("详细地址")
    private String address;
    //经度
    @ExcelIgnore
    private String longitude;
    //纬度
    @ExcelIgnore
    private String latitude;
    //导购id(we_user_id)，多个使用逗号隔开
    @ExcelIgnore
    private String shopGuideId;
    //导购名称，多个使用逗号隔开
    @ExcelIgnore
    private String shopGuideName;
    //导购活码url
    @ExcelIgnore
    private String shopGuideUrl;
    //群活码
    @ExcelIgnore
    private String groupCodeUrl;
    //门店状态(0:启用;1:关闭)
    @ExcelIgnore
    private Integer storeState;

    //导购二维码configId
    @ExcelIgnore
    private String configId;

    @TableLogic
    @ExcelIgnore
    private Integer delFlag;

    @TableField(exist = false)
    @ExcelIgnore
    private Long storeCodeId;

    //群活码名称
    @ExcelIgnore
    private String groupCodeName;

    //群活码id
    @ExcelIgnore
    private Long groupCodeId;

    //渠道标识
    @ExcelIgnore
    private String state;

 }
