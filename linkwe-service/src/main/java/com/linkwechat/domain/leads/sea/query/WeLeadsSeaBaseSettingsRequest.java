package com.linkwechat.domain.leads.sea.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author zhaoyijie
 * @since 2023-04-04 16:49:45
 */
@Data
public class WeLeadsSeaBaseSettingsRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 员工每日领取上限
     */
    @Min(value = 1, message = "员工领取上限最小值为1")
    @ApiModelProperty(value = "员工每日领取上限")
    private Integer maxClaim;

    /**
     * 成员客户存量上限
     */
    @ApiModelProperty(value = "成员客户存量上限")
    private Integer stockMaxClaim;

}

