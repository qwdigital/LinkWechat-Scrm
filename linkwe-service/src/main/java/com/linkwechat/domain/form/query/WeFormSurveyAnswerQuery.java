package com.linkwechat.domain.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author danmo
 * @date 2022年09月20日 18:24
 */
@Data
public class WeFormSurveyAnswerQuery {


    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷ID不能为空")
    private Long belongId;

    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 微信openId
     */
    @ApiModelProperty("微信OpenId")
    private String openId;


    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddr;
}
