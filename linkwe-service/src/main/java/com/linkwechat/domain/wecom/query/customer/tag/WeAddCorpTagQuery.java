package com.linkwechat.domain.wecom.query.customer.tag;

import com.linkwechat.domain.wecom.entity.customer.tag.WeCorpTagEntity;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author danmo
 * @Description 添加企业客户标签入参
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeAddCorpTagQuery extends WeBaseQuery {
    /**
     * 标签组id
     */
    private String group_id;

    /**
     * 标签组名称，最长为30个字符
     */
    private String group_name;

    /**
     * 标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)
     */
    private Integer order;

    /**
     * 添加的标签
     */
    private List<WeCorpTagEntity> tag;

//    public WeAddCorpTagQuery(){
//
//    }
//
//    public WeAddCorpTagQuery(String groupName,List<WeCorpTagEntity> tag){
//        this.group_name=groupName;
//        this.tag=tag;
//    }
//
//    public WeAddCorpTagQuery(WeBaseQuery weBaseQuery,String groupName,List<WeCorpTagEntity> tag){
//        this.corpid=weBaseQuery.getCorpid();
//        this.suite_id=weBaseQuery.getSuite_id();
//        this.agentid=weBaseQuery.getAgentid();
//        this.group_name=groupName;
//        this.tag=tag;
//    }
}
