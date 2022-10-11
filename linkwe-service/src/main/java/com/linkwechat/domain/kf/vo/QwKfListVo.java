package com.linkwechat.domain.kf.vo;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.kf.WeKfUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服列表
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class QwKfListVo extends BaseEntity {
    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("客服账号Id")
    private String openKfId;

    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("客服头像")
    private String avatar;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("员工列表")
    private List<WeKfUser> userIdList;

    @ApiModelProperty("场景列表")
    private List<WeKfScenesVo> scenesList;
}
