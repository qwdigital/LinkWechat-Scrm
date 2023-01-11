package com.linkwechat.domain.sop.vo;

import lombok.Data;

import java.util.List;

/**
 * sop执行结束项
 */
@Data
public class WeSopExecuteEndVo {


    private ExecuteTag executeTag;

    //客户加入客群
    private JoinCustomerGroup joinCustomerGroup;

    //转入其他sop
    private ToChangeIntoOtherSop toChangeIntoOtherSop;



    //执行标签
    @Data
    public static class ExecuteTag{

        //是否选择,默认不选择
        private boolean change=false;
        //标签id
        private List<String> tagIds;

    }

    /**
     * 加入客群
     */
    @Data
    public static class JoinCustomerGroup{
        //是否选择,默认不选择
        private boolean change=false;
        //入群提示
        private String joinGroupTip;
        //群二维码主键id
        private String groupCodeId;
        //群名称
        private String groupName;
        //群二维码url
        private String groupUrl;
    }


    /**
     * 转入其他sop
     */
    @Data
    public static class ToChangeIntoOtherSop{
        //是否选择,默认不选择
        private boolean change=false;
        //转接引导语
        private String toChangeTip;
        //转接提示
        private String toChangeIntoSopId;
    }




}
