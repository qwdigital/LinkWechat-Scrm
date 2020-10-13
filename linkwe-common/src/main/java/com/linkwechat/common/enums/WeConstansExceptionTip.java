package com.linkwechat.common.enums;

public enum WeConstansExceptionTip {
    WE_EXCEPTION_60111(60111,"UserID参数为空,或者不存在通讯录中");


    private Integer code;

    private String tip;

    private WeConstansExceptionTip(Integer code, String tip){
        this.code = code;
        this.tip = tip;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public static String getExceptionMsg(Integer code){
        WeConstansExceptionTip[] values = WeConstansExceptionTip.values();
        for(WeConstansExceptionTip value:values){
            if(value.getCode().equals(code)){
                return value.getTip();
            }
        }

        return null;
    }
}
