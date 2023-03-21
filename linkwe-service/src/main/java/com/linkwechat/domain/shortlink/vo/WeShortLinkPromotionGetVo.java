package com.linkwechat.domain.shortlink.vo;

import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.domain.material.vo.WePosterVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 短链推广
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/14 13:43
 */
@ApiModel
@Data
public class WeShortLinkPromotionGetVo {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 任务状态: 0带推广 1推广中 2已结束
     */
    @ApiModelProperty(value = "任务状态: 0带推广 1推广中 2已结束")
    private Integer taskStatus;

    /**
     * 推广短链Id
     */
    @ApiModelProperty(value = "推广短链Id")
    private Long shortLinkId;

    /**
     * 短链
     */
    @ApiModelProperty(value = "短链")
    private WeShortLinkListVo shortLink;

    /**
     * 推广样式：0短链二维码 1短链海报
     */
    @ApiModelProperty(value = "推广样式：0短链二维码 1短链海报")
    private Integer style;

    /**
     * url地址（二维码或海报的地址）
     */
    @ApiModelProperty(value = "url地址（二维码或海报的地址）")
    private String url;

    /**
     * 推广方式：0群发客户，1群发客户群，2群发朋友圈，3应用消息
     */
    @ApiModelProperty(value = "推广方式：0群发客户，1群发客户群，2群发朋友圈，3应用消息")
    private Integer type;

    /**
     * 海报素材Id
     */
    @ApiModelProperty(value = "海报素材Id")
    private Long materialId;

    /**
     * 海报详情
     */
    @ApiModelProperty(value = "海报详情")
    private WeMaterialVo weMaterial;

    /**
     * 客户推广信息
     */
    @ApiModelProperty(value = "客户推广信息")
    private WeShortLinkPromotionTemplateClientVo client;

    /**
     * 客群推广信息
     */
    @ApiModelProperty(value = "客群推广信息")
    private WeShortLinkPromotionTemplateGroupVo group;

    /**
     * 朋友圈推广信息
     */
    @ApiModelProperty(value = "朋友圈推广信息")
    private WeShortLinkPromotionTemplateMomentsVo moments;

    /**
     * 应用消息推广信息
     */
    @ApiModelProperty(value = "应用消息推广信息")
    private WeShortLinkPromotionTemplateAppMsgVo appMsg;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<WeMessageTemplate> attachments;

}
