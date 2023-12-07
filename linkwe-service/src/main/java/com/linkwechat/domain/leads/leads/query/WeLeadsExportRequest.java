package com.linkwechat.domain.leads.leads.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 线索导出请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/13 11:29
 */
@Data
public class WeLeadsExportRequest extends WeLeadsBaseRequest {

    @ApiModelProperty(value = "为导出时用，线索是否全选 (全选true，不全选false) 默认不全选")
    protected Boolean all = Boolean.FALSE;

    @ApiModelProperty(value = "为导出时用，线索Id集合,all为false时，必填")
    protected List<Long> leadsIds;

    @ApiModelProperty(value = "为导出时用，全选时，未被选中的导入记录id集台，当为导出时不为空")
    protected List<Long> unLeadsIds;
}
