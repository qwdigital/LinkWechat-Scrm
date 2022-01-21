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


}
