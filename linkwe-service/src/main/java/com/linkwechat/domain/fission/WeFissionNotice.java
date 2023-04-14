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
@TableName(value ="we_fission_notice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFissionNotice extends BaseEntity {
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
     * 当target_type为2时，当前字段为具体群成员的userId;当target_type为1时,当前字段客户的微信uid
     */
    private String targetSubId;


    /**
     * 消息id
     */
    private String msgId;


    /**
     * 员工是否已经将消息发送给老客： -1：失败  0：未执行 1：完成 2：取消
     */
    private Integer status;

}