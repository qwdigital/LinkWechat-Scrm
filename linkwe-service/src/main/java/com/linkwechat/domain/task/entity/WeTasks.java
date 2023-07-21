package com.linkwechat.domain.task.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 待办任务
 * </p>
 *
 * @author WangYX
 * @since 2023-07-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("we_tasks")
public class WeTasks extends BaseEntity {

    /**
     * 主键Id
     */
    @TableId("id")
    private Long id;

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 员工企微Id
     */
    private String weUserId;

    /**
     * 任务类型
     *
     * @see com.linkwechat.common.enums.task.WeTasksTypeEnum
     */
    private Integer type;

    /**
     * 任务标题
     *
     * @see com.linkwechat.common.enums.task.WeTasksTypeEnum
     */
    private String title;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 链接
     */
    private String url;

    /**
     * 状态，0待执行，1已完成，2已取消
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Boolean delFlag;
}
