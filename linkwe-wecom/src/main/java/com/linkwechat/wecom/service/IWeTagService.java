package com.linkwechat.wecom.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.dto.WeTagDto;
import com.linkwechat.wecom.domain.vo.tag.WeTagVo;

/**
 * 企业微信标签Service接口
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public interface IWeTagService  extends IService<WeTag>
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
    public int deleteWeTagByIds(String[] ids);

    /**
     * 删除企业微信标签信息
     * 
     * @param id 企业微信标签ID
     * @return 结果
     */
    public int deleteWeTagById(String id);


    /**
     * 来自企业微信传输实体添加标签
     * @param weTagDto
     * @return
     */
    public int insertWeTagFromWeTagDto(WeTagDto weTagDto);

    void creatTag(String tagId);

    void deleteTag(String tagId);

    void updateTag(String tagId);

    /**
     * 根据标签id查询企业微信标签
     *
     * @param ids 企业微信标签ID
     * @return WeTagVo
     */
    List<WeTagVo> selectWeTagByIds(List<String> ids);
}
