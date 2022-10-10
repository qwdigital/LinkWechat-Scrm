package com.linkwechat.domain.msgtlp.query;

import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @description 欢迎语新增入参
 * @date 2022/03/26 13:49
 **/
@ApiModel
@Data
public class WeMsgTlpAddQuery {

    @ApiModelProperty("模板Id")
    private Long id;

    @ApiModelProperty("员工ID列表")
    private List<String> userIds;

    @ApiModelProperty("员工名称列表")
    private List<String> userNames;

    @ApiModelProperty("模板类型:1:活码欢迎语;2:员工欢迎语;3:入群欢迎语")
    @NotBlank(message = "模板类型不能为空")
    private Integer tplType;

    @ApiModelProperty("欢迎语素材列表")
    @NotNull(message = "欢迎语素材列表不能为空")
    private List<WeMessageTemplate> attachments;

}
