package com.linkwechat.domain.customer.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WeCustomersQuery implements Serializable {

    //0-未知 1-男性 2-女性
    private Integer gender;

    //查询多个性别，用逗号隔开
    private String genders;

    //跟踪状态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;

    //不包含的跟踪状态，多个使用逗号隔开
    private String noContainTrackStates;

    //添加方式
    private Integer addMethod;

    //客户类型 1:微信用户，2:企业用户
    private Integer customerType;

    //查询多个客户类型用逗号隔开
    private String customerTypes;

    //客户id
    private String externalUserid;

    //多个企业微信客户id查询
    private String externalUserids;

    //跟进人id
    private String firstUserId;

    //0正常；1:删除;
    private Integer delFlag;

    //查询条件客户姓名
    private String name;

    //查询标签id(企业标签id)
    private String tagIds;

    //查询条件
    private String userIds;

    //部门查询条件
    private String deptIds;

    //查询开始时间
    private String beginTime;

    //查询结束时间
    private String endTime;

    //不包含的标签id
    private String excludeTagIds;

    //个人数据:false 全部数据(相对于角色定义的数据权限):true
    private Boolean dataScope = false;

    //渠道值
    private List<String> stateList;

    //是否按照无标签查询 true:是 false:不是
    private boolean noTagCheck = false;

    //是否去重客户 true:去重  false:不去重
    private boolean noRepeat = false;

    private boolean isFilterLossCustomer = false;

    /**
     * 手机号码
     */
    private String phone;


    /**
     * 客户id，多个使用逗号隔开
     */
    private String customerIds;


    /**
     * 0:加入黑名单;1:不加入黑名单;
     */
    private Integer isJoinBlacklist;


    /**
     * 1:包含全部选中标签 2:包含其中一个标签 3:不包含选中标签
     */
    private Integer isContain;


    /**
     * 添加人名称
     */
    private String userNames;

    /**
     * 标签名，多个逗号隔开
     */
    private String tagNames;


}
