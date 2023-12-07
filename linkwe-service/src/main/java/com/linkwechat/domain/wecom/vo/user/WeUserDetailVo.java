package com.linkwechat.domain.wecom.vo.user;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 成员详情
 * @date 2021/12/7 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUserDetailVo extends WeResultVo {
    /**
     * 成员UserID
     */
    private String userId;

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
    private List<Long> department;

    /**
     * 部门内的排序值，默认为0
     */
    private List<Long> order;


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
     * 企业邮箱
     */
    private String bizMail;

    /**
     * 座机。32字节以内，由纯数字、“-”、“+”或“,”组成。
     */
    private String telephone;

    /**
     * 个数必须和参数department的个数一致，表示在所在的部门内是否为部门负责人。
     * 1表示为部门负责人，0表示非部门负责人
     */
    private List<Integer> isLeaderInDept;

    /**
     * 直属上级UserID，设置范围为企业内成员，可以设置最多5个上级
     */
    private List<String> directLeader;

    /**
     * 成员头像的mediaid，通过素材管理接口上传图片获得的mediaid
     */
    private String avatarMediaid;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 头像缩略图url
     */
    private String thumbAvatar;

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
    private Long mainDepartment;

    /**
     * 是否邀请该成员使用企业微信（将通过微信服务通知或短信或邮件下发邀请，
     * 每天自动下发一次，最多持续3个工作日），默认值为true。
     */
    private Boolean toInvite;

    /**
     * 成员扩展属性
     */
    private String extAttr;

    /**
     * 成员对外职务
     */
    private String externalPosition;


    /**
     * 成员对外属性
     */
    private String externalProfile;

    /**
     * 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
     */
    private Integer status;

    /**
     * 员工个人二维码，扫描可添加为外部联系人(注意返回的是一个url，可在浏览器上打开该url以展示二维码)
     */
    private String qrCode;

    /**
     * 全局唯一。对于同一个服务商，不同应用获取到企业内同一个成员的open_userid是相同的
     */
    private String openUserId;

    /**
     * 是否离职1:是；0:否
     */
    private Integer isUserLeave;


    public Integer getGender() {
        if (gender != null) {
            switch (gender) {
                case 1:
                    return 0;
                case 2:
                    return 1;
                default:
                    return 2;
            }
        }
        return 2;
    }
}
