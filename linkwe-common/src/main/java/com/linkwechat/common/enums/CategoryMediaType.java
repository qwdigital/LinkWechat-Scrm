package com.linkwechat.common.enums;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum CategoryMediaType {
    IMAGE(0, "图片"),
    VOICE(1, "语音"),
    VIDEO(2, "视频"),
    FILE(3, "普通文件"),
    TEXT(4, "文本"),
    POSTER(5, "海报"),
    LIVE(6, "活码"),
    CROWD(7,"人群"),
    TRIP(8,"旅程"),
    IMAGE_TEXT(9, "图文"),
    LINK(10, "链接"),
    APPLET(11, "小程序"),
    ARTICLE(12, "文章"),
    QY_TALK(13,"企业话术"),
    KF_TALK(14,"客服话术"),
    ZNBD(15,"智能表单"),
    SOPTLP(16,"SOP模板"),
    QFTLP(17,"群发模板"),

    ZLBDURL(18,"智能表单"),
    MURL(19,"外链");


    private final Integer type;

    private final String name;

    CategoryMediaType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

    public static List<SideMaterialType> sideMaterialType(){

        //素材类型：需要参考 CategoryMediaType类中的定义
        //由于枚举类定义了很多不属于素材中的类型，所以需要把用到的素材类型挑选出来
        List<SideMaterialType> result = new ArrayList<>();
        result.add(new SideMaterialType( TEXT.getType(),TEXT.getName(),1));


        result.add(new SideMaterialType( IMAGE.getType(),IMAGE.getName(),2));




        result.add(new SideMaterialType( IMAGE_TEXT.getType(),IMAGE_TEXT.getName(),3));


        result.add(new SideMaterialType( APPLET.getType(),APPLET.getName(),4));


        result.add(new SideMaterialType( ARTICLE.getType(),ARTICLE.getName(),5));


        result.add(new SideMaterialType( VIDEO.getType(),VIDEO.getName(),6));

        result.add(new SideMaterialType( FILE.getType(),FILE.getName(),7));


        result.add(new SideMaterialType( POSTER.getType(),POSTER.getName(),8));

        result.add(new SideMaterialType( ZLBDURL.getType(),ZLBDURL.getName(),9));

        result.add(new SideMaterialType( MURL.getType(),MURL.getName(),10));

        result = result.stream().sorted(Comparator.comparing(SideMaterialType::getSort)).collect(Collectors.toList());

        return result;
    }


    @Data
    public static  class  SideMaterialType{
        /**
         * 素材媒体类型
         */
        private Integer type;

        /**
         * 素材媒体类型名称
         */
        private String name;

        /**
         * 排序
         */
        private Integer sort;


        public SideMaterialType(Integer type, String name, Integer sort) {
            this.type = type;
            this.name = name;
            this.sort = sort;
        }
    }
}
