package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum CategoryModuleTypeEnum {
    ERROR(-1,"错误模块","错误子模块"),
    MATERIAL(1,"内容中心","素材中心"),
    TALK(2,"内容中心","话术中心"),
    TEMPLATE(3,"内容中心","模板中心")
    ;

    private final Integer value;
    private final String ModuleName;
    private final String ModuleNameSon;
    CategoryModuleTypeEnum(Integer value,String ModuleName,String ModuleNameSon){
        this.value = value;
        this.ModuleName = ModuleName;
        this.ModuleNameSon = ModuleNameSon;
    }
    CategoryModuleTypeEnum(Integer value,String ModuleNameSon){
        this.value = value;
        this.ModuleName = "默认模块";
        this.ModuleNameSon = ModuleNameSon;
    }

    CategoryModuleTypeEnum(Integer value){
        this.value = value;
        this.ModuleName = "默认模块";
        this.ModuleNameSon = "默认子模块";
    }

    public static CategoryModuleTypeEnum getCategoryModuleTypeEnumByValue(Integer code){
        for(CategoryModuleTypeEnum categoryModuleTypeEnum : CategoryModuleTypeEnum.values()){
            if(Objects.equals(code, categoryModuleTypeEnum.getValue())){
                return categoryModuleTypeEnum;
            }
        }

        return ERROR;
    }

}
