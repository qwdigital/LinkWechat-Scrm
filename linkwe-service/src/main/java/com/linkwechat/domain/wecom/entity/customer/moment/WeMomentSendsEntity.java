package com.linkwechat.domain.wecom.entity.customer.moment;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 发表任务的执行者
 * @date 2021/12/6 13:48
 **/
@Data
public class WeMomentSendsEntity {
    /**
     * 发表任务的执行者用户
     */
    private List<String> user_list;

    /**
     * 发表任务的执行者部门
     */
    private List<Integer> department_list;
}
