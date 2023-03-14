package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 裂变明细子表
 * @TableName we_fission_detail_sub
 */
@Data
@TableName(value ="we_fission_detail_sub")
public class WeFissionDetailSub extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 裂变明细表id
     */
    private Long fissionDetailId;

    /**
     * 老客户外部联系人的userid
     */
    private String newExternalUserid;

    /**
     * 添加员工id
     */
    private String addWeUserid;

}