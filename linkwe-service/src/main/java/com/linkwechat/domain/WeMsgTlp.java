package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.material.entity.WeMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 欢迎语模板表(WeMsgTlp)
 *
 * @author danmo
 * @since 2022-03-28 10:21:24
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_msg_tlp")
public class WeMsgTlp extends BaseEntity {


    @TableId
    private Long id;


    private Long categoryId;


    /**
     * 1欢迎语模板2群发模板3sop模板
     */
    private Integer templateType;


    /**
     * 模板内容
     */
    private String templateInfo;

    /**
     * 使用员工名称，用逗号隔开
     */
    @ApiModelProperty(value = "使用员工名称，用逗号隔开")
    @TableField("user_ids")
    private String userIds;


    /**
     * 使用员工名称，逗号隔开
     */
    @ApiModelProperty(value = "使用员工名称，逗号隔开")
    @TableField("user_names")
    private String userNames;


    /**
     * 模板类型:1:活码欢迎语;2:员工欢迎语;3:入群欢迎语
     */
    @ApiModelProperty(value = "模板类型:1:活码欢迎语;2:员工欢迎语;3:入群欢迎语")
    @TableField("tpl_type")
    private Integer tplType;


    /**
     * 模版id
     */
    private String templateId;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;



    @TableField(exist = false)
    private List<WeMaterial> weMaterialList;

    @Data
    public static  class  Applet{

        //小程序标题
        private String appTile;

        //小程序id
        private String appId;

        //小程序路径
        private String appPath;

        //小程序图片
        private String appPic;

    }

    @Data
    public static class ImageText{

        //图文标题
        private String imageTextTile;

        //图文url
        private String imageTextUrl;

    }
}
