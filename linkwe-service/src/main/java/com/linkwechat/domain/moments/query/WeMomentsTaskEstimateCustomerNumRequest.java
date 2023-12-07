package com.linkwechat.domain.moments.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预估客户数量
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/07 16:59
 */
@ApiModel("预估客户数量")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeMomentsTaskEstimateCustomerNumRequest {

    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    @NotNull(message = "发送范围必填")
    @ApiModelProperty(value = "发送范围: 0全部客户 1按条件筛选")
    private Integer scopeType;

    /**
     * 部门id集合
     */
    @ApiModelProperty(value = "部门id集合")
    private List<Long> deptIds;

    /**
     * 岗位id集合
     */
    @ApiModelProperty(value = "岗位id集合")
    private List<String> posts;

    /**
     * 员工id集合
     */
    @ApiModelProperty(value = "员工id集合")
    private List<String> userIds;

    /**
     * 客户标签
     */
    @ApiModelProperty(value = "客户标签")
    private List<String> customerTag;

    /**
     * 客户查询条件
     */
    private WeCustomersQuery weCustomersQuery;

}
