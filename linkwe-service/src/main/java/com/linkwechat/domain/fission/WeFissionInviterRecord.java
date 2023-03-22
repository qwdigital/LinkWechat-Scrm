package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邀请记录主表
 * @TableName we_fission_inviter_record
 */
@Data
@TableName(value ="we_fission_inviter_record")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFissionInviterRecord extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 裂变明细表id
     */
    private Long fissionId;

    /**
     * 邀请人unionid
     */
    private String inviterUnionid;

    /**
     * 邀请数量
     */
    private Integer inviterNumber;

    /**
     * 1:已完成;2:未完成
     */
    private Integer inviterState;


    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;

}