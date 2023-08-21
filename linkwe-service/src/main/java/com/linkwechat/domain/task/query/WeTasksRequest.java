package com.linkwechat.domain.task.query;

import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordCooperateUserRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 待办任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/24 9:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeTasksRequest {

    /**
     * 待办任务Id
     */
    private Long id;

    /**
     * 待办任务类型
     *
     * @see com.linkwechat.common.enums.task.WeTasksTitleEnum
     */
    private Integer type;

    /**
     * 处理方式 0处理 1取消
     */
    private Integer mode;

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 员工企微Id
     */
    private String weUserId;

    /**
     * 员工名称
     */
    private String userName;

    /**
     * 线索中心-线索Id
     */
    private Long leadsId;

    /**
     * 跟进记录Id
     */
    private Long recordId;

    /**
     * 约定时间
     */
    private Date cooperateTime;

    /**
     * 协作成员列表
     */
    @ApiModelProperty(value = "协作成员列表")
    private List<WeLeadsFollowRecordCooperateUserRequest> cooperateUsers;

    /**
     * 员工Id集合，（标签建群任务使用）
     */
    private List<String> weUserIds;

    /**
     * 客户SOP或者客群SOP代办任务内容
     */
    private String content;

}
