package com.linkwechat.domain.taggroup;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 老客户标签建群客户统计(用于个人群发类型的统计)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("we_pres_tag_group_stat")
public class WePresTagGroupTaskStat extends BaseEntity {


    @TableId
    private Long id;

    /**
     * 老客户标签建群任务id
     */
    private Long taskId;


    /**
     * 跟进者id
     */
    private String userId;

    /**
     * 客户external_id
     */
    private String externalUserid;

    /**
     * 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    private Integer sent;


    /**
     * 企业微信消息id
     */
    private String msgId;


}
