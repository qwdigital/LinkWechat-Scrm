package com.linkwechat.domain.leads.leads.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 线索基础请求类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/04 16:57
 */
@Data
public class WeLeadsBaseRequest {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;


    /**
     * 线索状态(0待分配，1跟进中，2已上门，3已退回)
     */
    private Integer leadsStatus;

    /**
     * 所属公海
     */
    private Long seaId;

    //=======================导出相关start===========================
    @ApiModelProperty(value = "为导出时用，线索是否全选 (全选true，不全选false) 默认不全选")
    protected Boolean all = Boolean.FALSE;

    @ApiModelProperty(value = "为导出时用，线索Id集合,all为false时，必填")
    protected List<Long> leadsIds;

    @ApiModelProperty(value = "为导出时用，全选时，未被选中的导入记录id集台，当为导出时不为空")
    protected List<Long> unLeadsIds;

    @ApiModelProperty(value = "专栏名称，当为导出时不为空")
    private String columnTitle;
    //========================导出相关end==============================

    //=======================数据范围限制参数start=======================
    /**
     * 数据权限范围
     */
    private String dataScope;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 用户部门Id
     */
    private List<Long> deptIds;
    //=======================数据范围限制参数end=======================

}
