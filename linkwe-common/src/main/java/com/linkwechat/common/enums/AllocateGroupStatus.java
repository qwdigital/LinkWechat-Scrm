package com.linkwechat.common.enums;

/**
 * 群分配状态
 */
public enum AllocateGroupStatus {

    ALLOCATE_GROUP_STATUS_BJTCG(0,"被接替成功"),
    ALLOCATE_GROUP_STATUS_DJT(1,"待接替"),
    ALLOCATE_GROUP_STATUS_JTSB(2,"接替失败"),
    ALLOCATE_GROUP_STATUS_ZCZT(3,"正常状态");

    private Integer key;
    private String val;

    AllocateGroupStatus(Integer key,String val){
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
}
