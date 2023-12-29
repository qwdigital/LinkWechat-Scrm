package com.linkwechat.domain.storecode.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.converter.SexConverter;
import com.linkwechat.common.converter.WhetherConverter;
import com.linkwechat.converter.CustomerTypeConverter;
import lombok.Data;

import java.util.Date;

@Data
public class WeStoreCodeTableVo {
    /**
     * 头像
     */
    @ExcelIgnore
    private String avatar;

    /**
     * 客户名称
     */
    @ExcelProperty(value = "客户姓名", index = 0)
    @ColumnWidth(30)
    private String customerName;

    /**
     * 客户类型 1:微信用户，2:企业用户
     */
    @ExcelProperty(value = "客户类型", index = 1, converter = CustomerTypeConverter.class)
    private Integer customerType;

    /**
     * 客户id
     */
    @ExcelIgnore
    private String externalUserid;


    /**
     * 跟进人id
     */
    @ExcelIgnore
    private String addUserId;

    /**
     * 跟进人名称
     */
    @ExcelProperty(value = "跟进人名称", index = 3)
    private String addUserName;


    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;


    /**
     * 0-未知 1-男性 2-女性
     */
    @ExcelProperty(value = "性别", index = 2, converter = SexConverter.class)
    private Integer gender;




    /**
     * 进群数
     */
    @ExcelProperty(value = "进群数")
    private int joinGroupNumber;


    /**
     * 是否进群，1:是 0:否
     */
    @ExcelProperty(value = "是否进群", converter = WhetherConverter.class)
    private Integer isJoinGroup;

}
