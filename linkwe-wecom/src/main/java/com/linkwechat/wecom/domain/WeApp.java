package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 自建应用相关
 * @author: HaoN
 * @create: 2021-01-26 18:38
 **/
@Data
@TableName("we_app")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeApp {

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /**应用id**/
    private String agentId;

    /**应用名称**/
    private String agentName;


    /**应用密钥**/
    private String agentSecret;


    /**应用描述**/
    private String description;

    /**应用图标**/
    private String squareLogoUrl;

    /**企业应用是否被停用(1:是;0:否)**/
    private Integer close;


    /**企业应用可信域名**/
    private String redirectDomain;


    /**企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；**/
    private Integer reportLocationFlag;


    /**是否上报用户进入应用事件。0：不接收；1：接收**/
    private Integer isreportenter;


    /**应用主页url**/
    private String homeUrl;


    /**应用类型(1:自建应用;)**/
    private Integer appType;


    /**应用创建时间**/
    private Date createTime;



    /**删除标志（0代表存在 2代表删除）**/
    private String delFlag;



    /**帐号状态（0正常 1停用)**/
    private String status;


    /**企业应用头像的mediaid，通过素材管理接口上传图片获得mediaid，上传后会自动裁剪成方形和圆形两个头像**/
    @TableField(exist = false)
    private String logoMediaid;






}
