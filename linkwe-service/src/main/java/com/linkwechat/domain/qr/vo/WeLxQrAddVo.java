package com.linkwechat.domain.qr.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 拉新活码新增出参
 * @date 2023/03/07 22:33
 **/
@ApiModel
@Data
public class WeLxQrAddVo {

    @ApiModelProperty(value = "活码id")
    private Long qrId;

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
