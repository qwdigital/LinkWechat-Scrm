package com.linkwechat.domain.user.vo;

import lombok.Data;

import java.util.List;

@Data
public class WeUserScreenConditVo {
    //添加员工
    public ExecuteUserCondit executeUserCondit;

    //选择部门范围与岗位
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
