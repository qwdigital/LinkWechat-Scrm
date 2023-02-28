package com.linkwechat.domain.kf.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.enums.WeKfStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author danmo
 * @description 场景列表
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfRecordListVo{

    @ApiModelProperty(value = "客服账号ID")
    @ExcelIgnore
    private String openKfId;

    @ApiModelProperty(value = "连接池ID")
    @ExcelIgnore
    private String poolId;

    @ApiModelProperty(value = "客服名称")
    @ExcelProperty("客服名称")
    private String kfName;

    @ApiModelProperty(value = "客服头像")
    @ExcelIgnore
    private String kfAvatar;

    @ApiModelProperty(value = "客户名称")
    @ExcelProperty("客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户头像")
    @ExcelIgnore
    private String customerAvatar;

    @ApiModelProperty(value = "客户ID")
    @ExcelIgnore
    private String externalUserId;

    @ApiModelProperty(value = "场景值")
    @ExcelIgnore
    private String scene;

    @ApiModelProperty(value = "场景名称")
    @ExcelProperty("场景名称")
    private String sceneName;

    @ApiModelProperty(value = "员工ID")
    @ExcelIgnore
    private String userId;

    @ApiModelProperty(value = "员工名称")
    @ExcelProperty("员工名称")
    private String userName;

    @ApiModelProperty(value = "是否为企业客户 0=是,1=否")
    @ExcelIgnore
    private Integer isQyCustomer = 1;

    @ApiModelProperty(value = "是否为企业客户",hidden = true)
    @ExcelProperty(value = "是否为企业客户")
    private String isQyCustomerStr;

    @ApiModelProperty(value = "接待状态 0-未处理,1-机器人,2-接待池,3-人工接待,4-已结束/未开始")
    @ExcelIgnore
    private Integer status;

    @ApiModelProperty(value = "接待状态",hidden = true)
    @ExcelProperty(value = "接待状态")
    private String statusStr;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    @ExcelIgnore
    private Integer receptionType;

    @ApiModelProperty(value = "接待方式",hidden = true)
    @ExcelProperty(value = "接待方式")
    private String receptionTypeStr;

    @ApiModelProperty(value = "会话开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "会话开始时间")
    private Date sessionStartTime;

    @ApiModelProperty(value = "会话结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "会话结束时间")
    private Date sessionEndTime;

    @ApiModelProperty(value = "时长")
    @ExcelProperty("时长")
    private String duration;

    public String getDuration() {
        if(this.duration == null){
            return null;
        }
        long second = Math.round(Double.parseDouble(duration));
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        if(hours>0){
            return hours + "h" + minutes + "m" + second + "s";
        }else{
            return minutes + "m" + second + "s";
        }
    }

    public String getIsQyCustomerStr() {
        if(isQyCustomer == 0){
            return "是";
        }else if(isQyCustomer == 1){
            return "否";
        }
        return "";
    }

    public String getStatusStr() {
        WeKfStatusEnum weKfStatusEnum = WeKfStatusEnum.parseEnum(status);
        return weKfStatusEnum.getMsg();
    }

    public String getReceptionTypeStr() {
        if(receptionType == 1){
            return "人工客服";
        }else if(receptionType == 2){
            return "智能助手";
        }
        return "";
    }

}
