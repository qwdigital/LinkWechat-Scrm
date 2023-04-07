package com.linkwechat.domain.qr.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.tag.vo.WeTagVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 拉新活码详情出参
 * @date 2023/03/07 22:15
 **/
@ApiModel
@Data
public class WeLxQrCodeDetailVo {


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
     * 业务数据
     */
    @ApiModelProperty(value = "业务数据")
    private String businessData;


    /**
     * 添加渠道
     */
    @ApiModelProperty(value = "添加渠道")
    private String state;


    /**
     * 扫码次数
     */
    @ApiModelProperty(value = "扫码次数")
    private Integer scanNum;


    /**
     * 二维码配置id
     */
    @ApiModelProperty(value = "二维码配置id")
    private String configId;


    /**
     * 二维码地址
     */
    @ApiModelProperty(value = "二维码地址")
    private String qrCode;

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

    @ApiModelProperty("标签")
    private List<WeTagVo> qrTags;

    @ApiModelProperty("员工信息")
    private List<WeLxQrScopeUserVo> qrUserInfos;

    @ApiModelProperty("素材")
    private List<WeQrAttachments> qrAttachments;
}
