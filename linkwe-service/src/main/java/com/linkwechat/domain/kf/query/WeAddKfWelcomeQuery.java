package com.linkwechat.domain.kf.query;

import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author danmo
 * @description 新增客服入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeAddKfWelcomeQuery {

    @NotNull(message = "客服ID不能为空")
    private Long id;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("是否分时段: 1-否 2-是")
    private Integer splitTime;

    @NotEmpty(message = "欢迎语不能为空")
    @Size(min = 1, message = "欢迎语不能为空")
    @ApiModelProperty("欢迎语")
    private List<WeKfWelcomeInfo> welcome;
}
