package com.linkwechat.domain.wecom.entity.department;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 部门‘
 * @date 2021/12/3 15:23
 **/
@Data
public class WeDeptEntity {
    /**
     * 部门名称。同一个层级的部门名称不能重复。长度限制为1~32个字符，字符不能包括\:*?”<>｜
     */
    private String name;

    /**
     * 英文名称。同一个层级的部门名称不能重复。需要在管理后台开启多语言支持才能生效。长度限制为1~32个字符，字符不能包括\:*?”<>｜
     */
    private String nameEn;

    /**
     * 父部门id，32位整型
     */
    private Long parentId;

    /**
     * 在父部门中的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)
     */
    private Long order;

    /**
     * 部门id，32位整型，指定时必须大于1。若不填该参数，将自动生成id
     */
    private Long id;

    /**
     * 	部门负责人的UserID
     */
    private List<String> departmentLeader;
}
