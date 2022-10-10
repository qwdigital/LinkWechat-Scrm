package com.linkwechat.domain.wecom.query.customer.moment;

import com.linkwechat.domain.wecom.entity.customer.moment.WeVisibleRangeEntity;
import com.linkwechat.domain.wecom.query.WeMsgTemplateQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 企业发表内容到客户的朋友圈
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeAddMomentQuery extends WeMsgTemplateQuery {
    /**
     * 发表范围
     */
    private WeVisibleRangeEntity visible_range;

    /**
     *  指定的发表范围；若未指定，则表示执行者为应用可见范围内所有成员
     * @param userList 发表任务的执行者用户列表
     * @param departmentList 发表任务的执行者部门列表
     * @param tagList 可见到该朋友圈的客户列表
     */
    public void setWeVisibleRange(List<String> userList, List<Integer> departmentList, List<String> tagList){
        WeVisibleRangeEntity weVisibleRangeEntity = new WeVisibleRangeEntity();
        weVisibleRangeEntity.setSenderList(userList,departmentList);
        weVisibleRangeEntity.setExternalContactList(tagList);
    }

}
