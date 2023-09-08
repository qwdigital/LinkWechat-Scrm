package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 具有外部联系人功能员工记录表
 * @TableName we_flow_user_list
 */
@TableName(value ="we_flow_user_list")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFlowUserList extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 跟进人id
     */
    private String weUserId;



    /**
     * 删除标识 0 有效 1 删除
     */
    private String  externalUserid;


    /**
     * 0:等待同步;1:已经同步;2:同步失败;
     */
    private Integer synchState;

}