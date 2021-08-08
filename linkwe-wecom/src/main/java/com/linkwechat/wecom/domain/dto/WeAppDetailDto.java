package com.linkwechat.wecom.domain.dto;


import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业应用详情接口
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeAppDetailDto extends WeResultDto{
    //企业应用id
    private String agentid;
    //企业应用名称
    private String name;
    //企业应用方形头像
    private String square_logo_url;
    //企业应用详情
    private String description;
    //企业应用是否被停用
    private Boolean  close;
    //企业应用可信域名
    private String redirect_domain;
    //企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；
    private Integer report_location_flag;
    //是否上报用户进入应用事件。0：不接收；1：接收
    private Integer isreportenter;
    //应用主页url
    private String home_url;
    //企业应用头像的mediaid，通过素材管理接口上传图片获得mediaid，上传后会自动裁剪成方形和圆形两个头像
    private String logo_mediaid;

    //企业应用可见范围（人员）
    private AllowUserinfos allow_userinfos;

    //企业应用可见范围（部门）
    private AllowPartys allow_partys;



    //可见部门
    @Data
    public class AllowPartys{
        private String[] partyid;

    }


    @Data
    public class AllowUserinfos{
        private user[] user;
    }

    @Data
    public class user{
        private String userid;
    }



}
