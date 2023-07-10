package com.linkwechat.domain.sop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 目标执行对象表
 * @TableName we_sop_execute_target
 */
@TableName(value ="we_sop_execute_target")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSopExecuteTarget extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * sop主键
     */
    private Long sopBaseId;

    /**
     * sop结束时间
     */
    private Date executeEndTime;

    /**
     * 执行根据成员id，无论选择部门还是啥最终都落实到具体员工
     */
    private String executeWeUserId;

    /**
     * 目标类型1:客户 2:群
     */
    private Integer targetType;

    /**
     * 目标id
     */
    private String targetId;

    /**
     * sop执行的状态(1:进行中;2:提前结束;3:正常结束;4:异常结束)
     */
    private Integer executeState;

    /**
     * sop子状态:0正常sop执行完,没有动作生成的sop需要执行；1:正常sop执行完,有动作生成的sop需要执行
     */
    private Integer executeSubState;

    /**
     * 执行人添加该客户的时间或创建该群时间
     */
    private Date addCustomerOrCreateGoupTime;

    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;


    /**
     * 群创建时间
     */
    @TableField(exist = false)
    private Date addTime;

}