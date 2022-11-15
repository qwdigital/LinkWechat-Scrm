package com.linkwechat.domain.msgtlp.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 欢迎语入参
 * @date 2022/03/26 13:49
 **/
@ApiModel
@Data
public class WeMsgTlpQuery extends BaseEntity {

    @ApiModelProperty("模板Id")
    private Long templateId;

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("模板类型:1:活码欢迎语;2:员工欢迎语;3:入群欢迎语")
    private Integer tplType;

    @ApiModelProperty("消息类型 文本:text, 图片:image, 视频:video, 文件:file, 图文消息:link, 小程序：miniprogram")
    private String msgType;

    @ApiModelProperty(value = "为false是不分页", hidden = true)
    private Boolean flag = true;

    @ApiModelProperty("欢迎语查询条件")
    private String welcomeMsg;

    //1欢迎语 2群发 3sop
    @ApiModelProperty("1欢迎语 2群发 3sop")
    private Integer templateType;

    private String templateInfo;

    private List<Long> ids;

    /**
     * 分组Id
     */
    private Long categoryId;

    /**
     *
     */
    private Long categoryIdNew;
}
