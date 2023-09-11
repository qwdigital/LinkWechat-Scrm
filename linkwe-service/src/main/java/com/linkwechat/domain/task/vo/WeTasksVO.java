package com.linkwechat.domain.task.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待办任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 17:59
 */
@Data
public class WeTasksVO {

    /**
     * 主键Id
     */
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
     */
    private Integer type;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH::mm:ss", timezone = "GMT+8")
    private LocalDateTime sendTime;

    /**
     * 链接
     */
    private String url;

    /**
     * 状态，0待执行，1已完成，2已取消
     */
    private Integer status;

}
