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

    ZLBDURL(18,"智能表单");


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
        result.add(SideMaterialType.builder().type(
                TEXT.getType()
        ).name(TEXT.getName()).sort(1).build());
        result.add(SideMaterialType.builder().type(IMAGE.getType()).name(
                IMAGE.getName()
        ).sort(2).build());
        result.add(SideMaterialType.builder().type(IMAGE_TEXT.getType()).name(IMAGE_TEXT.getName()).sort(3).build());
        result.add(SideMaterialType.builder().type(APPLET.getType()).name(
                APPLET.getName()
        ).sort(4).build());
        result.add(SideMaterialType.builder().type(ARTICLE.getType()).name(ARTICLE.getName()).sort(5).build());
        result.add(SideMaterialType.builder().type(VIDEO.getType()).name(VIDEO.getName()).sort(6).build());
        result.add(SideMaterialType.builder().type(FILE.getType()).name(FILE.getName()).sort(7).build());
        result.add(SideMaterialType.builder().type(POSTER.getType()).name(POSTER.getName()).sort(8).build());
        result.add(SideMaterialType.builder().type(ZLBDURL.getType()).name(ZLBDURL.getName()).sort(9).build());
        result = result.stream().sorted(Comparator.comparing(SideMaterialType::getSort)).collect(Collectors.toList());

        return result;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class  SideMaterialType{
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
    }
}
