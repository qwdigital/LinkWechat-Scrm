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
 * @description 客服客户信息
 * @date 2021/10/9 15:49
 **/

@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeKfCustomerInfoDto extends WeResultDto {

    @ApiModelProperty("微信客户列表")
    private List<WeCustomerServiceCustomerInfo> customerList;

    @ApiModelProperty("无效的客户id")
    private List<String> invalidExternalUserId;


    @ApiModel
    @Data
    public static class WeCustomerServiceCustomerInfo {

        @ApiModelProperty("外部联系人id")
        private String externalUserId;

        @ApiModelProperty("昵称")
        private String nickName;

        @ApiModelProperty("头像")
        private String avatar;

        @ApiModelProperty("性别")
        private String gender;

        @ApiModelProperty("unionid")
        private String unionId;
    }
}
