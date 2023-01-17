package com.linkwechat.domain.wecom.query.media;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/24 0024 0:16
 */
@Data
public class WeMediaQuery extends WeBaseQuery {
    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
     */
    private String type;

    /**
     * 群机器人用
     */
    private String key;
    /**
     * 媒体文件上传后获取的唯一标识，3天内有效
     */
    private String mediaId;

    /**
     * 文件路径
     */
    private String url;

    /**
     * 文件名称
     */
    private String name;

    @ApiModelProperty("文件")
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

    /**
     * 附件类型
     */
    private Integer attachmentType;

    public WeMediaQuery() {}

    public WeMediaQuery(String mediaId) {
        this.mediaId=mediaId;
    }

    public WeMediaQuery(String type, String url, String name) {
        this.type = type;
        this.url = url;
        this.name = name;
    }

    public WeMediaQuery(String key, String type, String url, String name) {
        this.key = key;
        this.type = type;
        this.url = url;
        this.name = name;
    }

    public WeMediaQuery(MultipartFile file) {
        this.file = file;
    }
}
