package com.linkwechat.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 客户添加方式
 */
public enum CustomerAddWay {
    ADD_WAY_WZLY(0,"未知来源"),
    ADD_WAY_SMEWM(1,"扫描二维码"),
    ADD_WAY_SSSJH(2,"搜索手机号"),
    ADD_WAY_MPFX(3,"名片分享"),
    ADD_WAY_QL(4,"群聊"),
    ADD_WAY_SJTXL(5,"手机通讯录"),
    ADD_WAY_WXLXR(6,"微信联系人"),
    ADD_WAY_LZWXTJHY(7,"来自微信的添加好友申请"),
    ADD_WAY_KFRY(8,"安装第三方应用时自动添加的客服人员"),
    ADD_WAY_SSYX(9,"搜索邮箱"),
    ADD_WAY_SPHZYTJ(10,"视频号主页添加"),
    ADD_WAY_RCCYRY(11,"通过日程参与人添加"),
    ADD_WAY_HYCYRY(12,"通过会议参与人添加"),
    ADD_WAY_WXTOQW_USER(13,"添加微信好友对应的企业微信"),
    ADD_WAY_ZHYJKF(14,"通过智慧硬件专属客服添加"),
    ADD_WAY_LBCYGX(201,"内部成员共享"),
    ADD_WAY_GLYFP(202,"管理员/负责人分配"),
    ADD_WAY_HKZS(16,"通过获客链接添加");

    private Integer key;

    private String val;
    CustomerAddWay(Integer key, String val){
        this.key=key;
        this.val=val;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    public static  CustomerAddWay of(Integer type){
        return Stream.of(values()).filter(s->s.getKey().equals(type)).findFirst().orElseGet(null);
    }

    public static List<Map<String, Object>> getType() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustomerAddWay customerAddWay : values()) {
            Map<String, Object> energyTypeMap = new HashMap<>(16);
            energyTypeMap.put("code", customerAddWay.getKey());
            energyTypeMap.put("value", customerAddWay.getVal());
            list.add(energyTypeMap);
        }
        return list;
    }
}
