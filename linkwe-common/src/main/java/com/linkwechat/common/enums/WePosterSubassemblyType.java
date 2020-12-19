package com.linkwechat.common.enums;

/**
 * 海报组件类型
 * @author ws
 */
public enum WePosterSubassemblyType {
    /**
     * 自定义文本
     */
    plainText(0,"自定义文本"),
    /**
     * 图片
     */
    picture(1,"图片"),
    /**
     * 二维码
     */
    QRCode(2,"二维码");

    private Integer code;
    private String label;

    private WePosterSubassemblyType(Integer code,String label){
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
