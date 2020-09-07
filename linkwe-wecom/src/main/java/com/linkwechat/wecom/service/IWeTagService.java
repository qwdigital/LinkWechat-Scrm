package com.linkwechat.wecom.service;

import java.util.List;
import com.linkwechat.wecom.domain.WeTag;

/**
 * 企业微信标签Service接口
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public interface IWeTagService 
{
    /**
     * 查询企业微信标签
     * 
     * @param id 企业微信标签ID
     * @return 企业微信标签
     */
    public WeTag selectWeTagById(Long id);

    /**
     * 查询企业微信标签列表
     * 
     * @param weTag 企业微信标签
     * @return 企业微信标签集合
     */
    public List<WeTag> selectWeTagList(WeTag weTag);

    /**
     * 新增企业微信标签
     * 
     * @param weTag 企业微信标签
     * @return 结果
     */
    public int insertWeTag(WeTag weTag);

    /**
     * 修改企业微信标签
     * 
     * @param weTag 企业微信标签
     * @return 结果
     */
    public int updateWeTag(WeTag weTag);

    /**
     * 批量删除企业微信标签
     * 
     * @param ids 需要删除的企业微信标签ID
     * @return 结果
     */
    public int deleteWeTagByIds(Long[] ids);

    /**
     * 删除企业微信标签信息
     * 
     * @param id 企业微信标签ID
     * @return 结果
     */
    public int deleteWeTagById(Long id);
}
