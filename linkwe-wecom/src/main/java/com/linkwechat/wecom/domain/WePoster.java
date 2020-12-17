package com.linkwechat.wecom.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 海报
 * @author ws
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_poster")
public class WePoster extends BaseEntity {

    @TableId
    private Long id;

    /**
     * 海报标题
     */
    @TableField
    private String title;

    /**
     * 背景图片
     */
    @TableField
    private String backgroundImgPath;

    /**
     * 示例图片
     */
    @TableField
    private String sampleImgPath;

    /**
     * 海报类型 1 通用海报
     */
    @TableField
    private Long type;

    @TableField
    private Integer delFlag;

    @TableField
    private Integer width;

    @TableField
    private Integer height;

    /**
     * 海报组件数组
     */
    @TableField(exist = false)
    private List<WePosterSubassembly> posterSubassemblyList;

    public static void main(String[] args) {
        WePoster wePoster = new WePoster();
        wePoster.setDelFlag(1);
        wePoster.setTitle("你好世界");
        wePoster.setHeight(1000);
        wePoster.setWidth(1000);
        wePoster.setType(1L);
        wePoster.setBackgroundImgPath("1");
        wePoster.setPosterSubassemblyList(new ArrayList<>());
        for(int i = 0; i < 1; i++){
            WePosterSubassembly subassembly = new WePosterSubassembly();
            subassembly.setFontColor("#544875");
            subassembly.setFontId(1L);
            subassembly.setHeight(100);
            subassembly.setWidth(100);
            subassembly.setDelFlag(1);
            subassembly.setLeft(1);
            subassembly.setTop(1);
            subassembly.setFontTextAlign(1);
            wePoster.getPosterSubassemblyList().add(subassembly);
        }
        System.out.println(JSON.toJSONString(wePoster));
    }
}
