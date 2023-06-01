package com.linkwechat.common.enums;

/**
 * @author danmo
 * @date 2023年01月17日 11:11
 */

public enum CorpUserEnum {
    //离职状态员工，数据分配状态:0:未分配;1:已分配
    NO_IS_ALLOCATE(0, "未分配"),
    YES_IS_ALLOCATE(1, "已分配");

    private int key;
    private String value;

    /**
     * 构造方法
     *
     * @param key
     * @param value
     */
    CorpUserEnum(int key, String value) {
        this.setKey(key);
        this.setValue(value);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
