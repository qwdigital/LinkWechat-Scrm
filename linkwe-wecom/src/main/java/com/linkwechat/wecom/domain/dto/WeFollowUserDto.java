package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @description: 企业服务人员数据传输实体（配置客户联系功能的成员）
 * @author: HaoN
 * @create: 2020-09-15 14:08
 **/
@Data
public class WeFollowUserDto extends WeResultDto{
    /** 拥有通讯录功能的企业成员id集合 */
    private String[] follow_user;

    /** 拥有通讯录功能的企业成员id */
    private String userid;

    /** 外部联系人的备注 */
    private String remark;

    /** 外部联系人描述 */
    private String description;

    /** 外部联系人创建时间 */
    private Long createtime;

    /** 添加外部联系人所打的标签 */
    private List<WeTagDto> tags;

    /** 客户备注的企业名称 */
    private String remark_corp_name;

    /** 客户备注的手机号码 */
    private String[] remark_mobiles;

    /** 该成员添加此客户的来源 */
    private Integer add_way;

    /** 发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid */
    private String oper_userid;

    /** 企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加 */
    private Integer state;
}
