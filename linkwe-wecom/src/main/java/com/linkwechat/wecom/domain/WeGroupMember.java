package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:56
 */
@Data
@TableName("we_group_member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupMember {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id= SnowFlakeUtil.nextId();;

    @NotBlank(message = "groupId")
    private String chatId;

    @TableField(exist = false)
    private String memberName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;

    private Integer joinScene;

    @TableField(value="type")
    private Integer joinType;


    private String userId;

    private String unionId;






}
