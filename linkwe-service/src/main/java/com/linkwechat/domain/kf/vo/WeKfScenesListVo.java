package com.linkwechat.domain.kf.vo;

import com.linkwechat.domain.WeKfScenes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 场景列表
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfScenesListVo extends WeKfScenes {

    @ApiModelProperty(value = "客服名称")
    private String kfName;

    @ApiModelProperty(value = "客服头像")
    private String kfAvatar;

    @ApiModelProperty(value = "访问客户数")
    private Integer accessCnt;

    @ApiModelProperty(value = "咨询客户数")
    private Integer consultCnt;

    @ApiModelProperty(value = "接待客户数")
    private Integer receptionCnt;
}
