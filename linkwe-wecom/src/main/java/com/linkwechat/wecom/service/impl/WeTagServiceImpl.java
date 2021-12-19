package com.linkwechat.wecom.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCropTagClient;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.domain.dto.WeTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagListDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeFindCropTagParam;
import com.linkwechat.wecom.domain.vo.tag.WeTagVo;
import com.linkwechat.wecom.service.IWeTagGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeTagMapper;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.service.IWeTagService;

/**
 * 企业微信标签Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Service
public class WeTagServiceImpl extends ServiceImpl<WeTagMapper,WeTag> implements IWeTagService
{
    @Autowired
    private WeTagMapper weTagMapper;
    @Autowired
    private WeCropTagClient weCropTagClient;
    @Autowired
    private IWeTagGroupService weTagGroupService;

    /**
     * 查询企业微信标签
     * 
     * @param id 企业微信标签ID
     * @return 企业微信标签
     */
    @Override
    public WeTag selectWeTagById(Long id)
    {
        return weTagMapper.selectWeTagById(id);
    }

    /**
     * 查询企业微信标签列表
     * 
     * @param weTag 企业微信标签
     * @return 企业微信标签
     */
    @Override
    public List<WeTag> selectWeTagList(WeTag weTag)
    {
        return weTagMapper.selectWeTagList(weTag);
    }

    /**
     * 新增企业微信标签
     * 
     * @param weTag 企业微信标签
     * @return 结果
     */
    @Override
    public int insertWeTag(WeTag weTag)
    {
        return weTagMapper.insertWeTag(weTag);
    }

    /**
     * 修改企业微信标签
     * 
     * @param weTag 企业微信标签
     * @return 结果
     */
    @Override
    public int updateWeTag(WeTag weTag)
    {
        return weTagMapper.updateWeTag(weTag);
    }

    /**
     * 批量删除企业微信标签
     * 
     * @param ids 需要删除的企业微信标签ID
     * @return 结果
     */
    @Override
    public int deleteWeTagByIds(String[] ids)
    {
        return weTagMapper.deleteWeTagByIds(ids);
    }

    /**
     * 删除企业微信标签信息
     * 
     * @param id 企业微信标签ID
     * @return 结果
     */
    @Override
    public int deleteWeTagById(String id)
    {
        return weTagMapper.deleteWeTagById(id);
    }


    /**
     * 来自weTagDto实体添加标签（该实体为获取客户时相关联系人下的实体）
     * @param weTagDto
     * @return
     */
    @Override
    public int insertWeTagFromWeTagDto(WeTagDto weTagDto) {
        //查询有无当前标签组，没有则创建一个标签组，同时创建该标签

        //查询当前标签组+标签名校验查询当前标签，没有，则创建标签

        //之后将标签体转化为关系入库




        return 0;
    }

    @Override
    public void creatTag(String tagId) {
        if (StringUtils.isNotEmpty(tagId)){
            WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(WeFindCropTagParam.builder().tag_id(tagId.split(",")).build());
            List<WeCropGroupTagDto> tag_group = weCropGroupTagListDto.getTag_group();
            if (CollectionUtil.isNotEmpty(tag_group)) {
                tag_group.stream().forEach(k -> {
                    List<WeCropTagDto> tag = k.getTag();
                    if (CollectionUtil.isNotEmpty(tag)) {
                        List<WeTag> weTags = new ArrayList<>();
                        tag.stream().forEach(v -> {
                            WeTag tagInfo = this.getOne(new LambdaQueryWrapper<WeTag>()
                                    .eq(WeTag::getGroupId, k.getGroup_id())
                                    .eq(WeTag::getTagId, v.getId()));
                            if (tagInfo == null) {
                                WeTag weTag = new WeTag();
                                weTag.setTagId(v.getId());
                                weTag.setGroupId(k.getGroup_id());
                                weTag.setName(v.getName());
                                weTags.add(weTag);
                            }
                        });
                        this.saveBatch(weTags);
                    }
                });
            }
        }
    }

    @Override
    public void deleteTag(String tagId) {
        this.deleteWeTagById(tagId);
    }

    @Override
    public void updateTag(String tagId) {
        if (StringUtils.isNotEmpty(tagId)){
            WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(WeFindCropTagParam.builder().tag_id(tagId.split(",")).build());
            List<WeCropGroupTagDto> tag_group = weCropGroupTagListDto.getTag_group();
            if (CollectionUtil.isNotEmpty(tag_group)) {
                tag_group.stream().forEach(k -> {
                    List<WeCropTagDto> tag = k.getTag();
                    if (CollectionUtil.isNotEmpty(tag)) {
                        List<WeTag> weTags = new ArrayList<>();
                        tag.stream().forEach(v -> {
                            WeTag weTag = new WeTag();
                            weTag.setTagId(v.getId());
                            weTag.setGroupId(k.getGroup_id());
                            weTag.setName(v.getName());
                            weTags.add(weTag);
                        });
                        this.saveOrUpdateBatch(weTags);
                    }
                });
            }
        }
    }

    @Override
    public List<WeTagVo> selectWeTagByIds(List<String> tagIds) {
        return this.baseMapper.selectWeTagByIds(tagIds);
    }
}
