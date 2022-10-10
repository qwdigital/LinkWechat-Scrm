package com.linkwechat.domain.wecom.query.customer;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户详情返回对象
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCustomerRemarkQuery extends WeBaseQuery {
    /**
     * 企业成员的userid
     */
    private String userid;

    /**
     * 外部联系人userid
     */
    private String external_userid;

    /**
     * 此用户对外部联系人的备注，最多20个字符
     */
    private String remark;

    /**
     * 此用户对外部联系人的描述，最多150个字符
     */
    private String description;

    /**
     * 此用户对外部联系人备注的所属公司名称，最多20个字符
     */
    private String remark_company;

    /**
     * 此用户对外部联系人备注的手机号
     */
    private List<String> remark_mobiles;

    /**
     * 备注图片的mediaid
     */
    private String remark_pic_mediaid;
}
