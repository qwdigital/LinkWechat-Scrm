package com.linkwechat.domain.wecom.query.third.auth;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;


/**
 * 企业微信推广链接
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeExtensionRegisterQuery extends WeBaseQuery {

    /**
     * 注册模板id，最长为128个字节
     */
    private String template_id;

    /**
     * 企业名称
     */
    private String corp_name;

    /**
     *管理员姓名
     */
    private String admin_name;

    /**
     *管理员手机号
     */
    private String admin_mobile;

    /**
     *用户自定义的状态值。只支持英文字母和数字，最长为128字节。若指定该参数， 接口 查询注册状态 及 注册完成回调事件 会相应返回该字段值
     */
    private String state;

    /**
     *跟进人的userid，必须是服务商所在企业的成员。若配置该值，则由该注册码创建的企业，在服务商管理后台，该企业的报备记录会自动标注跟进人员为指定成员
     */
    private String follow_user;
}