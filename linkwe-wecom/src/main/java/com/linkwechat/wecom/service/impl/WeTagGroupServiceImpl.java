package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.linkwechat.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeTagGroupMapper;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.service.IWeTagGroupService;

/**
 * 标签组Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Service
public class WeTagGroupServiceImpl implements IWeTagGroupService 
{
    @Autowired
    private WeTagGroupMapper weTagGroupMapper;

    /**
     * 查询标签组
     * 
     * @param id 标签组ID
     * @return 标签组
     */
    @Override
    public WeTagGroup selectWeTagGroupById(Long id)
    {
        return weTagGroupMapper.selectWeTagGroupById(id);
    }

    /**
     * 查询标签组列表
     * 
     * @param weTagGroup 标签组
     * @return 标签组
     */
    @Override
    public List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup)
    {
        return weTagGroupMapper.selectWeTagGroupList(weTagGroup);
    }

    /**
     * 新增标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    @Override
    public int insertWeTagGroup(WeTagGroup weTagGroup)
    {
        weTagGroup.setCreateTime(DateUtils.getNowDate());
        return weTagGroupMapper.insertWeTagGroup(weTagGroup);
    }

    /**
     * 修改标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    @Override
    public int updateWeTagGroup(WeTagGroup weTagGroup)
    {
        return weTagGroupMapper.updateWeTagGroup(weTagGroup);
    }

    /**
     * 批量删除标签组
     * 
     * @param ids 需要删除的标签组ID
     * @return 结果
     */
    @Override
    public int deleteWeTagGroupByIds(Long[] ids)
    {
        return weTagGroupMapper.deleteWeTagGroupByIds(ids);
    }

    /**
     * 删除标签组信息
     * 
     * @param id 标签组ID
     * @return 结果
     */
    @Override
    public int deleteWeTagGroupById(Long id)
    {
        return weTagGroupMapper.deleteWeTagGroupById(id);
    }
}
