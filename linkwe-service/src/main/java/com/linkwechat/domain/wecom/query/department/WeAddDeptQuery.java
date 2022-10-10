package com.linkwechat.domain.wecom.query.department;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 创建部门入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddDeptQuery extends WeBaseQuery {

    /**
     * 部门名称。同一个层级的部门名称不能重复。长度限制为1~32个字符，字符不能包括\:*?”<>｜
     */
    private String name;

    /**
     * 英文名称。同一个层级的部门名称不能重复。需要在管理后台开启多语言支持才能生效。长度限制为1~32个字符，字符不能包括\:*?”<>｜
     */
    private String name_en;

    /**
     * 父部门id，32位整型
     */
    private Integer parentid;

    /**
     * 在父部门中的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)
     */
    private Integer order;

    /**
     * 部门id，32位整型，指定时必须大于1。若不填该参数，将自动生成id
     */
    private Integer id;
}
