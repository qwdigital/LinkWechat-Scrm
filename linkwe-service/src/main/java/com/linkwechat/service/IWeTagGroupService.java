package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeTagGroup;

import java.util.List;

public interface IWeTagGroupService extends IService<WeTagGroup> {



    /**
     * 查询标签组列表
     *
     * @param weTagGroup 标签组
     * @return 标签组集合
     */
    List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup);

    /**
     * 新增标签组
     *
     * @param weTagGroup 标签组
     * @return 结果
     */
     void insertWeTagGroup(WeTagGroup weTagGroup);


    /**
     * 修改标签组
     *
     * @param weTagGroup 标签组
     * @return 结果
     */
     void updateWeTagGroup(WeTagGroup weTagGroup);

    /**
     * 批量删除标签组
     *
     * @param ids 需要删除的标签组ID
     * @return 结果
     */
     void deleteWeTagGroupByIds(String[] ids);



    /**
     * 同步标签（mq发送同步消息）
     * @return
     */
     void synchWeTags();


    /**
     * 同步企业标签(监听mq同步)
     * @param msg
     */
    void synchWeGroupTagHandler(String msg);






    /**
     * 指定标签同步
     * @param businessId
     * @param tagType
     * @param isCallBack true来自回调 false 不来自回调
     */
    void synchWeGroupAndTag(String businessId,String tagType,boolean isCallBack);

    /**
     * 查询标签组列表（分页）
     * @param tagGroup
     * @return
     */
    List<WeTagGroup> getTagGroupPageList(WeTagGroup tagGroup);

    /**
     * 查询标签组列表（不分页）
     * @param tagGroup
     * @return
     */
    List<WeTagGroup> getTagGroupList(WeTagGroup tagGroup);
}
