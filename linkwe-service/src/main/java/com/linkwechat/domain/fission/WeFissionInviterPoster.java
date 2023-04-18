package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 邀请人裂变海报
 * @TableName we_fission_inviter_poster
 */
@Data
@TableName(value ="we_fission_inviter_poster")
public class WeFissionInviterPoster extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 邀请人id，此处对应邀请人的unionid
     */
    private String inviterId;


    /**
     * 裂变海报对应的当前员工渠道标识
     */
    private String state;

    /**
     * 海报邀请码对应的员工config
     */
    private String config;

    /**
     * 裂变任务id
     */
    private Long fissionId;

    /**
     * 裂变海报url
     */
    private String fissionPosterUrl;

}