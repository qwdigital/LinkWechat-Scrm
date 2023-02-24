package com.linkwechat.common.enums.fieldtempl;

import com.linkwechat.common.enums.AllocateCustomerStatus;
import lombok.Data;

import java.util.stream.Stream;

/**
 * 客户画像表单默认字段
 */
public enum CustomerPortraitFieldTempl {



    LABLE_CUSTOMER_NAME("姓名","customerFullName",1,null,11),
    LABLE_CUSTOMER_PHONE("手机号","remarkMobiles",1,null,10),
    LABLE_CUSTOMER_AGE("年龄","age",1,null,9),
    LABLE_CUSTOMER_BIRTHDAY("生日","birthday",2,null,8),

    LABLE_CUSTOMER_EMAIL("邮箱","email",1,null,7),


    //针对当前省市区特殊处理
    LABLE_CUSTOMER_AREA("所在省市区（县）","area",3,1,6),

    LABLE_CUSTOMER_ADDRESS("详细地址","address",1,null,5),

    LABLE_CUSTOMER_QQ("qq","qq",1,null,4),

    LABLE_CUSTOMER_POSITION("职业","position",1,null,3),


    LABLE_CUSTOMER_COMPANY("公司","remarkCorpName",1,null,2),

    LABLE_CUSTOMER_OTHERDESC("其他描述","otherDescr",1,null,1);



    //标签名
    private String labelName;


    //标签值
    private String labelVal;


    //标签类型(信息项属性(1:文本;2:时间;3:勾选项))
    private Integer labelType;


    //标签勾选属性（信息属性子类型(目前type字段为3的时候 1:单选;2:多选)）
    private Integer labelProper;


    //排序
    private Integer sort;

    CustomerPortraitFieldTempl(String labelName,String labelVal,Integer labelType,Integer labelProper,Integer sort){
        this.labelName=labelName;
        this.labelVal=labelVal;
        this.labelType=labelType;
        this.labelProper=labelProper;
        this.sort=sort;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelVal() {
        return labelVal;
    }

    public void setLabelVal(String labelVal) {
        this.labelVal = labelVal;
    }

    public Integer getLabelType() {
        return labelType;
    }

    public void setLabelType(Integer labelType) {
        this.labelType = labelType;
    }

    public Integer getLabelProper() {
        return labelProper;
    }

    public void setLabelProper(Integer labelProper) {
        this.labelProper = labelProper;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


}
