package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 裂变明细表
 * @TableName we_fission_detail
 */
@Data
@TableName(value ="we_fission_detail")
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
     * 老客户外部联系人的userid
     */
    private String oldExternalUserid;

    /**
     * 裂变发送员工we_user_id
     */
    private String sendWeUserid;

    /**
     * 裂变状态: 1:已完成 2: 未完成
     */
    private Integer fissionState;

}