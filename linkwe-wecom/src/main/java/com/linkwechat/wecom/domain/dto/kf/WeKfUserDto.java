package com.linkwechat.wecom.domain.dto.kf;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author danmo
 * @description 客服接待人员
 * @date 2021/10/9 15:19
 **/
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeKfUserDto extends WeResultDto {

    @ApiModelProperty("接待人员的userid")
    private String userId;

    @ApiModelProperty("接待人员的接待状态。0:接待中,1:停止接待")
    private Integer status;
}
