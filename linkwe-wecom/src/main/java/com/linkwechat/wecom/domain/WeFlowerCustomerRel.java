package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 具有外部联系人功能企业员工也客户的关系对象 we_flower_customer_rel
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFlowerCustomerRel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /** 添加了此外部联系人的企业成员userid */
    private String userId;

    /** 外部联系人名称 */
    private String userName;


    /** 该成员对此外部联系人的描述 */
    private String description;

      /** 该成员对此客户备注的企业名称 */
     private String remarkCorpName;

    /** 该成员对此客户备注的手机号码 */
    private String[] remarkMobiles;

    /** 发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid */
    private String operUserid;

    /** 该成员添加此客户的来源， */
    private String addWay;

    /** 企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加，由企业通过创建「联系我」方式指定 */
    private String state;

    /** 客户id */
    private Long customerId;


    /** 微信用户添加的标签 */
    private List<WeFlowerCustomerTagRel>  weFlowerCustomerTagRels;

}
