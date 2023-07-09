package com.linkwechat.domain;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 
* @TableName sys_leave_user
*/
@TableName("sys_leave_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLeaveUser extends BaseEntity {

    /**
    * 主键
    */
    @TableId
    private Long id;
    /**
    * 离职员工名称
    */
    private String userName;
    /**
    * 离职员工所在部门名称
    */
    private String deptNames;
    /**
    * 员工id
    */
    private String weUserId;
    /**
    * 分配客户数
    */
    private Integer allocateCustomerNum;
    /**
    * 分配群数
    */
    private Integer allocateGroupNum;
    /**
    * 离职时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dimissionTime;
    /**
    * 是否已分配:1:是;0:否
    */
    private Integer isAllocate;

    /**
    * 0:正常;1:删除;
    */
    @TableField
    private Integer delFlag;


}
