package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeGroupSop;
import com.linkwechat.wecom.domain.vo.WeGroupSopVo;

import java.util.List;

/**
 * 社区运营 群sop service接口
 */
public interface IWeGroupSopService {

    /**
     * 通过规则id获取sop规则
     *
     * @param ruleId 规则id
     * @return 结果
     */
    WeGroupSopVo getGroupSopById(Long ruleId);

    /**
     * 通过过滤条件获取群sop列表
     *
     * @param ruleName  规则名称
     * @param createBy  创建者
     * @param beginTime 创建区间 - 开始时间
     * @param endTime   创建区间 - 结束时间
     * @return 群sop规则列表
     */
    List<WeGroupSopVo> getGroupSopList(String ruleName, String createBy, String beginTime, String endTime);


    /**
     * 新增群sop
     *
     * @param weGroupSop     新增所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picList        手动上传的图片URL
     * @return 结果
     */
    int addGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList);


    /**
     * 更新群sop
     *
     * @param weGroupSop     更新所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picList        手动上传的图片URL
     * @return 结果
     */
    int updateGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList);

    /**
     * 批量删除群sop
     *
     * @param ids sop规则id列表
     * @return 结果
     */
    int batchRemoveGroupSopByIds(Long[] ids);

    /**
     * 校验规则名是否唯一
     *
     * @param ruleName 规则名
     * @return 是否唯一
     */
    boolean isRuleNameUnique(String ruleName);
}
