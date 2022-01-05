package com.linkwechat.common.enums;

import java.util.stream.Stream;

public enum TrajectorySceneType {

    TRAJECTORY_TITLE_TJYG(1,"添加员工","%s通过:%s 方式添加员工:%s"),
    TRAJECTORY_TITLE_SCYG(2,"删除员工","%s删除员工%s"),
    TRAJECTORY_TITLE_JRQL(3,"加入群聊","{客户名}通过{添加的方式}加入群聊{群名}"),
    TRAJECTORY_TITLE_TCQL(4,"退出群聊","{客户名}退出群聊{员工名}"),
    TRAJECTORY_TITLE_GXQYBQ(5,"更新企业标签","员工:%s,更新客户标签:%s"),
    TRAJECTORY_TITLE_GXGRBQ(6,"更新个人标签","员工:%s,更新个人标签:%s"),
    TRAJECTORY_TITLE_BJBQ(7,"编辑信息","员工:%s 编辑了:%s的详细资料"),
    TRAJECTORY_TITLE_TJGJ(8,"添加跟进","员工%s添加跟进:%s"),
    TRAJECTORY_TITLE_DZPYQ(9,"点赞朋友圈","%s点赞了员工%s的朋友圈"),
    TRAJECTORY_TITLE_PLPYQ(10,"评论朋友圈","%s评论了员工%s的朋友圈");


    private String name;

    private Integer type;

    private String msgTpl;




    TrajectorySceneType( Integer type,String name,String msgTpl) {
        this.name = name;
        this.type = type;
        this.msgTpl=msgTpl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsgTpl() {
        return msgTpl;
    }

    public void setMsgTpl(String msgTpl) {
        this.msgTpl = msgTpl;
    }

    public static  TrajectorySceneType of(Integer type){
        return Stream.of(values()).filter(s->s.getType().equals(type)).findFirst().orElseGet(null);
    }
}
