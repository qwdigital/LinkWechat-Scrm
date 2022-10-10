package com.linkwechat.domain.storecode.entity;

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
    private Long id;
    //门店名称
    @Excel(name="门店名称")
    private String storeName;
    //省id
    private Integer provinceId;
    //市id
    private Integer cityId;
    //区id
    private Integer areaId;
    //省市区
    @Excel(name = "所属地图")
    private String area;
    //详细地址
    @Excel(name = "详细地址")
    private String address;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    //导购id(we_user_id)，多个使用逗号隔开
    private String shopGuideId;
    //导购名称，多个使用逗号隔开
    private String shopGuideName;
    //导购活码url
    private String shopGuideUrl;
    //群活码
    private String groupCodeUrl;
    //门店状态(0:启用;1:关闭)
    private Integer storeState;

    //导购二维码configId
    private String configId;

    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    private Long storeCodeId;

    //群活码名称
    private String groupCodeName;

    //群活码id
    private Long groupCodeId;

    //渠道标识
    private String state;

 }
