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
     * 删除企业微信标签信息
     * 
     * @param id 企业微信标签ID
     * @return 结果
     */
    public int deleteWeTagById(String id);



    void creatTag(String tagId);

    void deleteTag(String tagId);

    void updateTag(String tagId);


}
