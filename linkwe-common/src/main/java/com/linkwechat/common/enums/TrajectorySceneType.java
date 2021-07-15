package com.linkwechat.common.enums;

public enum TrajectorySceneType {

    TRAJECTORY_TYPE_XXDT_BCZL(TrajectoryType.TRAJECTORY_TYPE_XXDT.getType(),"信息动态-补充资料",1),
    TRAJECTORY_TYPE_XXDT_SZBQ(TrajectoryType.TRAJECTORY_TYPE_XXDT.getType(),"信息动态-设置标签",2);


    private String name;

    private Integer type;


    private Integer key;


    TrajectorySceneType( Integer type,String name,Integer key) {
        this.name = name;
        this.type = type;
        this.key=key;
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

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
