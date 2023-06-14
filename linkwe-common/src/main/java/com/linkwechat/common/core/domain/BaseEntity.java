package com.linkwechat.common.core.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author ruoyi
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private String searchValue;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private String createBy;

    /**
     * 创建者ID
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private Long createById;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private String updateBy;

    /**
     * 更新者ID
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private Long updateById;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private String remark;

    /**
     * 开始时间
     */
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private String beginTime;

    /**
     * 结束时间
     */
    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private String endTime;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    @ExcelIgnore
    private Map<String, Object> params;


    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = DateUtils.getNowDate();
        } else {
            this.createTime = createTime;
        }

    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
