package com.linkwechat.domain.leads.record.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 跟进记录内容附件
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 15:27
 */
@Data
public class WeLeadsFollowRecordAttachmentRequest {

    @ApiModelProperty(value = "附件类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)", required = true)
    private Integer attachmentType;

    @ApiModelProperty(value = "附件名称", required = true)
    private String attachmentName;

    @ApiModelProperty(value = "附件地址", required = true)
    private String attachmentAddress;

}
