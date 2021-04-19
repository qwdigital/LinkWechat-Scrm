package com.linkwechat.wecom.domain;

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


    /**应用密钥**/
    private String agentSecret;


    /**应用类型(1:自建应用;)**/
    private Integer appType;


    /**应用创建时间**/
    private Date createTime;



    /**删除标志（0代表存在 2代表删除）**/
    private String delFlag;



    /**帐号状态（0正常 1停用)**/
    private String status;






}
