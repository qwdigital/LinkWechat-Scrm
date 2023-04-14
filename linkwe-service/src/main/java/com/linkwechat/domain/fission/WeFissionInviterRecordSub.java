package com.linkwechat.domain.fission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 裂变邀请记录
 * @TableName we_fission_detail_sub
 */
@Data
@TableName(value ="we_fission_inviter_record_sub")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFissionInviterRecordSub extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 裂变邀请主表id
     */
    private Long fissionInviterRecordId;


    /**
     * 被邀请人名称
     */
    private String inviterUserName;


    /**
     * 裂变客户添加的目标id，target_type为1的时候当前为企业员工id，为2的时候为群id
     */
    private String addTargetId;




    /**
     * 1:企业员工;2:群
     */
    private Integer addTargetType;


    /**
     * 头像
     */
    private String avatar;


    /**
     * 任务宝时当前id代表客户id，群裂变时当前id代表群成员id
     */
    private String userId;


    /**
     * 是否删除
     */
    @TableLogic
    private Integer delFlag;

}