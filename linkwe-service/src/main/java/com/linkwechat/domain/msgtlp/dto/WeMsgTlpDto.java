package com.linkwechat.domain.msgtlp.dto;

import com.linkwechat.domain.WeMsgTlp;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msgtlp.entity.WeTlpMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author 狗头军师
 * @Description TODO
 * @Date 2022/10/13 16:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeMsgTlpDto extends WeMsgTlp {

    @ApiModelProperty("员工ID列表")
    private List<String> userIdList;

    @ApiModelProperty("员工名称列表")
    private List<String> userNameList;

    @ApiModelProperty("欢迎语素材列表")
    @NotNull(message = "欢迎语素材列表不能为空")
    private List<WeMessageTemplate> attachmentList;

    @NotNull(message = "排序不能为空")
    private List<WeTlpMaterial> weTlpMaterialList;

}
