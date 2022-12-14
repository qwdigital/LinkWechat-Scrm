package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 质量分析入参
 * @date 2022/11/28 17:09
 **/
@ApiModel
@Data
public class WeKfQualityStatQuery extends BaseEntity {

    @ApiModelProperty("客服账号ID")
    private List<String> openKfIds;

    @ApiModelProperty("接待人员ID")
    private List<String> userIds;

    @ApiModelProperty("类型 1-参评率 2-好评 3-一般 4-差评 （柱状图使用默认值2）")
    private Integer type = 2;
}
