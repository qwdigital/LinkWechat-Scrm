package com.linkwechat.wecom.domain.dto.customer;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: 获取客户详情
 * @author: HaoN
 * @create: 2020-10-19 22:33
 **/
@Data
public class ExternalUserDetail extends WeResultDto {

    /** 客户详情 */
    private ExternalContact external_contact;


    /** 客户联系人 */
    private List<FollowUser> follow_user;

    private FollowInfo follow_info;

    private String external_userid;


    @Data
    public static class ExternalContact{
        /** 外部联系人userId */
        private String externalUserid;
        /** 外部联系人名称 */
        private String name;
        /** 外部联系人职位 */
        private String position;
        /** 外部联系人头像 */
        private String avatar;
        /** 外部联系人所在企业简称 */
        private String corpName;
        /** 外部联系人所在企业全称 */
        private String corpFullName;
        /** 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户 */
        private Integer type;
        /** 外部联系人性别 0-未知 1-男性 2-女性 */
        private Integer gender;
        /** 外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。 */
        private String unionid;
    }


    @Data
    public class FollowUser{
        /**添加了此外部联系人的企业成员userid*/
        private String userid;
        /**该成员对此外部联系人的备注*/
        private String remark;
        /**该成员对此外部联系人的描述*/
        private String description;
        /**该成员添加此外部联系人的时间*/
        private long createtime;
        /**该成员对此客户备注的企业名称*/
        private String remarkCorpName;
        /**该成员对此客户备注的手机号码*/
        private String[] remarkMobiles;
        /**该成员添加此客户的来源*/
        private Integer addWay;
        /**发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid*/
        private String operUserid;
        /** 企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加，由企业通过创建「联系我」方式指定 */
        private String state;
        /**标签**/
        private List<ExternalUserTag> tags;
    }

    @Data
    public class FollowInfo{
        /**添加人id*/
        private String userid;
        /**该成员对此外部联系人的备注*/
        private String remark;
        /**该成员对此外部联系人的描述*/
        private String description;
        /**该成员添加此外部联系人的时间*/
        private long createtime;
        /**该成员对此客户备注的企业名称*/
        private String remark_company;
        /**该成员对此客户备注的手机号码*/
        private String[] remark_mobiles;
        /**该成员添加此客户的来源*/
        private Integer add_way;
        /**发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid*/
        private String oper_userid;
        /**标签**/
        private String[] tag_id;
        /** 企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加，由企业通过创建「联系我」方式指定 */
        private String state;
    }

}
