package com.linkwechat.wecom.domain;

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

    private Date birthday;

    private String position;

    private String email;

    private String corpName;

    private String otherDescr;


    private List<CompanyOrPersonTag> companyTags;


    private List<CompanyOrPersonTag> personTags;




    @Data
    @Builder
    public static class TrackUser {

        //添加员工名称
        private String userName;

        //添加方式
        private Integer addMethod;

        //添加时间
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
        private Date joinTime;



    }





    //企业或个人标签
    @Data
    @Builder
    public static class  CompanyOrPersonTag{

        private String userName;
        private String tagsNames;
        private String tagIds;


    }

    @Data
    public  static class  TrackStates{
        //跟进人
        private String userName;

        //跟进状态列表
        private List<WeCustomerTrajectory> trackStateList;

    }





}
