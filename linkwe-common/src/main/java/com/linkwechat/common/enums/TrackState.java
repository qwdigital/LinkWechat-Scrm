package com.linkwechat.common.enums;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TrackState {
    //跟踪状态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    STATE_DGJ(1,"待跟进","新客户,等待商机跟进",1,1),
    STATE_GJZ(2,"跟进中","客户产生商机,员工跟进中",2,2),
    STATE_JCJ(3,"已赢单","客户当前商机已成交",3,3),
    STATE_WYX(4,"已输单","短时间内客户无法成交",3,4),
    STATE_YLS(5,"已流失","客户主动将跟进员工删除,系统自动处理",3,5);
//    STATE_OTHER(6,"其他");

    private String name;

    private Integer type;

    private String desc;

    private Integer strackStage;


    private Integer sort;

    TrackState(Integer type,String name,String desc,Integer strackStage, Integer sort){
        this.name=name;
        this.type=type;
        this.desc=desc;
        this.strackStage=strackStage;
        this.sort=sort;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStrackStage() {
        return strackStage;
    }

    public void setStrackStage(Integer strackStage) {
        this.strackStage = strackStage;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public static  TrackState of(Integer type){
        List<TrackState> trackStates = Stream.of(values()).filter(s -> s.getType()
                .equals(type)).collect(Collectors.toList());

        if(CollectionUtil.isEmpty(trackStates)){
            return null;
        }


        return trackStates.stream().findFirst().orElseGet(null);
    }
}
