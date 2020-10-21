package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_group")
public class WeGroup {
    private static final long serialVersionUID = 1L;

    @TableId
    private String chatId;

    private String groupName;

    @TableField(exist = false)
    private Long memberNum;


    private String notice;

    private String owner;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @TableField(exist = false)
    private String groupLeaderName;
}
