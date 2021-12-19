package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagDto;

import java.util.List;

/**
 * 标签组Service接口
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public interface IWeTagGroupService  extends IService<WeTagGroup>
{


    /**
     * 查询标签组列表
     * 
     * @param weTagGroup 标签组
     * @return 标签组集合
     */
    public List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup);

    /**
     * 新增标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    public void insertWeTagGroup(WeTagGroup weTagGroup);

    /**
     * 修改标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    public void updateWeTagGroup(WeTagGroup weTagGroup);

    /**
     * 批量删除标签组
     * 
     * @param ids 需要删除的标签组ID
     * @return 结果
     */
    public void deleteWeTagGroupByIds(String[] ids);



    /**
     * 同步标签
     * @return
     */
    public void synchWeTags();


    /**
     * 来自微信端批量保存或者更新标签组和标签
     * @param tagGroup
     * @param isSync
     */
    public void batchSaveOrUpdateTagGroupAndTag(List<WeCropGroupTagDto> tagGroup,Boolean isSync);

    void createTagGroup(String id);

    void deleteTagGroup(String id);

    void updateTagGroup(String id);



//    /**
//     * 根据企业员工与添加客户关系id给客户打标签
//     * @param flowerCustomerRelId
//     * @return
//     */
//    public List<WeTagGroup> findCustomerTagByFlowerCustomerRelId(String externalUserid, String userid);
}
