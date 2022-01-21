package com.linkwechat.wecom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;


/**
 * 客户详情
 */
@Data
public class WeCustomerDetail {
    //头像
    private  String avatar;
    //客户名称
    private  String customerName;
    //客户类型
    private Integer customerType;
    //客户标签，多个用逗号隔开
    private String tagNames;

    //跟进员工
    private List<TrackUser> trackUsers;

    //所在客群
    private List<Groups> groups;


    private String commonGroupChat;

    private String phone;

    private String address;

    private int age;

    private String qq;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String position;

    private String email;

    private String corpName;

    private String otherDescr;

    //0-未知 1-男性 2-女性
    private Integer gender;


    private List<CompanyOrPersonTag> companyTags;


    private List<CompanyOrPersonTag> personTags;


    private List<TrackStates> trackStates;




    @Data
    @Builder
    public static class TrackUser {

        //添加员工名称
        private String userName;

        //添加方式
        private Integer addMethod;

        //跟进人id
        private String trackUserId;

        //添加时间
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date firstAddTime;

        //跟进状态
        private Integer trackState;

    }

    @Data
    public static class  Groups{

        //群id
        private String chatId;

        //群名
        private String groupName;

        //加入时间
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date joinTime;

        //群主
        private String leaderName;

        //入群方式入群方式。
        //1 - 由群成员邀请入群（直接邀请入群）
        //2 - 由群成员邀请入群（通过邀请链接入群）
        //3 - 通过扫描群二维码入群
        private Integer joinScene;



    }





    //企业或个人标签
    @Data
    @Builder
    public static class  CompanyOrPersonTag{

        private String userName;
        private String tagNames;
        private String tagIds;


    }

    @Data
    @Builder
    public  static class  TrackStates{
        //跟进人
        private String userName;

        //跟进状态列表
        private List<WeCustomerTrajectory> trackStateList;

    }





}
