package com.linkwechat.domain.qr.vo;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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


}
