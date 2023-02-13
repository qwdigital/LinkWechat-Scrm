package com.linkwechat.domain.know;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.converters.url.UrlImageConverter;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.know.vo.WeKnowCustomerJumpContentVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.user.vo.WeUserScreenConditVo;
import lombok.Data;

import java.net.URL;
import java.util.Date;
import java.util.List;


/**
 * 识客码
 * @TableName we_know_customer_code
 */
@TableName(value ="we_know_customer_code",autoResultMap = true)
@Data
public class WeKnowCustomerCode extends BaseEntity{
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ExcelIgnore
    private Long id;

    /**
     * 识客码名称
     */
    @ExcelProperty("识客码名称")
    private String knowCustomerName;

    /**
     * 添加的成员,相关信息
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    @ExcelIgnore
    private WeUserScreenConditVo addWeUser;


    /**
     * 添加标签
     */
    @TableField(exist = false)
    @ExcelIgnore
    private  List<WeKnowCustomerCodeTag> weKnowCustomerCodeTags;

    /**
     * 海报的url
     */
    @ExcelIgnore
    private String posterUrl;

    /**
     * 海报id
     */
    @ExcelIgnore
    private Long postersId;


    /**
     * 素材新增编辑传入(欢迎语和附件)
     */
    @TableField(exist = false)
    @ExcelIgnore
    private List<WeMessageTemplate> attachments;


    /**
     * 识客码url
     */
    @ExcelIgnore
    private String knowCustomerUrl;

    /**
     * 识客码qr
     */
    @ExcelProperty(value="二维码")
    private String knowCustomerQr;



    /**
     * 老客识别类型（是否添加所有成员）:1:已添加任意成员;0:已添加指定成员
     */
    @ExcelIgnore
    private Integer isAddAllUser;

    /**
     * 已添加指定成员的条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    @ExcelIgnore
    private WeUserScreenConditVo appointWeUser;

    /**
     * 跳转指定内容类型(1:员工活码;2:链接;3:小程序)
     */
    @ExcelIgnore
    private Integer jumpContentType;

    /**
     * 跳转实际内容
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    @ExcelIgnore
    private WeKnowCustomerJumpContentVo jumpContent;






    /**
     * 语素材返回展示
     */
    @TableField(exist = false)
    @ExcelIgnore
    private List<WeKnowCustomerAttachments> weKnowCustomerAttachments;


    /**
     * 添加成员活码config
     */
    @ExcelIgnore
    private String addWeUserConfig;

    /**
     * 添加成员活码url
     */
    @ExcelIgnore
    private String addWeUserUrl;




    /**
     * 今日扫码次数
     */
    @ExcelProperty("今日扫码次数")
    @TableField(exist = false)
    private Long tdScanCodeNumber;

    /**
     * 扫码总次数
     */
    @ExcelProperty("扫码总次数")
    @TableField(exist = false)
    private Long totalScanCodeNumber;

    /**
     * 创建人
     */
    @ExcelProperty("创建人")
    @TableField(exist = false)
    private String createName;


    /**
     * 创建时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("创建时间")
    @TableField(exist = false)
    private Date createAddTime;


    /**
     * 是否为新老客 true:新客 false:老客
     */
    @TableField(exist = false)
    @ExcelIgnore
    private boolean newOrOldWeCustomer=true;


    /**
     * 员工二维码渠道标识
     */
    @ExcelIgnore
    private String addWeUserState;

    @TableLogic
    @ExcelIgnore
    private Integer delFlag;




}