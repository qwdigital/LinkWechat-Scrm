package com.linkwechat.domain.storecode.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.WeBuildUserOrGroupConditVo;
import com.linkwechat.domain.fission.vo.WeExecuteUserOrGroupConditVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 门店活码
 */
@Data
@TableName(value ="we_store_code",autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeStoreCode extends BaseEntity {
    //主键
    @TableId
    @ExcelIgnore
    private Long id;
    //门店名称
    @Excel(name = "门店名称")
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
    @Excel(name = "所属地区")
    @ExcelProperty("所属地区")
    private String area;
    //详细地址
    @Excel(name = "详细地址")
    @ExcelProperty("详细地址")
    private String address;
    //经度
    @ExcelIgnore
    private String longitude;
    //纬度
    @ExcelIgnore
    private String latitude;



    //门店状态(0:启用;1:关闭)
    @ExcelIgnore
    private Integer storeState;




    /**
     * 添加员工或群活码
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    @ExcelIgnore
    private WeBuildUserOrGroupConditVo addWeUserOrGroupCode;

    @TableLogic
    @ExcelIgnore
    private Integer delFlag;

    @TableField(exist = false)
    @ExcelIgnore
    private Long storeCodeId;


//    //导购id(we_user_id)，多个使用逗号隔开
//    @ExcelIgnore
//    private String shopGuideId;

    //导购名称，多个使用逗号隔开
    @ExcelIgnore
    private String shopGuideName;
    //导购活码url
    @ExcelIgnore
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String shopGuideUrl;

    //导购码渠道标识
    @ExcelIgnore
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String shopGuideState;


    //导购二维码configId
    @ExcelIgnore
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String shopGuideConfigId;




    //群活码名称,多个使用逗号隔开
    @ExcelIgnore
    private String groupCodeName;



    //群码渠道标识
    @ExcelIgnore
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String groupCodeState;

    //群码config
    @ExcelIgnore
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String groupCodeConfigId;

    //群活码
    @ExcelIgnore
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String groupCodeUrl;

}