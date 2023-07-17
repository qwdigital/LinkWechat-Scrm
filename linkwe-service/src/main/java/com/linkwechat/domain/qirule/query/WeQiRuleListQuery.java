package com.linkwechat.domain.qirule.query;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 质检列表入参
 * @date 2021/11/9 13:58
 **/
@ApiModel
@Data
public class WeQiRuleListQuery {

    @ApiModelProperty(value = "质检ID",hidden = true)
    private List<Long> qiRuleIds;

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("会话类型 不传-全部 2-客户会话 3-客群会话")
    private List<Integer> chatType;

    @ApiModelProperty("员工ID")
    private String userIds;

    @ApiModelProperty("督导ID")
    private String manageUserId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty(value = "排期查询",hidden = true)
    private Integer workCycle;

    @ApiModelProperty(value = "排期时间",hidden = true)
    private Date formatTime;

    @ApiModelProperty(value = "是否展示名称",hidden = true)
    private Boolean isShow = true;

}
