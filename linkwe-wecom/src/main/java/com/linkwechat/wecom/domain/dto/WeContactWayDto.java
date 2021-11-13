package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 联系我详情
 * @date 2021/11/13 23:50
 **/
@Data
public class WeContactWayDto extends WeResultDto {
    private WeContactWay contactWay;


    @Data
    public static class WeContactWay {
        //系我二维码链接，仅在scene为2时返回
        private String configId;
        // 联系方式类型,1-单人, 2-多人
        private Integer type;

        //场景，1-在小程序中联系，2-通过二维码联系
        private Integer scene;


        //外部客户添加时是否无需验证，默认为true
        private Boolean skipVerify;


        //使用该联系方式的用户userID列表，在type为1时为必填，且只能有一个
        private List<String> user;

        //使用该联系方式的部门id列表，只在type为2时有效
        private List<Long> party;


        //企业自定义的state参数，用于区分不同的添加渠道，在调用“获取外部联系人详情”时会返回该参数值，不超过30个字符
        private String state;

        //样式
        private Integer style = 1;
    }
}
