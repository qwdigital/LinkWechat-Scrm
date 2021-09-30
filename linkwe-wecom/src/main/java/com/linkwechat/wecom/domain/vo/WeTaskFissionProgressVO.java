package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeCustomer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.SuperBuilder;
import lombok.Data;

import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/3/20 13:12
 *
 * <p>
 * 客户任务进度和邀请列表
 * </p>
 */
@ApiModel
@Data
@SuperBuilder
public class WeTaskFissionProgressVO {
    @ApiModelProperty("总数")
    private Long total;

    @ApiModelProperty("完成数")
    private Long completed;

    @ApiModelProperty("客户列表")
    private List<WeCustomer> customers;
}
