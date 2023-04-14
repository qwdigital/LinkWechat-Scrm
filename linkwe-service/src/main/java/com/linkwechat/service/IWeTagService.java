package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.WeTagGroup;

import java.util.List;

public interface IWeTagService extends IService<WeTag> {

    /**
     * 添加标签同步到微信端
     * @param weTagGroup
     * @param weTags
     */
    void addWxTag(WeTagGroup weTagGroup, List<WeTag> weTags) throws WeComException;


    /**
     * 从企业微信端移除抱歉
     * @param groupId
     * @param removeWeTags
     * @param removeGroup 移除组
     */
    void removeWxTag(String groupId,List<WeTag> removeWeTags,boolean removeGroup );
    /**
     * 从企业微信端移除抱歉
     * @param groupId
     * @param removeWeTags
     * @param removeGroup 移除组
     * @param qwNotify 是否调用企微接口
     */
    void removeWxTag(String groupId,List<WeTag> removeWeTags,boolean removeGroup, Boolean qwNotify);


    /**
     * 新增或更新标签(根据企业微信返回的id唯一索引)
     * @param weTags
     */
    void batchAddOrUpdate(List<WeTag> weTags);


    /**
     * 根据标签组ID查询标签列表
     * @param groupIds 标签组ID
     * @return
     */
    List<WeTag> getTagListByGroupIds(List<String> groupIds);
}
