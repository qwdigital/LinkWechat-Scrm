package com.linkwechat.domain.wecom.query.user;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 创建成员
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddUserQuery extends WeBaseQuery {
    /**
     * 成员UserID
     */
    private String userid;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 成员别名
     */
    private String alias;

    /**
     * 手机号码。企业内必须唯一，mobile/email二者不能同时为空
     */
    private String mobile;

    /**
     * 成员所属部门id列表，不超过100个
     */
    private List<Integer> department;

    /**
     * 部门内的排序值，默认为0
     */
    private List<Integer> order;


    /**
     * 职务信息。长度为0~128个字符
     */
    private String position;

    /**
     * 性别。1表示男性，2表示女性
     */
    private Integer gender;

    /**
     * 邮箱。长度6~64个字节，且为有效的email格式。企业内必须唯一，mobile/email二者不能同时为空
     */
    private String email;

    /**
     * 座机。32字节以内，由纯数字、“-”、“+”或“,”组成。
     */
    private String telephone;

    /**
     * 个数必须和参数department的个数一致，表示在所在的部门内是否为部门负责人。
     * 1表示为部门负责人，0表示非部门负责人
     */
    private List<Integer> is_leader_in_dept;

    /**
     * 直属上级UserID，设置范围为企业内成员，可以设置最多5个上级
     */
    private List<String> direct_leader;

    /**
     * 成员头像的mediaid，通过素材管理接口上传图片获得的mediaid
     */
    private String avatar_mediaid;

    /**
     * 启用/禁用成员。1表示启用成员，0表示禁用成员
     */
    private Boolean enable;

    /**
     * 地址。长度最大128个字符
     */
    private String address;

    /**
     * 主部门
     */
    private Integer main_department;

    /**
     * 是否邀请该成员使用企业微信（将通过微信服务通知或短信或邮件下发邀请，
     * 每天自动下发一次，最多持续3个工作日），默认值为true。
     */
    private Boolean to_invite;

    /**
     * 成员对外属性
     */
    private String external_position;

}
