package com.linkwechat.domain.operation.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 运营入参
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeOperationGroupQuery extends BaseEntity {

    @ApiModelProperty("群主id")
    private List<String> ownerIds;

    @ApiModelProperty("群聊id")
    private List<String> chatIds;
}
