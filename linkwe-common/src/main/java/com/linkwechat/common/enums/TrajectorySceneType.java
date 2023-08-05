package com.linkwechat.common.enums;

import java.util.stream.Stream;

public enum TrajectorySceneType {

    TRAJECTORY_TITLE_DZPYQ(9,"点赞朋友圈","客户:%s 点赞了员工:%s的朋友圈"),
    TRAJECTORY_TITLE_PLPYQ(10,"评论朋友圈","客户：%s 评论了员工:%s的朋友圈"),

    TRAJECTORY_TITLE_LHB(13,"领红包","%s 领取了员工 %s 发放的红包，领取金额 %s 元"),
    TRAJECTORY_TITLE_WXKF(14,"咨询客服","%s 咨询了客服 %s，并由员工 %s 接待"),
    TRAJECTORY_TITLE_ZLBD(15,"填写表单","%s 填写了员工 %s 发起的表单 %s"),

    TRAJECTORY_TITLE_TJYG(1,"添加员工","客户:%s 添加员工:%s"),
    TRAJECTORY_TITLE_SCYG(2,"删除员工","客户:%s 删除员工:%s"),

    TRAJECTORY_TITLE_SCKH(16,"删除客户","员工:%s 删除客户:%s"),

    TRAJECTORY_TITLE_GXQYBQ(5,"更新企业标签","员工:%s,更新客户:%s,企业标签为:%s"),
    TRAJECTORY_TITLE_GXGRBQ(6,"更新个人标签","员工:%s,更新客户:%s,个人标签:%s"),
    TRAJECTORY_TITLE_BJBQ(7,"编辑信息","员工:%s 更新客户详细资料:%s"),
    TRAJECTORY_TITLE_QXKHQYBQ(16,"取消客户企业标签","员工:%s,取消客户:%s 的企业标签"),
    TRAJECTORY_TITLE_QXKHGRBQ(17,"取消客户个人标签","员工:%s,取消客户:%s 的个人标签"),

    TRAJECTORY_TITLE_TJGJ(8,"添加跟进","员工:%s 添加客户:%s 跟进动态为:%s"),


    TRAJECTORY_TITLE_JRQL(3,"加入群聊","客户:%s 进入群聊:%s"),
    TRAJECTORY_TITLE_TCQL(4,"退出群聊","客户:%s 退出群聊:%s"),
    TRAJECTORY_TITLE_YCQL(16,"移出群聊","客户:%s 移出群聊:%s"),
    TRAJECTORY_TITLE_YGCJQL(11,"员工创建了群聊","员工:%s 创建群聊:%s"),
    TRAJECTORY_TITLE_YGJSQL(12,"员工解散了群聊","员工:%s 解散群聊:%s"),
    TRAJECTORY_TITLE_LOOK_MATERIAL(13, "查看素材", "%s 查看了 %s 发送的素材 %s"),

    TRAJECTORY_TITLE_BUY_GOODS(14, "购买商品", "%s 购买了 %s 发送的商品 %s"),

    ;
    private String name;

    private Integer type;

    private String msgTpl;


    TrajectorySceneType(Integer type, String name, String msgTpl) {
        this.name = name;
        this.type = type;
        this.msgTpl = msgTpl;
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

    public static TrajectorySceneType of(Integer type) {
        return Stream.of(values()).filter(s -> s.getType().equals(type)).findFirst().orElseGet(null);
    }
}
