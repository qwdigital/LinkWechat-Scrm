package com.linkwechat.wecom.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 客户实体
 * @author: HaoN
 * @create: 2020-09-15 17:43
 **/
@Data
public class WeCustomerDto extends WeResultDto{


    /** 客户id集合 */
    private String[] external_userid;

    /** 客户详情 */
    private ExternalContact external_contact;

//    /** 客户联系人 */
//    private List<WeFollowUserDto> follow_user;


    @Data
    public class ExternalContact{
        /** 外部联系人userId */
        private String external_userid;
        /** 外部联系人名称 */
        private String name;
        /** 外部联系人职位 */
        private String position;
        /** 外部联系人头像 */
        private String avatar;
        /** 外部联系人所在企业简称 */
        private String corp_name;
        /** 外部联系人所在企业全称 */
        private String corp_full_name;
        /** 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户 */
        private Integer type;
        /** 外部联系人性别 0-未知 1-男性 2-女性 */
        private Integer gender;
        /** 外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。 */
        private String unionid;

    }

    @Data
    public class WeCustomerRemark {
        private String userid;
        private String external_userid;
        private String remark;
        private String description;
        private String remark_company;
        private String[] remark_mobiles;
        private String remark_pic_mediaid;
    }


    /**
     * 批量获取客户参数
     */
    @Data
    @Builder
    public static class  BatchCustomerParam{
        private String[] userid_list;
        private String cursor;
        private Integer limit;
    }


//    public WeCustomer transformWeCustomer(){
//        WeCustomer weCustomer=new WeCustomer();
//
//        if(null != external_contact){
//            BeanUtils.copyPropertiesignoreOther(external_contact,weCustomer);
//        }
//
//        if(CollectionUtil.isNotEmpty(follow_user)){
//            List<WeFlowerCustomerRel> weFlowerCustomerRels=new ArrayList<>();
//            List<WeTagDto> weTagDtos=new ArrayList<>();
//            follow_user.stream().forEach(k->{
//                WeFlowerCustomerRel weFlowerCustomerRel=new WeFlowerCustomerRel();
//                BeanUtils.copyPropertiesignoreOther(k,weFlowerCustomerRel);
//                weFlowerCustomerRels.add(weFlowerCustomerRel);
//                if(CollectionUtil.isNotEmpty(k.getTags())){
//                    k.getTags().stream().forEach(v->v.setFlower_customer_rel_id(k.getId()));
//                    weTagDtos.addAll(k.getTags());
//                }
//            });
//
//        }
//
//        return weCustomer;
//    }


}
