package com.linkwechat.domain.sop.vo;

import lombok.Data;

import java.util.List;

/**
 * 执行成员相关条件
 */
@Data
public class WeSopExecuteUserConditVo {

    //执行员工
    public ExecuteUserCondit executeUserCondit;

    //执行部门与岗位
    public ExecuteDeptCondit executeDeptCondit;



    @Data
    public static class ExecuteUserCondit{

        //是否选择,默认不选择
        private boolean change=false;
        //员工id
        private List<String> weUserIds;

    }

    @Data
    public static class ExecuteDeptCondit{
        //是否选择,默认不选择
        private boolean change=false;

        //部门范围id
        private List<String> deptIds;

        //岗位名称集合
        private List<String> posts;


    }




}
