package com.linkwechat.domain.msgtlp.vo;

import com.linkwechat.domain.WeMsgTlp;
import com.linkwechat.domain.material.entity.WeMaterial;
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
public class WeMsgTlpVo extends WeMsgTlp {

    @ApiModelProperty("欢迎语素材列表")
    private List<WeMessageTemplate> attachments;


    private Long attachTotalNum;

    private List<WeMaterial> weMaterialList;
}
