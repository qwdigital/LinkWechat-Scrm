package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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


    /**应用密钥**/
    private String agentSecret;


    /**应用类型(1:自建应用;2:小程序)**/
    private Integer appType;


    /**应用创建时间**/
    private Date createTime;



    /**删除标志（0代表存在 2代表删除）**/
    private String delFlag;



    /**帐号状态（0正常 1停用)**/
    private String status;


    /**可见范围部门id，使用逗号隔开**/
    private String allowPartys;


    /**可见范围人员id，使用逗号隔开**/
    private String allowUserinfos;


    /**应用名称**/
    private String agentName;


    /**企业应用描述**/
    private String description;


    /**应用头像**/
    private String squareLogoUrl;


    @TableField(exist = false)
    private String logoMediaid;


}
