package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 裂变明细表
 * @TableName we_fission_detail
 */
@Data
@TableName(value ="we_fission_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFissionDetail extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 裂变id
     */
    private Long fissionId;

    /**
     * 发送的目标id，target_type为1的时候当前为客户id，为2的时候为群id
     */
    private String targetId;

    /**
     * 1:老客;2:群
     */
    private Integer targetType;

    /**
     * 裂变发送员工we_user_id
     */
    private String sendWeUserid;

    /**
     * 裂变状态: 1:已完成 2: 未完成
     */
    private Integer fissionState;

    /**
     * 消息id
     */
    private String msgId;

}