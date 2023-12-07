package com.linkwechat.domain.qr.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.tag.vo.WeTagVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码详情出参
 * @date 2021/11/8 22:15
 **/
@ApiModel
@Data
public class WeQrCodeDetailVo extends WeQrCode {

    @ApiModelProperty("分组名称")
    private String qrGroupName;

    @ApiModelProperty("标签")
    private List<WeTagVo> qrTags;

    @ApiModelProperty("适用范围")
    private List<WeQrScopeVo> qrUserInfos;

    @ApiModelProperty("素材")
    private List<WeQrAttachments> qrAttachments;


    @ApiModelProperty("活码短链")
    private String qrShortLink;
}
