package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @description 客服接口入参
 * @date 2021/12/13 10:27
 **/
@ApiModel
@Data
public class WeKfQuery extends WeBaseQuery {

    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("客服头像临时素材")
    private String media_id;

    @ApiModelProperty("客服帐号ID")
    @NotNull(message = "客服帐号ID不能为空")
    private String open_kfid;

    @ApiModelProperty("场景值，字符串类型，由开发者自定义")
    private String scene;

    @ApiModelProperty("员工ID")
    private List<Integer> accountid_list;

    @ApiModelProperty("接待人员userid列表")
    private List<String> userid_list;

    @ApiModelProperty("接待人员部门id列表")
    private List<Long> department_id_list;
}
