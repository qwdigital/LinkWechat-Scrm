package com.linkwechat.domain.material.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class ContentAxisVo {
    @ApiModelProperty(value = "时间")
    private String dateStr;

    private Integer sendNum = 0;

    private Integer viewNum = 0;

    private Long viewByNum = 0L;
}
