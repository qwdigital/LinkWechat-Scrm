package com.linkwechat.domain.welcomemsg;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.media.WeMessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 默认欢迎语附件
 * @TableName we_default_welcome_msg
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="we_default_welcome_msg")
public class WeDefaultWelcomeMsg extends BaseEntity {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file 
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 消息链接
     */
    private String linkUrl;

    /**
     * 消息图片地址
     */
    private String picUrl;

    /**
     * 小程序appid
     */
    private String appId;

    /**
     * 素材真实类型
     */
    private Integer realType;


    private Long materialId;








}