package com.linkwechat.domain.shortlink.query;

import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.xmlbeans.impl.jam.mutable.MElement;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 短链推广
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@ApiModel
@Data
public class WeShortLinkPromotionUpdateQuery {

    /**
     * 主键Id
     */
    @NotNull(message = "主键Id必填")
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 推广短链Id
     */
    @ApiModelProperty(value = "推广短链Id")
    private Long shortLinkId;

    /**
     * 推广样式：0短链二维码 1短链海报
     */
    @ApiModelProperty(value = "推广样式：0短链二维码 1短链海报")
    private Integer style;

    /**
     * 海报素材Id
     */
    @ApiModelProperty(value = "海报素材Id")
    private Long materialId;

    /**
     * 推广方式：0群发客户，1群发客户群，2群发朋友圈，4应用消息
     */
    @ApiModelProperty(value = "推广方式：0群发客户，1群发客户群，2群发朋友圈，4应用消息")
    private Integer type;

    /**
     * 推广方式-客户
     */
    private WeShortLinkPromotionTemplateClientUpdateQuery client;

    /**
     * 推广方式-客群
     */
    private WeShortLinkPromotionTemplateGroupUpdateQuery group;


    @ApiModelProperty("附件列表")
    @Size(max = 8)
    private List<WeMessageTemplate> attachmentsList;

    /**
     * 指定接收消息的成员及对应客户列表
     */
    @Size(min = 1)
    @ApiModelProperty("指定接收消息的成员及对应客户列表")
    private List<WeAddGroupMessageQuery.SenderInfo> senderList;


}
