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
 * @description 客服账号列表企微实体
 * @date 2021/10/9 14:36
 **/
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeKfListDto extends WeResultDto{

    @ApiModelProperty("帐号信息列表")
    private List<WeKfDto>  accountList;
}
