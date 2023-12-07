package com.linkwechat.domain.leads.record.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 跟进记录内容协作成员
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 15:51
 */
@Data
@Builder
@TableName(value = "we_leads_record_content_cooperate_user")
@NoArgsConstructor
@AllArgsConstructor
public class WeLeadsFollowRecordCooperateUser {

    /**
     * 主键Id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 记录内容Id
     */
    private Long contentId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户企微Id
     */
    private String weUserId;

    /**
     * 用户名称
     */
    private String userName;
}
