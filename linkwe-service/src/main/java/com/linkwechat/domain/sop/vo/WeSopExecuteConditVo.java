package com.linkwechat.domain.sop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class WeSopExecuteConditVo implements Serializable {
    //客户属性
    private ExecuteCustomerCondit executeCustomerCondit;

    //已有人数
    private CrowdAttribute crowdAttribute;

    private WeSopExecuteGroupTimeCondit weSopExecuteGroupTimeCondit;


    private WeSopExecuteGroupTagIdsCondit weSopExecuteGroupTagIdsCondit;


    private WeSopExecuteGroupMemberLimitCondit weSopExecuteGroupMemberLimitCondit;


    //客户属性
    @Data
    public static class ExecuteCustomerCondit{
        //是否选择,默认不选择
        private boolean change=false;

        private List<ExecuteCustomerQUECondits> executeCustomerQUECondits;

    }

    @Data
    public static class ExecuteCustomerQUECondits{

        //且或 1-且 2-或
        private Integer andOr;

        private List<ExecuteCustomerQUECondit>  executeCustomerQUECondits;
    }

    @Data
    public static class ExecuteCustomerQUECondit{

        //条件名code
        private String code;

        //关系
        private String relation;

        //条件值
        private String value;

        //2-企业标签 3-客户属性 6-所属群聊
        private Integer swipType;

        //开始时间
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private String startTime;
        //结束时间
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private String endTime;

    }

    //人群属性
    @Data
    public static class  CrowdAttribute{
        //是否选择,默认不选择
        private boolean change=false;
        //人群包id
        private List<String> crowdIds;
    }


    //时间条件
    @Data
    public static class WeSopExecuteGroupTimeCondit{
        //是否选择,默认不选择
        private boolean change=false;
        private Date beginTime;
        private Date endTime;
    }

    //群标签条件
    @Data
    public static class WeSopExecuteGroupTagIdsCondit{
        //是否选择,默认不选择
        private boolean change=false;
        //标签id集合
        private List<String> tagIds;

    }

    //群人数限制条件
    @Data
    public static class WeSopExecuteGroupMemberLimitCondit{
        //是否选择,默认不选择
        private boolean change=false;
        //群人数上限
        private Integer groupMemberUp;
        //群人数下限
        private Integer groupMemberDown;
    }

}
