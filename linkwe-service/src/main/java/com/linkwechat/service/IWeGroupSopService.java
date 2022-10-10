package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.community.WeGroupSop;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import com.linkwechat.domain.community.vo.WeGroupSopVo;

import java.util.List;

public interface IWeGroupSopService extends IService<WeGroupSop> {

    /**
     * 校验是否重名
     * @param ruleName
     * @return
     */
    boolean isNameOccupied(String ruleName);


    /**
     * 新增群sop
     *
     * @param weGroupSop     新增所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picList        手动上传的图片URL
     * @return 结果
     */
    void addGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList);


    /**
     * 批量删除群sop
     *
     * @param ids sop规则id列表
     * @return 结果
     */
    void batchRemoveGroupSopByIds(Long[] ids);


    /**
     * 更新群sop
     *
     * @param weGroupSop     更新所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picList        手动上传的图片URL
     * @return 结果
     */
    void updateGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList);



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
     * 向指定的群聊进行sop企微消息推送
     *
     * @param groupIdList 群聊id列表
     */
    void sendMessage(List<String> groupIdList);


    /**
     * 根据员工id获取对应的sop任务列表
     *
     * @param emplId 员工id
     * @param isDone 已完成还是待处理
     * @return 结果
     */
    List<WeGroupSopVo> getEmplTaskList(String emplId, boolean isDone);


    /**
     * 变更某员工sop规则发送任务的状态
     *
     * @param ruleId 规则名称
     * @param emplId 群聊的群主id
     * @return 结果
     */
    int updateChatSopStatus(Long ruleId, String emplId);



    /**
     * 根据SOP 规则id获取所有使用人员信息
     *
     * @param ruleId sop id
     * @return 结果
     */
    List<WeCommunityTaskEmplVo> getScopeListByRuleId(Long ruleId);


}
