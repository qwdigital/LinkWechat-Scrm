package com.linkwechat.domain.form.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 答题-用户主表(WeFormSurveyAnswer)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@ExcelIgnoreUnannotated
@Data
public class WeFormSurveyAnswerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    //@Excel(name = "日期", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("日期")
    private Date createTime;

    /**
     * 用户姓名
     */
   // @Excel(name = "用户姓名")
    @ExcelProperty("用户姓名")
    private String name;

    /**
     * 渠道
     */
    //@Excel(name = "渠道")
    @ExcelProperty("渠道")
    private String dataSource;

    /**
     * 手机号
     */
    //@Excel(name = "用户手机号")
    @ExcelProperty("用户手机号")
    private String mobile;

    /**
     * 微信openID
     */
    //@Excel(name = "微信openID")
    @ExcelProperty("微信openID")
    private String openId;


    /**
     * 微信unionID
     */
    //@Excel(name = "微信unionID")
    @ExcelProperty("微信unionID")
    private String unionId;


    /**
     * 微信unionID
     */
    //@Excel(name = "是否是企业用户")
    @ExcelProperty("是否是企业用户")
    private String isCorpUser;

}
