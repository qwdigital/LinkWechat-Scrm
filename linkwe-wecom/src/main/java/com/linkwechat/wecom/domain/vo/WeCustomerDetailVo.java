package com.linkwechat.wecom.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 客户详情
 */
@Data
public class WeCustomerDetailVo {
    //客户昵称
    private String name;
    //客户头像
    private String avatar;
    //当前跟踪状态（1:跟踪中;2:待跟进;3:已拒绝;4:已成交;）
    private Integer trackState;
    //手机号
    private String phone;
    //生日
    private Date birthday;
    //邮箱
    private String email;

    //地址
    private String address;

    //qq
    private String qq;

    //公司
    private String corpName;

    //职业
    private String position;

    //其他描述
    private String otherDescr;

    //外部联系人性别 0-未知 1-男性 2-女
    private Integer gender;

    //群标签，用逗号隔开
    private String groupTags;

    //添加的员工，名称用逗号隔开
    private String addWeuserNames;

    //添加的群,群名用逗号隔开
    private String addWeGroupNames;

    //客户所属员工信息
    private List<CusertomerBelongUserInfo> belongUserInfos;
}
