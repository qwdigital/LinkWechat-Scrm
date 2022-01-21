package com.linkwechat.common.enums;

public enum TrackState {
    //跟踪状态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    STATE_DGJ(1,"待跟进"),
    STATE_GJZ(2,"跟进中"),
    STATE_JCJ(3,"已成交"),
    STATE_WYX(4,"无意向"),
    STATE_YLS(5,"已流失");

    private String name;

    private Integer type;

    TrackState(Integer type,String name){
        this.name=name;
        this.type=type;
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
}
