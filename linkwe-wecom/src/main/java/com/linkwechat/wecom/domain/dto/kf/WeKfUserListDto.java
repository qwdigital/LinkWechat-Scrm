package com.linkwechat.wecom.domain.dto.kf;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
public class WeKfUserListDto extends WeResultDto {

    @ApiModelProperty("操作结果")
    private List<WeKfUserDto> resultList;

    @ApiModelProperty("客服帐号的接待人员列表")
    private List<WeKfUserDto> servicerList;
}
