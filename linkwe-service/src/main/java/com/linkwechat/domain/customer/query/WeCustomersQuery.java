package com.linkwechat.domain.customer.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WeCustomersQuery {

    //0-未知 1-男性 2-女性
    private Integer gender;

    //跟踪状态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;

    //添加方式
    private Integer addMethod;

    //客户类型 1:微信用户，2:企业用户
    private Integer customerType;

    //客户id
    private String externalUserid;

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
    private Boolean dataScope=false;

    //渠道值
    private List<String> stateList;
}
