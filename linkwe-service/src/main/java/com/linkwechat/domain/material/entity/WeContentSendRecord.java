package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("we_content_send_record")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "发送人信息记录")
public class WeContentSendRecord extends BaseEntity {

    @TableId
    private Long id;

    /**
     * 话术Id
     */
    private Long talkId;

    /**
     * 素材Id
     */
    private Long contentId;

    /**
     * 发送者
     */
    private String sendBy;

    /**
     * 发送者Id
     */
    private Long sendById;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * 素材来源(1素材，2话术)
     */
    private Integer resourceType;

    @TableField(exist = false)
    private Integer sendCount;




}
