package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 拉新活码信息表(WeLxQrCode)
 *
 * @author danmo
 * @since 2023-03-07 14:59:35
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_lx_qr_code")
public class WeLxQrCode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码名称
     */
    @ApiModelProperty(value = "活码名称")
    @TableField("name")
    private String name;


    /**
     * 拉新方式 1：红包 2：卡券
     */
    @ApiModelProperty(value = "拉新方式 1：红包 2：卡券")
    @TableField("type")
    private Integer type;


    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    @TableField("business_id")
    private String businessId;

    /**
     * 业务数据
     */
    @ApiModelProperty(value = "业务数据")
    @TableField("business_data")
    private String businessData;


    /**
     * 添加渠道
     */
    @ApiModelProperty(value = "添加渠道")
    @TableField("state")
    private String state;


    /**
     * 扫码次数
     */
    @ApiModelProperty(value = "扫码次数")
    @TableField("scan_num")
    private Integer scanNum;


    /**
     * 二维码配置id
     */
    @ApiModelProperty(value = "二维码配置id")
    @TableField("config_id")
    private String configId;


    /**
     * 二维码地址
     */
    @ApiModelProperty(value = "二维码地址")
    @TableField("qr_code")
    private String qrCode;


    /**
     * 链接地址
     */
    @ApiModelProperty(value = "链接地址")
    @TableField("link_path")
    private String linkPath;


    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    @TableField("image_url")
    private String imageUrl;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

}
