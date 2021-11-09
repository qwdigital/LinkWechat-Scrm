package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.vo.tag.WeTagVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 企业微信标签Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Repository
public interface WeTagMapper  extends BaseMapper<WeTag>
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
     * 删除企业微信标签
     * 
     * @param id 企业微信标签ID
     * @return 结果
     */
    public int deleteWeTagById(@Param("id") String id);

    /**
     * 批量删除企业微信标签
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTagByIds(@Param("ids") String[] ids);


    /**
     * 批量插入标签
     * @param weTags
     * @return
     */
    public int batchInsetWeTag(List<WeTag> weTags);


    /**
     * 标签批量更新
     * @param weTags
     * @return
     */
    public int batchUpdateWeTag(List<WeTag> weTags);

    /**
     * 根据标签id批量查询
     * @param tagIds
     * @return
     */
    List<WeTagVo> selectWeTagByIds(List<String> tagIds);
}
