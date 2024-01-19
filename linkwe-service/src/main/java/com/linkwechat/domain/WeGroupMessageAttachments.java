package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;


import lombok.Data;

/**
 * 群发消息附件表(WeGroupMessageAttachments)
 *
 * @author danmo
 * @since 2022-04-06 22:29:03
 */
@Data
@SuppressWarnings("serial")
@TableName("we_group_message_attachments")
public class WeGroupMessageAttachments extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @TableId
    private Long id;


    /**
     * 消息模板id
     */
    private Long msgTemplateId;


    /**
     * 企业群发消息的id
     */
    private String msgId;


    /**
     * 消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file
     */
    private String msgType;


    /**
     * 消息内容
     */
    private String content;


    /**
     * 媒体id
     */
    private String mediaId;


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
     * 删除标识 0 有效 1删除
     */
    @TableLogic
    private Integer delFlag;


    /**
     * 真实数据类型，（其他类型的数据，转成链接之后的真是的数据类型）
     */
    private Integer realType;

    /**
     * 素材中心Id
     */
    private Long materialId;
}
