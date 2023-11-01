package com.linkwechat.domain.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author danmo
 * @date 2022年09月20日 18:24
 */
@Data
public class WeAddFormSurveyAnswerQuery {


    @ApiModelProperty(value = "id(修改是传)")
    private Long id;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    //@NotNull(message = "手机号不能为空")
    private String mobile;


    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String name;


    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;


    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String addr;


    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;


    /**
     * 微信openID
     */
    @ApiModelProperty(value = "微信openID")
    private String openId;


    /**
     * 微信unionID
     */
    @ApiModelProperty(value = "微信unionID")
//    @NotEmpty(message = "用户ID不能为空")
    private String unionId;


    /**
     * 答题开始时间
     */
    @ApiModelProperty(value = "答题开始时间")
    private Date anTime;


    /**
     * 答题用时
     */
    @ApiModelProperty(value = "答题用时")
    private Float totalTime;


    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddr;


    /**
     * 答案
     */
    @ApiModelProperty(value = "答案")
    private String answer;


    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id")
    private Long belongId;


    /**
     * 是否完成;0完成，1未完成
     */
    @ApiModelProperty(value = "是否完成;0完成，1未完成")
    private Integer anEffective;


    /**
     * 答题数
     */
    @ApiModelProperty(value = "答题数")
    private Integer quNum;


    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    private String dataSource;
}
