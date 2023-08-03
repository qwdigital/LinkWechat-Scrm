package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 
 * @TableName we_sop_change
 */
@TableName(value ="we_sop_change")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSopChange extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 外部联系人的userid
     */
    private String externalUserid;

    /**
     * 添加人id
     */
    private String addUserId;

    /**
     * sop主键
     */
    private Long sopBaseId;


    /**
     * 删除标识 0 有效 1删除(表示已转入)
     */
    @TableLogic
    private Integer delFlag;


}