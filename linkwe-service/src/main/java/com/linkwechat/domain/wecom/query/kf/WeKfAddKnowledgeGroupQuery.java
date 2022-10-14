package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 添加知识库分组入参
 * @date 2022/10/11 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfAddKnowledgeGroupQuery extends WeBaseQuery {

    @ApiModelProperty("分组名。不超过12个字")
    private String name;
}
