package com.linkwechat.wecom.domain.vo.qr;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码适用范围出参
 * @date 2021/11/8 22:33
 **/
@ApiModel
@Data
public class WeQrScopeVo {

    @ApiModelProperty(value = "活码id")
    private Long qrId;

    @ApiModelProperty(value = "排期分组id")
    private String scopeId;

    @ApiModelProperty(value = "消息类型 0 默认排期 1 自定义排期")
    private Integer type;


    @ApiModelProperty(value = "周期时间")
    private String workCycle;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;


    @ApiModelProperty("员工姓名")
    private List<WeQrScopeUserVo> weQrUserList;

    @ApiModelProperty("部门名称")
    private List<WeQrScopePartyVo> weQrPartyList;

}
