package com.linkwechat.domain.qr.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 拉新活码列表出参
 * @date 2023/03/07 22:15
 **/
@ApiModel
@Data
public class WeLxQrCodeListVo extends BaseEntity {


    /**
     * 活码ID
     */
    @ApiModelProperty(value = "活码ID")
    private Long id;


    /**
     * 活码名称
     */
    @ApiModelProperty(value = "活码名称")
    private String name;

    /**
     * 拉新方式 1：红包 2：卡券
     */
    @ApiModelProperty(value = "拉新方式 1：红包 2：卡券")
    private Integer type;

    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    /**
     * 扫码次数
     */
    @ApiModelProperty(value = "扫码次数")
    private Integer scanNum;


    @ApiModelProperty("新客领取数")
    private Integer receiveNum = 0;

    /**
     * 链接地址
     */
    @ApiModelProperty(value = "链接地址")
    private String linkPath;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createBy;

    /** 创建者ID */
    @ApiModelProperty("创建者ID")
    private Long createById;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("更新者ID")
    private Long updateById;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
