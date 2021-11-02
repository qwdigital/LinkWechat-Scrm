package com.linkwechat.wecom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WeCustomerList {
    //客户唯一id
    private String externalUserid;
    //首位添加人id
    private String firstUserId;
    //客户名称
    private String customerName;
    //首位添加人姓名
    private String userName;
    //跟踪状态
    private Integer trackState;
    //首位添加人时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstAddTime;
    //标签名称，使用逗号隔开
    private String tagNames;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


//    private List<WeFlowerCustomerRel> weFlowerCustomerRels;

    //查询条件客户姓名
    private String name;
    //查询条件
    private String userIds;
    //查询开始时间
    private String beginTime;
    //查询结束时间
    private String endTime;
    //查询标签id
    private String tagIds;

    //0正常；1:删除;
    private Integer delFlag=new Integer(0);
}
