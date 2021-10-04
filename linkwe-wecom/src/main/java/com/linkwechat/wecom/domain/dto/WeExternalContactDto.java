package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 对外联系相关字段
 * @author: HaoN
 * @create: 2020-10-13 10:53
 **/
@Data
public class WeExternalContactDto extends WeResultDto {

    //系我二维码链接，仅在scene为2时返回
    private String qr_code;

    //新增联系方式的配置id
    private String config_id;


    @Data
    public static class WeContactWay {
        //系我二维码链接，仅在scene为2时返回
        private String config_id;
        // 联系方式类型,1-单人, 2-多人
        private Integer type;

        //场景，1-在小程序中联系，2-通过二维码联系
        private Integer scene;


        //外部客户添加时是否无需验证，默认为true
        private Boolean skip_verify;


        //使用该联系方式的用户userID列表，在type为1时为必填，且只能有一个
        private String[] user;

        //使用该联系方式的部门id列表，只在type为2时有效
        private Long[] party;


        //企业自定义的state参数，用于区分不同的添加渠道，在调用“获取外部联系人详情”时会返回该参数值，不超过30个字符
        private String state;

        //样式
        private Integer style = 1;




        public WeContactWay() {
        }

        public WeContactWay(Integer type, Integer scene, Boolean skip_verify) {
            this.type = type;
            this.scene = scene;
            this.skip_verify = skip_verify;
        }
    }


}
