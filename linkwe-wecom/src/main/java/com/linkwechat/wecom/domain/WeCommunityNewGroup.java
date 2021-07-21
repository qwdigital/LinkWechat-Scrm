package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@ApiModel
@Data
@TableName("we_community_new_group")
public class WeCommunityNewGroup extends BaseEntity {

    /**
     *主键ID
     */
    @TableId
    private Long id = SnowFlakeUtil.nextId();

    /**
     * 员工活码名称
     */
    private String emplCodeName;

    /**
     * 群活码id
     */
    private Long groupCodeId;

    /**
     * 员工活码id
     */
    private Long emplCodeId;

    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private int delFlag;
}
